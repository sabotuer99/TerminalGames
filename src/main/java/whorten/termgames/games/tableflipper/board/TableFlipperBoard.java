package whorten.termgames.games.tableflipper.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import whorten.termgames.entity.Entity;
import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.npc.NPC;
import whorten.termgames.games.tableflipper.board.npc.NPCAgent;
import whorten.termgames.games.tableflipper.board.player.Player;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.games.tableflipper.board.wall.Wall;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.games.tableflipper.events.EntitySpawnEvent;
import whorten.termgames.games.tableflipper.events.PlayerMoveEvent;
import whorten.termgames.games.tableflipper.events.TableFlipEvent;
import whorten.termgames.geometry.Coord;

public class TableFlipperBoard {

	private final static Logger logger = LogManager.getLogger(TableFlipperBoard.class);
	private EntityBoard board;
	private Player player;
	private List<Table> tables;
	private List<NPCAgent> agents;
	private List<NPC> npcs;
	private EventBus eventbus;
	long lastPlayerMove;
	
	private TableFlipperBoard(){}
	
	public synchronized void movePlayerUp(long time){
		lastPlayerMove = time;
		Player next = player.moveUp(1);
		safeMovePlayer(next, true);
	}
	
	public synchronized void movePlayerDown(long time){
		lastPlayerMove = time;
		Player next = player.moveDown(1);
		safeMovePlayer(next, true);
	}
	
	public synchronized void movePlayerLeft(long time){
		lastPlayerMove = time;
		Player next = player.moveLeft(2);
		if(!safeMovePlayer(next, true)){
			next = player.moveLeft(1);
			safeMovePlayer(next, true);
		}
	}
	
	public synchronized void movePlayerRight(long time){
		lastPlayerMove = time;
		Player next = player.moveRight(2);
		if(!safeMovePlayer(next, true)){
			next = player.moveRight(1);
			safeMovePlayer(next, true);
		}
	}
	
	public synchronized void flip(long time){
		lastPlayerMove = time;
		boolean left = tableLeft();
		boolean right = tableRight();
		Player next = player.flip(left, right);
		if(left){ flipNeighbors(board.getLeftNeighbors(player)); }
		if(right){ flipNeighbors(board.getRightNeighbors(player)); }
		safeMovePlayer(next, false);
	}

	private void flipNeighbors(Set<Entity> neighbors) {
		logger.info("Trying to flip table");		
		List<TableFlipEvent> events = new ArrayList<>();
		for(Table table : tables){
			if(neighbors.contains(table) && !table.isFlipped()){
				logger.info("Flipping table");
				Table flipped = table.flip();
				events.add(new TableFlipEvent(table, flipped));
			}
		}
		for(TableFlipEvent ece : events){		
			flipTable(ece.getUnflipped(), ece.getFlipped());
			eventbus.fire(ece);
		}
		
	}

	private boolean tableLeft() {
		Set<Entity> left = board.getLeftNeighbors(player);
		return tables.containsAll(left);
	}
	
	private boolean tableRight() {
		Set<Entity> right = board.getRightNeighbors(player);
		return tables.containsAll(right);
	}

	public synchronized void tick(long time){
		if(time > lastPlayerMove + 250){
			Player next = player.stand();
			safeMovePlayer(next, false);
		}
		for(NPCAgent agent : agents){
			agent.tick(time);
		}
	}

	private boolean safeMovePlayer(Player next, boolean playermove) {
		if(board.canMove(player, next) && !player.getState().equals(next.getState())){
			board.move(player, next);
			eventbus.fire(new EntityChangeEvent(player, next));
			if(playermove){
				logger.info(String.format("Player moving to %s", next.getBaseCoord()));
				eventbus.fire(new PlayerMoveEvent());				
			}
			player = next;
			return true;
		} else {
			return false;			
		}
	}
	
	public Entity getPlayer() {
		return player;
	}
	
	public List<Entity> getNPCs() {
		return new ArrayList<>(npcs);
	}

	public synchronized void addNpc(NPC npc, int speed) {
		npcs.add(npc);
		board.addEntity(npc);
		NPCAgent agent = new NPCAgent.Builder(board, npc)
				.withSpeed(speed)
				.withEventBus(eventbus)
				.withTableFlipperBoard(this)
				.build();
		agent.tick(speed); //initialize
		agents.add(agent);
		eventbus.fire(new EntitySpawnEvent(npc));
	}

	public synchronized void addRandomNpc() {
		NPC start = NPC.newInstance(new Coord(0,0));
		List<Coord> coords = new ArrayList<>(board.getLegalPositions(start));
		Collections.shuffle(coords);
		Coord base = coords.get(0);
		int speed = 300 + new Random().nextInt(250);
		
		NPC npc = start.moveTo(base).stand().toBuilder().build();
		addNpc(npc, speed);
	}
	
	public synchronized void addRandomTable() {
		Table start = Table.newInstance(new Coord(0,0));
		List<Coord> coords = new ArrayList<>(board.getLegalPositions(start));
		Collections.shuffle(coords);
		Coord base = coords.get(0);
		for(int i = 0; i < coords.size() && !checkTableClearance(base); i++){
			base = coords.get(i);
		}
		
		Table table = Table.newInstance(base);
		tables.add(table);
		board.addEntity(table);
		eventbus.fire(new EntitySpawnEvent(table));
	}
	
	private boolean checkTableClearance(Coord base) {
		//TODO for now don't bother with clearance
		return true;
	}
	
	private void assignTableToAgent(Table table){
		if(agents != null && agents.size() > 0){
			Collections.shuffle(agents);
			agents.get(0).addTable(table);
		}
	}

	public static class Builder{
		EntityBoard board = new EntityBoard.Builder()
							.withHeight(22)
							.withWidth(57)
							.build();
		Player player = Player.newInstance(new Coord(0,0));
		List<Table> tables = new ArrayList<>();
		List<NPC> npcs = new ArrayList<>();
		List<NPCAgent> agents = new ArrayList<>();
		List<Wall> walls = new ArrayList<>();
		EventBus eventbus;
		
		public Builder(EventBus eventbus){
			this.eventbus = eventbus;
		}
		
		public Builder addNpc(NPC npc){
			npcs.add(npc);
			agents.add(new NPCAgent.Builder(board, npc).build());
			return this;
		}
		
		public TableFlipperBoard build(){
			TableFlipperBoard tfb = new TableFlipperBoard();
			tfb.board = board;
			tfb.player = player;
			tfb.tables = tables;
			tfb.eventbus = eventbus;
			tfb.agents = agents;
			tfb.npcs = npcs;
			
			tfb.board.addEntity(player);
			tfb.board.addAll(tables);
			tfb.board.addAll(walls);
			tfb.board.addAll(npcs);
			
			return tfb;
		}
	}

	public synchronized void unflipTable(Table flipped, Table unflipped) {
		board.move(flipped, unflipped);
		tables.remove(flipped);
		tables.add(unflipped);		
	}
	
	private synchronized void flipTable(Table unflipped, Table flipped){
		board.move(unflipped, flipped);
		assignTableToAgent(flipped);
		tables.remove(unflipped);
		tables.add(flipped);
	}

}
