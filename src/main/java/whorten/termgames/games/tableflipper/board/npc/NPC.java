package whorten.termgames.games.tableflipper.board.npc;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;
import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class NPC extends AbstractEntity<NPC, NPCState, NPC.Builder> {

	private NPC(){}
	
	NPCState state;
	
	public static NPC newInstance(Coord baseCoord){
		NPC n = new NPC();
		n.state = NPCState.getStartState(baseCoord);
		return n;
	}
	
	public NPC unflip(Direction side){
		Builder b = this.toBuilder();
		if(side == Direction.LEFT){
			return b.withState(state.unflipLeft()).build();
		} else if (side == Direction.RIGHT) {
			return b.withState(state.unflipRight()).build();
		}
		return this;
	}
	
	public NPC stand() {
		return this.toBuilder().withState(state.stand()).build();
	}
	
	public Coord getLocation(){
		return state.getBaseCoord();
	}
	
	@Override
	public Builder toBuilder() {
		return new Builder(this);
	}

	public class Builder implements EntityBuilder<NPC, NPCState, Builder> {

		NPCState state;
		
		Builder(NPC npc){
			this.state = npc.state;
		}
		
		@Override
		public Builder withState(NPCState state) {
			this.state = state;
			return this;
		}

		@Override
		public NPC build() {
			NPC n = new NPC();
			n.state = this.state;
			return n;
		}

	}

	@Override
	public NPCState getState() {
		return state;
	}

}
