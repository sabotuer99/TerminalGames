package whorten.termgames.games.tableflipper.board.wall;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;

public class Wall  extends AbstractEntity<Wall, WallState, Wall.Builder> {


	@Override
	public Builder toBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class Builder implements EntityBuilder<Wall, WallState, Builder>{
		
		@Override
		public Builder withState(WallState state) {
			return null;
		}
		
		@Override
		public Wall build() {
			return null;
		}
		
	}

}
