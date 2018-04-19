package whorten.termgames.games.snake;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Fruit {

	private Coord coord;
	private Glyph glyph;

	public Fruit(Coord coord, Glyph glyph){
		this.coord = coord;
		this.glyph = glyph;
	}
	
	public Coord getCoord(){
		return coord;
	}
	
	public Glyph getGlyph(){
		return glyph;
	}
}
