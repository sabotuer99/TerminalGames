package whorten.termgames.games.tableflipper.board;

import org.junit.Test;
import static com.google.common.truth.Truth.*;
import whorten.termgames.entity.EntityState;
import whorten.termgames.games.tableflipper.board.player.PlayerState;
import whorten.termgames.games.tableflipper.board.player.PlayerStrings;
import whorten.termgames.geometry.Coord;

public class PlayerStateTests {
	
	@Test
	public void moveUp_returnsUpState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveUp();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_UP);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(0,-1));
	}
	
	@Test
	public void moveLeft_returnsLeftState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveLeft();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_LEFT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(-1,0));
	}
	
	
	@Test
	public void moveRight_returnsRightState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveRight();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_RIGHT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(1,0));
	}
	
	
	@Test
	public void moveDown_returnsDownState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveDown();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_DOWN);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(0,1));
	}
	
	@Test
	public void moveRightFlip_returnsFlipRightState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveRight().flipNothing();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.THROW_RIGHT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(1,0));
	}
}
