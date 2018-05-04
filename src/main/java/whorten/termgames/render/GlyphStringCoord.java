package whorten.termgames.render;

import whorten.termgames.glyphs.GlyphString;

public class GlyphStringCoord {
	private int row;
	private int col;
	private GlyphString glyphString;
	
	public GlyphStringCoord(int row, int col, GlyphString glyphString){
		this.row = row;
		this.col = col;
		this.glyphString = glyphString;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public GlyphString getGlyphString(){
		return glyphString;
	}
	
	@Override
	public int hashCode() {
		return (row << 16 + col) ^ glyphString.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GlyphStringCoord){
			GlyphStringCoord o = (GlyphStringCoord) obj;
			return o.row == this.row &&
				   o.col == this.col &&
				   o.glyphString.equals(this.glyphString);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("GlyphStringCoord: row:[%d], col:[%d], GlyphString:[%s]", row, col, glyphString);
	}
}
