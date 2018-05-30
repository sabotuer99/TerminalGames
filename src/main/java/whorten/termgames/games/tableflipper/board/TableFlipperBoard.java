package whorten.termgames.games.tableflipper.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import whorten.termgames.games.tableflipper.events.PlayerMoveEvent;
import whorten.termgames.geometry.Coord;

public class TableFlipperBoard {

	//private final static Logger logger = LogManager.getLogger(TableFlipperBoard.class);
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
		safeMovePlayer(next, true);
	}
	
	public synchronized void movePlayerRight(long time){
		lastPlayerMove = time;
		Player next = player.moveRight(2);
		safeMovePlayer(next, true);
	}
	
	public synchronized void flip(long time){
		lastPlayerMove = time;
		boolean left = tableLeft();
		boolean right = tableRight();
		Player next = player.flip(left, right);
		safeMovePlayer(next, false);
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

	private void safeMovePlayer(Player next, boolean playermove) {
		board.removeEntity(player);
		if(board.canAdd(next) && !player.getState().equals(next.getState())){
			board.addEntity(next);
			eventbus.fire(new EntityChangeEvent(player, next));
			if(playermove){
				eventbus.fire(new PlayerMoveEvent());				
			}
			player = next;
		} else {
			board.addEntity(player);
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
				.build();
		agent.tick(speed); //initialize
		agents.add(agent);
	}

	public synchronized void addRandomNpc() {
		NPC start = NPC.newInstance(new Coord(0,0));
		List<Coord> coords = new ArrayList<>(board.getLegalPositions(start));
		Collections.shuffle(coords);
		Coord base = coords.get(0);
		int speed = 500 + new Random().nextInt(250);
		
		NPC npc = start.moveTo(base).stand().toBuilder().build();
		addNpc(npc, speed);
	}
	
	public static class Builder{
		EntityBoard board = new EntityBoard.Builder()
							.withHeight(22)
							.withWidth(58)
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

}
