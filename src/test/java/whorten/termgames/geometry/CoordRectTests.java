package whorten.termgames.geometry;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class CoordRectTests {

	@Test
	public void new3x3Rect(){
		CoordRect sut = new CoordRect(new Coord(0,0),new Coord(2,2));
		
		assertThat(sut.size()).isEqualTo(8);
		assertThat(sut).containsAllOf(
				new Coord(0,0),
				new Coord(1,0),
				new Coord(2,0),
				new Coord(0,1),
				new Coord(2,1),
				new Coord(0,2),
				new Coord(1,2),
				new Coord(2,2)
				);
	}
}
