package whorten.termgames.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class RangeTests {

	@Test
	public void newRange_allCoordsValid(){
		Range sut = new Range.Builder().build();
		
		assertTrue(sut.inRange(new Coord(0,0)));
		assertTrue(sut.inRange(new Coord(Integer.MAX_VALUE, Integer.MAX_VALUE)));
		assertTrue(sut.inRange(new Coord(Integer.MIN_VALUE, Integer.MIN_VALUE)));
		
	}
	
	@Test
	public void newRange_FromCoords(){
		Range sut = new Range.Builder()
				.betweenCoords(new Coord(5,5), new Coord(10,10))
				.build();
		
		assertFalse(sut.inRange(new Coord(0,0)));
		assertTrue(sut.inRange(new Coord(6,6)));
	}
}
