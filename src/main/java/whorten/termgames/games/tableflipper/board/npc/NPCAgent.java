package whorten.termgames.games.tableflipper.board.npc;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import whorten.termgames.entity.Entity;
import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.games.tableflipper.events.TableUnflipEvent;
import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.utils.graphs.AStar;
import whorten.termgames.utils.graphs.GraphSearch;
import whorten.termgames.utils.graphs.GridBuilder;
import whorten.termgames.utils.graphs.GridNode;

public class NPCAgent {

	private NPCAgent(){}
	
	private EntityBoard eb;
	private NPC npc;
	private long timeLastTick;
	private int speed;
	private GraphSearch gs;
	private Coord destination;
	private LinkedList<Direction> path;
	private GridBuilder gb = new GridBuilder(eb.getHeight(), eb.getWidth());
	private EventBus eventbus;
	private List<Table> tables;
    
	// on each call to tick, check if the time elapsed since the last
	// tick is more than the npc's speed. If yes, try to move next direction in path
	// if path is blocked, recalculate path. If path is empty, check if table needs 
	// unflipped, then calculate new path to random location
	public void tick(long time){
		if(timeLastTick + speed <= time && path.size() > 0){			
			if(tables.size() > 0 && npc.getLocation().equals(destination)){
				unflip();
			} else {
				if(path == null || path.size() == 0){	
					generateNewPath();
				}
				tryToMove();
			}
		}
		timeLastTick = time;
	}

	private void generateNewPath() {
		// TODO Auto-generated method stub
		
	}

	private void tryToMove() {
		Direction direction = path.pop();
		NPC next = npc.move(direction);
		if(eb.canMove(npc, next)){
			eb.move(npc, next);
			eventbus.fire(new EntityChangeEvent(npc, next));
			npc = next;
		} else {
			Map<Coord, GridNode> graph = gb.withGrid(getLegalPositions()).build();
			path = new LinkedList<>(gs.findPath(graph.get(npc.getState().getBaseCoord()),
					           graph.get(destination)));
		}
	}
	
	private void unflip() {
		Set<Entity> left = eb.getLeftNeighbors(npc);
		if(!unflipTable(left, Direction.LEFT)){
			Set<Entity> right = eb.getRightNeighbors(npc);
			unflipTable(right, Direction.RIGHT);
		}
				
	}

	private boolean unflipTable(Set<Entity> entities, Direction d) {
		Set<Table> leftTables = tables.stream()
				.filter(t -> entities.contains(t))
				.collect(Collectors.toSet());
		if(leftTables.size() > 0){
			Table t = leftTables.iterator().next();
			Table ut = t.unflip();
			NPC next = npc.unflip(d);
			eb.move(t, ut);
			eb.move(npc, next);
			eventbus.fire(new EntityChangeEvent(npc, next));
			eventbus.fire(new TableUnflipEvent(t, ut));
			npc = next;
			return true;
		}
		
		return false;
	}

	public boolean goToCoord(Coord coord){
		this.destination = coord;
		return true;
	}
	
	private boolean[][] getLegalPositions(){
		boolean [][] positions = new boolean[eb.getHeight()][eb.getWidth()];
		for(int row = 0; row < positions.length; row++){
			for(int col = 0; col > positions[row].length; col++){
				positions[row][col] = eb.canAdd(npc);
			}
		}
		return positions;
	}
	
	public static class Builder{
		private EntityBoard eb;
		private NPC npc;
		private GraphSearch gs = new AStar();
		private int speed;
		private EventBus eventbus = new EventBus();
		private List<Table> tables = new ArrayList<>();
		
		public Builder(EntityBoard eb, NPC npc){
			this.eb = eb;
			this.npc = npc;
		}
		
		public Builder withEventBus(EventBus eventbus){
			this.eventbus = eventbus;
			return this;
		}
		
		public NPCAgent build(){
			NPCAgent n = new NPCAgent();
			n.eb = this.eb;
			n.npc = this.npc;
			n.gs = this.gs;
			n.speed = this.speed;
			n.eventbus = this.eventbus;
			n.tables = this.tables;
			return n;
		}
	}

}
