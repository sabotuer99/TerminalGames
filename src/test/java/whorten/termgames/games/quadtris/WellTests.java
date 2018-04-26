package whorten.termgames.games.quadtris;

import static org.junit.Assert.*;

import org.junit.Test;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.well.Well;
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
		System.out.println(expected);
		System.out.println(result);
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

		System.out.println(sut.toString());
		
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
	public void isOccupied_pieceAboveWell_returnsFalse(){
		Well sut = getSut();
		Piece base = new Piece.Builder(new Coord(-2,5)) //square
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
		Piece base = new Piece.Builder(new Coord(-2,5)) //square
				.addOffset(new Coord(0,0))
				.addOffset(new Coord(0,-1))
				.addOffset(new Coord(1,-1))
				.addOffset(new Coord(1,0))
				.build();	

		boolean isLegal = sut.isLegal(base);
				
		assertFalse(isLegal);
	}
	

	private Well getSut() {
		return new Well(eventBus);
	}
}
