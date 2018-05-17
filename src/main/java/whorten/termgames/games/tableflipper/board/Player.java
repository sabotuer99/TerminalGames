package whorten.termgames.games.tableflipper.board;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;
import whorten.termgames.geometry.Coord;

public class Player extends AbstractEntity<Player> {

	PlayerState pstate = PlayerState.getStartState(new Coord(0,0));
	public Player(){
		super.state = pstate;
	}
	
	
	@Override
	public EntityBuilder toBuilder(AbstractEntity<Player> abstractEntity) {
		return null;
	}

}