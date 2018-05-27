package whorten.termgames.games.tableflipper.board.npc;


import java.util.LinkedList;

import whorten.termgames.entity.EntityBoard;
import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.utils.graphs.AStar;
import whorten.termgames.utils.graphs.GraphSearch;

public class NPCAgent {

	private NPCAgent(){}
	
	private EntityBoard eb;
	private NPC npc;
	private int timeLastTick;
	private int speed;
	private GraphSearch gs;
	private Coord destination;
	private LinkedList<Direction> path;
	
    
	// on each call to tick, check if the time elapsed since the last
	// tick is more than the npc's speed. If yes, try to move next direction in path
	// if path is blocked, recalculate path. If path is empty, check if table needs 
	// unflipped, then calculate new path to random location
	public void tick(long time){
		if(timeLastTick + speed <= time){
			if(canMove(path.pop())){
				
			}
		}
	}
	
	public boolean goToCoord(Coord coord){
		this.destination = coord;
		return true;
	}
	
	private boolean canMove(Direction direction) {
		NPC next = npc.move(direction);
		return eb.canMove(npc, next);
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
		EntityBoard eb;
		NPC npc;
		GraphSearch gs = new AStar();
		int speed;
		
		public Builder(EntityBoard eb, NPC npc){
			this.eb = eb;
			this.npc = npc;
		}
		
		public NPCAgent build(){
			NPCAgent n = new NPCAgent();
			n.eb = this.eb;
			n.npc = this.npc;
			n.gs = this.gs;
			n.speed = this.speed;
			return n;
		}
	}

}
