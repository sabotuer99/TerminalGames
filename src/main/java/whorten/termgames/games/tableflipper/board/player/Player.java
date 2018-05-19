package whorten.termgames.games.tableflipper.board.player;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;
import whorten.termgames.geometry.Coord;

public class Player extends AbstractEntity<Player,PlayerState,Player.Builder> {

	private Player(){}	
	
	@Override
	public Builder toBuilder() {
		return new Builder(this);
	}
	
	public static class Builder implements EntityBuilder<Player, PlayerState, Builder>{
		PlayerState state = PlayerState.getStartState(new Coord(0,0));

		public Builder(Player player){
			this.state = player.state;
		}	
		
		@Override
		public Player build() {
			Player p = new Player();
			p.state = state;
			return p;
		}

		@Override
		public Builder withState(PlayerState state) {
			this.state = state;
			return this;
		}
		
	}
}