package whorten.termgames.games.tableflipper.board.player;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.entity.Entity;
import whorten.termgames.geometry.Coord;

public class PlayerTests {

	@Test
	public void newInstance_createsValidPlayer(){
		Entity sut = Player.newInstance(new Coord(0,0));
		
		Set<Coord> coords = sut.getCoords();
		
		assertThat(coords).isNotEmpty();
		assertThat(coords.size()).isEqualTo(7);
	}
	
	@Test
	public void movePlayer_newPlayerInstanceValid(){
		Entity sut = Player.newInstance(new Coord(0,0));
		
		Entity moved = sut.moveDown(1);
		Set<Coord> coords = moved.getCoords();
		
		assertThat(coords).isNotEmpty();
		assertThat(coords.size()).isEqualTo(7);
	}
	
	@Test
	public void stand_newPlayerInstanceValid(){
		Player sut = Player.newInstance(new Coord(0,0));
		
		Entity moved = sut.stand();
		Set<Coord> coords = moved.getCoords();
		
		assertThat(coords).isNotEmpty();
		assertThat(coords.size()).isEqualTo(7);
	}
	
	@Test
	public void moveThenstand_newPlayerInstanceValid(){
		Player sut = Player.newInstance(new Coord(0,0));
		
		Entity moved = sut.moveDown(1).stand();
		Set<Coord> coords = moved.getCoords();
		
		assertThat(coords).isNotEmpty();
		assertThat(coords.size()).isEqualTo(7);
	}
}
