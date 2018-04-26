package whorten.termgames.games.quadtris.well;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Cell {

	private Coord location;
	private Glyph baseGlyph;
	private Cell(){}
	
	public static List<Cell> fromPiece(Piece piece){
		List<Cell> list = new ArrayList<>();
		for(Coord coord : piece.getCoords()){
			Cell c = new Cell();
			c.location = coord;
			c.baseGlyph = piece.getDefaultCell();
			list.add(c);
		}
		return list;
	}

	public Coord getCoord() {
		return location;
	}
	
	public Glyph getBaseGlyph(){
		return baseGlyph;
	}
}
