package whorten.termgames.games.tableflipper.board.npc;


import whorten.termgames.entity.EntityBoard;

public class NPCAgent {

	public EntityBoard eb;
	public NPC npc;

	
	
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
