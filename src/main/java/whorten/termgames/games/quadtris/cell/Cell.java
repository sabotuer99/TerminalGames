package whorten.termgames.games.quadtris.cell;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

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

	public Cell moveDown(int blocks){
		Cell c = new Cell();
		c.location = new Coord(location.getCol(), location.getRow() + blocks);
		c.baseGlyph = this.baseGlyph;
		return c;
	}
	
	public Coord getCoord() {
		return location;
	}
	
	public Glyph getBaseGlyph(){
		return baseGlyph;
	}
	
	public static final Cell EMPTY = new EmptyCell();
	private static class EmptyCell extends Cell{
		@Override
		public Cell moveDown(int blocks) {
			return this;
		}
	}
	
	public static class Builder{
		private Coord location = new Coord(0, 0);
		private Glyph baseGlyph = new Glyph.Builder(" ").build();
		
		public Builder withBaseGlyph(Glyph baseGlyph){
			this.baseGlyph = baseGlyph;
			return this;
		}
		
		public Builder withLocation(Coord location){
			this.location = location;
			return this;
		}
		
		public Cell build(){
			Cell c = new Cell();
			c.baseGlyph = this.baseGlyph;
			c.location = this.location;
			return c;
		}
		
	}
}
