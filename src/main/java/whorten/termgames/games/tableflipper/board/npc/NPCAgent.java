package whorten.termgames.games.tableflipper.board.npc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.entity.Entity;
import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.TableFlipperBoard;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.games.tableflipper.events.NPCMoveEvent;
import whorten.termgames.games.tableflipper.events.TableUnflipEvent;
import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.utils.graphs.AStar;
import whorten.termgames.utils.graphs.GraphSearch;
import whorten.termgames.utils.graphs.GridBuilder;
import whorten.termgames.utils.graphs.GridNode;

public class NPCAgent {

	private NPCAgent(){}
	private final static Logger logger = LogManager.getLogger(NPCAgent.class);
	private EntityBoard entityBoard;
	private NPC npc;
	private volatile long timeLastTick;
	private int speed;
	private GraphSearch gs;
	private Coord destination;
	private LinkedList<Direction> path;
	private GridBuilder gb;
	private EventBus eventbus;
	private LinkedList<Table> tables;
	private TableFlipperBoard tfBoard;
	private int standStillCounter;
    
	// on each call to tick, check if the time elapsed since the last
	// tick is more than the npc's speed. If yes, try to move next direction in path
	// if path is blocked, recalculate path. If path is empty, check if table needs 
	// unflipped, then calculate new path to random location
	public void tick(long time){
		if(timeLastTick + speed <= time){	
			//logger.info(String.format("Agent taking action: %d + %d <= %d", timeLastTick, speed, time));
			if(tables.size() > 0 && npc.getBaseCoord().equals(destination)){
				unflip();
			} 
			
			if(path == null || path.size() == 0){	
				pickDestination();
				generateNewPath();												
			} else {		
				Coord before = npc.getBaseCoord();
				tryToMove();
				if(before.equals(npc.getBaseCoord())){
					// couldn't move, increment counter
					standStillCounter++;
				} else {
					standStillCounter = 0;
				}
			}
			
			if(standStillCounter > 10){
				// npc stuck, send to random location
				setRandomDestination();
				generateNewPath();
			}

			timeLastTick = time;
		} else {
			//logger.info(String.format("Agent waiting: %d + %d > %d", timeLastTick, speed, time));
		}
	}

	private void pickDestination() {
		// if npc has not reached original destination, just keep it
		if(destination != null && !npc.getLocation().equals(destination)){
			logger.info("Still not at destination, will try again...");
			standStillCounter++;
			return;
		}
		
		// otherwise, check if a table needs unflipped
		if(tables.size() > 0){
			if(tryToFindPathToTable()){
				return;
			}
		}	
		setRandomDestination();
	}

	private boolean tryToFindPathToTable() {
		logger.info("Table assigned, looking for path to unflip.");
		// loop through tables once looking for valid destination. 
		// If none, just fall through to random destination
		for(int i = 0; i < tables.size(); i++){
			Table table = tables.peek();
			// if we can move to the right or left, we're done
			if(moveToLeft(table)){return true;}
			if(moveToRight(table)){return true;}
			// if we got here, we couldn't use this table, put it back
			tables.addLast(tables.pop());
		}
		logger.info("All tables blocked =(");
		return false;
	}

	private void setRandomDestination() {
		// if not tables need unflipped, just wander around
		logger.info("Generating a random destination.");
		List<Coord> coords = new ArrayList<>(entityBoard.getLegalPositions(npc));
		if(coords != null && coords.size() > 0){
			logger.info("Picking randomly from coords");
			Collections.shuffle(coords);
			destination = coords.get(0);
			logger.info(String.format("Set destination to %s", destination));
		}
	}

	private void generateNewPath() {
		logger.info("Generating new path.");
		path = getPath(npc.getBaseCoord(), destination);
		logger.info(String.format("New path is %d steps", path.size()));	
	}

	private boolean moveToLeft(Table table) {
		logger.info("Checking if left side of table is valid destination...");
		Coord leftCoord = entityBoard.leftOf(table, npc);
		NPC left = npc.moveTo(leftCoord);
		if(entityBoard.canMove(npc, left)){
			logger.info("Setting left side (%s) as destination", leftCoord);
			destination = leftCoord;
			return true;
		}
		logger.info("Left side blocked.");
		return false;
	}
	
	private boolean moveToRight(Table table) {		
		logger.info("Checking if right side of table is valid destination...");
		Coord rightCoord = entityBoard.rightOf(table, npc);
		NPC right = npc.moveTo(rightCoord);
		if(entityBoard.canMove(npc, right)){
			logger.info("Setting right side (%s) as destination", rightCoord);
			destination = rightCoord;
			return true;
		}		
		logger.info("Right side blocked.");
		return false;
	}

