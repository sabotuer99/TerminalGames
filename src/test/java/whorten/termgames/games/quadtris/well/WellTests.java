package whorten.termgames.games.quadtris.well;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.events.FullRowsEvent;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class WellTests {

	private EventBus eventBus = new EventBus();

	@Test
	public void toString_emptyWell(){
		Well sut = getSut();
		
		String result = sut.toString();
		String expected = 
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"+----------+";
		
		assertEquals(expected, result);
	}
	
	@Test
	public void toString_wellWithPiece_pieceShown(){
		Well sut = getSut();
		Piece testPiece = new Piece.Builder(new Coord(4,19))
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(-1,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,0))
				.build();
		sut.addPiece(testPiece);
		
		String result = sut.toString();
		String expected = 
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|    X     |\n" +
				"|   XXX    |\n" +
				"+----------+";
		
		assertEquals(expected, result);
	}
	
	@Test
	public void toString_wellWithMultiplePiece_piecesShown(){
		Well sut = getSut();
		Piece testPiece = new Piece.Builder(new Coord(4,2))
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(-1,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,0))
				.build();
		sut.addPiece(testPiece);
		sut.addPiece(testPiece);
		sut.addPiece(testPiece);
		sut.addPiece(testPiece);
		
		String result = sut.toString();
		String expected = 
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|          |\n" +
				"|    X     |\n" +
				"|   XXX    |\n" +
				"|    X     |\n" +
				"|   XXX    |\n" +
				"|    X     |\n" +
				"|   XXX    |\n" +
				"|    X     |\n" +
				"|   XXX    |\n" +
				"+----------+";
		//System.out.println(expected);
		//System.out.println(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void addPiece_spaceBelowPiece_pieceDroppedToBottom(){
		Well sut = getSut();
		Piece testPiece = new Piece.Builder(new Coord(4,5))
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(-1,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,0))
				.build();
		sut.addPiece(testPiece);

		//System.out.println(sut.toString());
		
		assertTrue(sut.isOccupied(new Coord(4,19))); //base Coordinate at bottom
		assertTrue(sut.isOccupied(new Coord(3,19))); 
		assertTrue(sut.isOccupied(new Coord(5,19))); 
		assertTrue(sut.isOccupied(new Coord(4,18))); 
		assertFalse(sut.isOccupied(new Coord(4,5))); //base Coordinate at drop location
		assertFalse(sut.isOccupied(new Coord(3,5))); 
		assertFalse(sut.isOccupied(new Coord(5,5))); 
		assertFalse(sut.isOccupied(new Coord(4,4))); 
	}
	
	@Test
	public void addPiece_createsLine_lineCleared(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(0,5)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();
		for(int i = 0; i < 10; i += 2){
			Piece p = new Piece.Builder(base)
					.withBaseCoord(new Coord(i,5))
					.build();
			sut.addPiece(p);
			System.out.println(sut.toString());
		}
				
		assertFalse(sut.isOccupied(new Coord(4,19))); //base Coordinate at bottom
		assertFalse(sut.isOccupied(new Coord(3,19))); 
		assertFalse(sut.isOccupied(new Coord(5,19))); 
		assertFalse(sut.isOccupied(new Coord(4,18))); 
	}
	
	@Test
	public void addPiece_createsLine_firesEventWithRows(){
		EventBus eb = new EventBus();
		Well sut = new Well(eb);
		FullRowsEvent[] events = new FullRowsEvent[1];
		eb.subscribe(FullRowsEvent.class, (FullRowsEvent fre) -> events[0] = fre);
		
		Piece base = new Piece.Builder(new Coord(0,5)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();
		for(int i = 0; i < 10; i += 2){
			Piece p = new Piece.Builder(base)
					.withBaseCoord(new Coord(i,5))
					.build();
			sut.addPiece(p);
		}
				
		FullRowsEvent event = events[0];
		assertNotNull(event);
		assertNotNull(event.getRows());
		assertEquals(2, event.getRows().size());
		assertTrue(event.getRows().contains(19));
		assertTrue(event.getRows().contains(18));
	}
	
	@Test
	public void addPiece_createsLine_linesAboveFall(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(0,5)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();
		sut.addPiece(base);
		for(int i = 0; i < 10; i += 2){
			Piece p = new Piece.Builder(base)
					.withBaseCoord(new Coord(i,5))
					.build();
			sut.addPiece(p);
			//System.out.println(sut.toString());
		}
				
		System.out.println(sut.toString());
		assertEquals("Too many cells!", 4, sut.getCells().size());
		assertTrue(sut.isOccupied(new Coord(0,19))); //base Coordinate at bottom
		assertTrue(sut.isOccupied(new Coord(0,18))); 
		assertTrue(sut.isOccupied(new Coord(1,19))); 
		assertTrue(sut.isOccupied(new Coord(1,18))); 
		assertFalse(sut.isOccupied(new Coord(2,19))); //next piece over
		assertFalse(sut.isOccupied(new Coord(2,18))); 
		assertFalse(sut.isOccupied(new Coord(3,19))); 
		assertFalse(sut.isOccupied(new Coord(3,18))); 
	}
	
	@Test
	public void isOccupied_pieceAboveWell_returnsFalse(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(5,-2)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();	

		boolean isOccupied = sut.isOccupied(base);
				
		assertFalse(isOccupied);
	}
	
	@Test
	public void isLegal_pieceAboveWell_returnsFalse(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(5,-2)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();	

		boolean isLegal = sut.isLegal(base);
				
		assertFalse(isLegal);
	}
	
	@Test
	public void applyGravity_emptyWell_returnedPieceIsAtBottom(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(-2,5)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();	

		Set<Coord> result = sut.applyGravity(getOPiece(new Coord(5,0)))
								.getCoords();

		assertTrue(result.contains(new Coord(6,18)));
		assertTrue(result.contains(new Coord(5,18)));
		assertTrue(result.contains(new Coord(5,19)));
		assertTrue(result.contains(new Coord(4,18)));
	}
	

	private Well getSut() {
		return new Well(eventBus);
	}
	
	private Piece getOPiece(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(1,0));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(-1,0));
		Glyph glyph = new Glyph.Builder("X").build();
		
		return new Piece.Builder(baseCoord)
				.withOffsets(coords)
				.withDefaultGlyph(glyph)
				.build();
	}
}
