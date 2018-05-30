package whorten.termgames.games.tableflipper.board.player;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.entity.EntityState;
import whorten.termgames.games.tableflipper.board.player.PlayerState;
import whorten.termgames.games.tableflipper.board.player.PlayerStrings;
import whorten.termgames.geometry.Coord;

public class PlayerStateTests {
	
	@Test
	public void moveUp_returnsUpState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveUp(1);
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_UP);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(0,-1));
	}
	
	@Test
	public void moveLeft_returnsLeftState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveLeft(2);
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_LEFT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(-2,0));
	}
	
	
	@Test
	public void moveRight_returnsRightState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveRight(2);
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_RIGHT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(2,0));
	}
	
	
	@Test
	public void moveDown_returnsDownState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveDown(1);
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.WALK_DOWN);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(0,1));
	}
	
	@Test
	public void moveRightFlip_returnsFlipRightState(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		EntityState<?> moved = sut.moveRight(2).flipNothing();
		
		assertThat(moved.getBaseString()).isEqualTo(PlayerStrings.THROW_RIGHT);
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(2,0));
	}
	
	@Test
	public void getCoords_returnsValidSet(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		Set<Coord> coords = sut.getCoords();
		
		assertThat(coords).isNotEmpty();
		assertThat(coords.size()).isEqualTo(7);
	}
	
	@Test
	public void stand_idempotent(){		
		PlayerState sut = PlayerState.getStartState(new Coord(0,0));
		
		PlayerState stand1 = sut.stand();
		PlayerState stand2 = stand1.stand();
		
		assertThat(stand1).isEqualTo(stand2);
	}
}
