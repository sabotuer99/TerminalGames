package whorten.termgames.glyphs.collate;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.GlyphString;

public class GlyphStringCoord implements Comparable<GlyphStringCoord>{
	private Coord coord;
	private GlyphString glyphString;
	
	public GlyphStringCoord(int row, int col, GlyphString glyphString){
		this.coord = new Coord(col, row);
		this.glyphString = glyphString;
	}
	
	public GlyphStringCoord(Coord coord, GlyphString glyphString){
		this.coord = coord;
		this.glyphString = glyphString;
	}
	
	public int getRow(){
		return coord.getRow();
	}
	
	public int getCol(){
		return coord.getCol();
	}
	
	public GlyphString getGlyphString(){
		return glyphString;
	}
	
	@Override
	public int hashCode() {
		return coord.hashCode() ^ glyphString.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GlyphStringCoord){
			GlyphStringCoord o = (GlyphStringCoord) obj;
			return o.coord.equals(this.coord) &&
				   o.glyphString.equals(this.glyphString);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("GlyphStringCoord: Coord: [%s], GlyphString:[%s]", coord, glyphString);
	}

	@Override
	public int compareTo(GlyphStringCoord o) {
		return coord.compareTo(o.coord);
	}

	public int length() {
		return glyphString.length();
	}

	public GlyphStringCoord append(GlyphStringCoord here) {
		return new GlyphStringCoord(coord, glyphString.append(here.glyphString));
	}
}
