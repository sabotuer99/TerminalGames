package whorten.termgames.games.tableflipper.board.player;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;
import whorten.termgames.geometry.Coord;

public class Player extends AbstractEntity<Player,PlayerState,Player.Builder> {

	private Player(){}	
	
	private PlayerState state;
	
	public static Player newInstance(Coord baseCoord){
		Player p = new Player();
		p.state = PlayerState.getStartState(baseCoord);
		return p;
	}
	
	public Player flip(boolean left, boolean right){
		Builder b = this.toBuilder();
		if(left && right){
			return b.withState(state.doubleFlip()).build();
		} else if(left){
			return b.withState(state.flipLeft()).build();
		} else if(right){
			return b.withState(state.flipRight()).build();
		} else {
			return b.withState(state.flipNothing()).build();			
		}
	}
	
	public Player stand() {
		return this.toBuilder().withState(state.stand()).build();
	}
	
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

	@Override
	public PlayerState getState() {
		return state;
	}

}