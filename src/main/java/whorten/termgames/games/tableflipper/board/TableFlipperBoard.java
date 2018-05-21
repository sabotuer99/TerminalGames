package whorten.termgames.games.tableflipper.board;

import java.util.List;

import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.npc.NPC;
import whorten.termgames.games.tableflipper.board.player.Player;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.games.tableflipper.board.wall.Wall;
import whorten.termgames.games.tableflipper.events.EntityMoveEvent;

public class TableFlipperBoard {

	EntityBoard board;
	Player player;
	List<Table> tables;
	List<NPC> npcs;
	List<Wall> walls;
	EventBus eventbus;
	
	public synchronized void movePlayerUp(){
		Player next = player.moveUp();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerDown(){
		Player next = player.moveDown();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerLeft(){
		Player next = player.moveLeft();
		safeMovePlayer(next);
	}
	
	public synchronized void movePlayerRight(){
		Player next = player.moveRight();
		safeMovePlayer(next);
	}

	private void safeMovePlayer(Player next) {
		if(board.canAdd(next)){
			board.addEntity(next);
			board.removeEntity(player);
			eventbus.fire(new EntityMoveEvent(player, next));
			player = next;
		}
	}
}
