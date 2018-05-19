package whorten.termgames.games.tableflipper.board.npc;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;

public class NPC extends AbstractEntity<NPC, NPCState, NPC.Builder> {

	@Override
	public Builder toBuilder() {
		return null;
	}

	public class Builder implements EntityBuilder<NPC, NPCState, Builder> {

		@Override
		public Builder withState(NPCState state) {
			return null;
		}

		@Override
		public NPC build() {
			return null;
		}

	}

}
