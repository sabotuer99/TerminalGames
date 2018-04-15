package whorten.termgames.render;

import whorten.termgames.glyphs.Glyph;

public class GlyphCoord {

	private int row;
	private int col;
	private Glyph glyph;
	
	public GlyphCoord(int row, int col, Glyph glyph){
		this.row = row;
		this.col = col;
		this.glyph = glyph;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public Glyph getGlyph(){
		return glyph;
	}
}
