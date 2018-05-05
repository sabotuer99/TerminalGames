package whorten.termgames.geometry;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class CoordLineTests {

	@Test
	public void horizontalLine_allCoordsHaveSameRow(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(5, 0));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(1, 0),
					new Coord(2, 0),
					new Coord(3, 0),
					new Coord(4, 0),
					new Coord(5, 0)
				);
	}
	
	@Test
	public void verticalLine_allCoordsHaveSameCol(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(0, 5));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(0, 1),
					new Coord(0, 2),
					new Coord(0, 3),
					new Coord(0, 4),
					new Coord(0, 5)
				);
	}
	
	@Test
	public void diagonalLine(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(5, 5));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(1, 1),
					new Coord(2, 2),
					new Coord(3, 3),
					new Coord(4, 4),
					new Coord(5, 5)
				);
	}
	
	@Test
	public void steepSlopeLine(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(1,5));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(0, 1),
					new Coord(0, 2),
					new Coord(1, 3),
					new Coord(1, 4),
					new Coord(1, 5)
				);
	}
	
	@Test
	public void shallowSlopeLine(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(5,1));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(1, 0),
					new Coord(2, 0),
					new Coord(3, 1),
					new Coord(4, 1),
					new Coord(5, 1)
				);
	}
	
	@Test
	public void coordsReversed_sameLine(){
		
		CoordLine sut1 = new CoordLine(new Coord(0,0), new Coord(5,1));
		CoordLine sut2 = new CoordLine(new Coord(5,1), new Coord(0,0));
		
		System.out.println(Coord.toAsciiString(sut1));
		System.out.println(Coord.toAsciiString(sut2));
		
		assertThat(sut1).containsAllIn(sut2);
	}
	
	@Test
	public void negativeDirection_StillWorks(){
		
		CoordLine sut = new CoordLine(new Coord(0,0), new Coord(-5, 0));
		
		System.out.println(Coord.toAsciiString(sut));
		
		assertThat(sut.size()).isEqualTo(6);
		assertThat(sut).containsAllOf(
					new Coord(0, 0), 
					new Coord(-1, 0),
					new Coord(-2, 0),
					new Coord(-3, 0),
					new Coord(-4, 0),
					new Coord(-5, 0)
				);
	}
}
