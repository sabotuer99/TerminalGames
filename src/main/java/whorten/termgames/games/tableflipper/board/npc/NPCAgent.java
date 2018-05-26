package whorten.termgames.games.tableflipper.board.npc;


import whorten.termgames.entity.EntityBoard;

public class NPCAgent {

	private NPCAgent(){}
	
	private EntityBoard eb;
	private NPC npc;
	private int timeLastTick;
    
	// on each call to tick, check if the time elapsed since the last
	// tick is more than the npc's speed. If yes, try to move next direction in path
	// if path is blocked, recalculate path. If path is empty, check if table needs 
	// unflipped, then calculate new path to random location
	public void tick(long time){
		
	}
	
	
	public static class Builder{
		EntityBoard eb;
		NPC npc;
		
		public Builder(EntityBoard eb, NPC npc){
			this.eb = eb;
			this.npc = npc;
		}
		
		public NPCAgent build(){
			NPCAgent n = new NPCAgent();
			n.eb = this.eb;
			n.npc = this.npc;
			return n;
		}
	}

}
