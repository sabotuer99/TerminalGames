package whorten.termgames.games.quadtris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceFactory;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class PieceTests {

	@Test
	public void getCoords_AtOrigin(){	
		Piece sut = getSut();
		
		Set<Coord> result = sut.getCoords();
		
		assertTrue(result.contains(new Coord(1,0)));
		assertTrue(result.contains(new Coord(0,0)));
		assertTrue(result.contains(new Coord(0,1)));
		assertTrue(result.contains(new Coord(-1,0)));
	}
	
	@Test
	public void getCoords_AtCol4Row5(){	
		Piece sut = getSut(new Coord(4,5));
		
		Set<Coord> result = sut.getCoords();
		
		assertTrue(result.contains(new Coord(5,5)));
		assertTrue(result.contains(new Coord(4,5)));
		assertTrue(result.contains(new Coord(4,6)));
		assertTrue(result.contains(new Coord(3,5)));
	}
	
	@Test
	public void rotateClockwise_returnsAppropriateCoords(){	
		Piece sut = getSut();
		
		Set<Coord> result = sut.rotateClockwise().getCoords();
		
		assertTrue(result.contains(new Coord(0,1)));
		assertTrue(result.contains(new Coord(0,0)));
		assertTrue(result.contains(new Coord(-1,0)));
		assertTrue(result.contains(new Coord(0,-1)));
	}
	
	@Test
	public void toAsciiString_doesTheRightThing(){
		Piece sut = getSut();
		
		String ascii = sut.toAsciiString();
		System.out.println(ascii);
		
		assertEquals("###\n # ", ascii);
	}
	
	@Test
	public void random_pieces(){
		
		for(int i = 0; i < 10; i++){
			System.out.println(PieceFactory.getRandomPiece(new Coord(0,0)).toAsciiString());
			System.out.println("\n\n");
		}

		assertTrue(true);
	}
	
	private Piece getSut(Coord baseCoord){
		return new Piece.Builder(getSut())
				.withBaseCoord(baseCoord)
				.build();
	}
	
	private Piece getSut(){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(1,0));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(-1,0));
		Coord baseCoord = new Coord(0,0);
		Glyph glyph = new Glyph.Builder("X").build();
		
		return new Piece.Builder(baseCoord)
				.withOffsets(coords)
				.withDefaultGlyph(glyph)
				.build();
	}
}
