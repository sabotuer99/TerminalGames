package whorten.termgames.games.tableflipper.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import whorten.termgames.entity.Entity;
import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.npc.NPC;
import whorten.termgames.games.tableflipper.board.player.Player;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.games.tableflipper.board.wall.Wall;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.geometry.Coord;

public class TableFlipperBoard {

	EntityBoard board;
	Player player;
	List<Table> tables;
	List<NPC> npcs;
	List<Wall> walls;
	EventBus eventbus;
	long lastPlayerMove;
	
	private TableFlipperBoard(){}
	
	public synchronized void movePlayerUp(long time){
		lastPlayerMove = time;
		Player next = player.moveUp();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerDown(long time){
		lastPlayerMove = time;
		Player next = player.moveDown();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerLeft(long time){
		lastPlayerMove = time;
		Player next = player.moveLeft();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerRight(long time){
		lastPlayerMove = time;
		Player next = player.moveRight();
		safeMovePlayer(next);
	}
	
	public synchronized void flip(long time){
		lastPlayerMove = time;
		boolean left = tableLeft();
		boolean right = tableRight();
		Player next = player.flip(left, right);
		safeMovePlayer(next);
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
			safeMovePlayer(next);
		}
	}

	private void safeMovePlayer(Player next) {
		if(board.canAdd(next)){
			board.addEntity(next);
			board.removeEntity(player);
			eventbus.fire(new EntityChangeEvent(player, next));
			player = next;
		}
	}
	
	public static class Builder{
		EntityBoard board = new EntityBoard.Builder()
							.withHeight(24)
							.withWidth(60)
							.build();
		Player player = Player.newInstance(new Coord(1,1));
		List<Table> tables = new ArrayList<>();
		List<NPC> npcs = new ArrayList<>();
		List<Wall> walls = new ArrayList<>();
		EventBus eventbus;
		
		public Builder(EventBus eventbus){
			this.eventbus = eventbus;
		}
		
		public TableFlipperBoard build(){
			TableFlipperBoard tfb = new TableFlipperBoard();
			tfb.board = board;
			tfb.player = player;
			tfb.tables = tables;
			tfb.walls = walls;
			tfb.npcs = npcs;
			
			tfb.board.addEntity(player);
			tfb.board.addAll(tables);
			tfb.board.addAll(walls);
			tfb.board.addAll(npcs);
			
			return tfb;
		}
	}
}
