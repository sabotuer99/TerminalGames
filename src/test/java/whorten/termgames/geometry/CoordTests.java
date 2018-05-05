package whorten.termgames.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CoordTests {

	@Test
	public void comparable_sortsCorrectly(){
		List<Coord> sut = new ArrayList<>();
		
		sut.add(new Coord(9,9));
		sut.add(new Coord(0,0));
		sut.add(new Coord(5,9));
		sut.add(new Coord(9,1));
		
		Collections.sort(sut);
		
		assertEquals(new Coord(0,0), sut.get(0));
		assertEquals(new Coord(9,1), sut.get(1));
		assertEquals(new Coord(5,9), sut.get(2));
		assertEquals(new Coord(9,9), sut.get(3));
	}
}