	private void tryToMove() {
		Direction direction = path.pop();
		NPC next = npc.move(direction, 1);
		logger.info(String.format("NPC trying to move %s to %s from %s", 
				direction, next.getBaseCoord(), npc.getBaseCoord()));
		if(entityBoard.canMove(npc, next)){
			logger.info(String.format("NPC moving to %s...", next.getBaseCoord()));
			entityBoard.move(npc, next);
			eventbus.fire(new EntityChangeEvent(npc, next));
			eventbus.fire(new NPCMoveEvent());
			npc = next;
		} else {
			logger.info(String.format("Could not move to %s, calculating new path...", next.getBaseCoord()));
			path = getPath(npc.getState().getBaseCoord(), destination);
		}
	}

	private LinkedList<Direction> getPath(Coord from, Coord to) {
		if(from.equals(to)){
			return new LinkedList<Direction>();
		}
		
		logger.info(String.format("Calculating grid of legal positions"));
		boolean[][] legals = entityBoard.getLegalPositionsGrid(npc);
		logger.info(String.format("Calculating graph across legal positions"));
		Map<Coord, GridNode> graph = gb.withGrid(legals).build();
		
		logger.info(String.format("Minimum coord in graph: %s", new TreeSet<>(graph.keySet()).iterator().next()));
		//logger.info("\n" + Coord.toAsciiString(graph.keySet()));
		
		GridNode fromNode = graph.get(from);
		logger.info(String.format("From node: %s", fromNode));
		GridNode toNode = graph.get(to);
		logger.info(String.format("To node: %s", toNode));
		List<Direction> newpath = gs.findPath(fromNode, toNode);
		logger.info(String.format("Size of new path: %d", newpath.size()));
		return new LinkedList<>(newpath);
	}
	
	private void unflip() {
		Set<Entity> left = entityBoard.getLeftNeighbors(npc);
		logger.info(String.format("Unflip left neighbor count: %d", left.size()));
		if(!unflipTable(left, Direction.LEFT)){
			Set<Entity> right = entityBoard.getRightNeighbors(npc);
			logger.info(String.format("Unflip right neighbor count: %d", left.size()));
			unflipTable(right, Direction.RIGHT);
		}
				
	}

	private boolean unflipTable(Set<Entity> entities, Direction d) {
		if(entities == null || entities.size() == 0){
			return false;
		}
		for(Entity e : entities){
			if(e != null){
				logger.info(String.format("Entity to unflip: %s", e.toString()));				
			}
		}
		Set<Table> ntables = tables.stream()
				.filter(t -> entities.contains(t))
				.collect(Collectors.toSet());
		for(Table t : ntables){
			logger.info(String.format("Table to unflip: %s", t.toString()));
			if(t.isFlipped()){
				tables.remove(t);
				Table ut = t.unflip();
				NPC next = npc.unflip(d);
				tfBoard.unflipTable(t, ut);
				entityBoard.move(npc, next);
				eventbus.fire(new TableUnflipEvent(t, ut));
				eventbus.fire(new EntityChangeEvent(npc, next));
				npc = next;
				return true;
			}
		}
		
		return false;
	}
	
	public static class Builder{
		private EntityBoard eb;
		private NPC npc;
		private GraphSearch gs = new AStar();
		private int speed;
		private EventBus eventbus;
		private LinkedList<Table> tables = new LinkedList<>();
		private TableFlipperBoard tableFlipperBoard;
		
		public Builder(EntityBoard eb, NPC npc){
			this.eb = eb;
			this.npc = npc;
		}
		
		public Builder withEventBus(EventBus eventbus){
			this.eventbus = eventbus;
			return this;
		}
		
		public Builder withSpeed(int speed){
			this.speed = speed;
			return this;
		}
		
		public NPCAgent build(){
			NPCAgent n = new NPCAgent();
			n.entityBoard = eb;
			n.npc = npc;
			n.gs = gs;
			n.speed = speed;
			n.eventbus = eventbus;
			n.tables = tables;
			n.gb = new GridBuilder(eb.getHeight(), eb.getWidth());
			n.path = new LinkedList<>();
			n.tfBoard = tableFlipperBoard;
			return n;
		}

		public Builder withTableFlipperBoard(TableFlipperBoard tableFlipperBoard) {
			this.tableFlipperBoard = tableFlipperBoard;
			return this;
		}
	}

	public void addTable(Table table) {
		tables.add(table);
	}

}
