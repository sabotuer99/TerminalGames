package whorten.termgames.games.snake;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Fruit {

	private Coord coord;
	private Glyph glyph;
	private boolean isGood;

	public Fruit(Coord coord, Glyph glyph, boolean isGood){
		this.coord = coord;
		this.glyph = glyph;
		this.isGood = isGood;
	}
	
	public Coord getCoord(){
		return coord;
	}
	
	public Glyph getGlyph(){
		return glyph;
	}
	
	public boolean isGood(){
		return isGood;
	}
	
	@Override
	public String toString() {
		return String.format("Fruit: coord: [%s], glyph: [%s], isGood: [%b]", coord, glyph, isGood);
	}
}
