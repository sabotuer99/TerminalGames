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
	
	@Override
	public int hashCode() {
		return (row << 16 + col) ^ glyph.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GlyphCoord){
			GlyphCoord o = (GlyphCoord) obj;
			return o.row == this.row &&
				   o.col == this.col &&
				   o.glyph.equals(this.glyph);
		}
		return false;
	}
}
