package whorten.termgames.games.quadtris;

import org.junit.Test;

import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.T;
import whorten.termgames.utils.Coord;

public class PieceTests {

	@Test
	public void rotateClockwise(){
		Piece sut = new T(new Coord(0,0));
		
		sut.rotateClockwise();
	}
}
