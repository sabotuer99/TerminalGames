package whorten.termgames.glyphs.boxdrawing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.CoordLine;
import whorten.termgames.geometry.CoordRect;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCollater;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.utils.BoxDrawingGenerator;

public class BoxDrawingBuilder {

	Set<Coord> coords = new TreeSet<>();
	GlyphString.Builder gBuilder = new GlyphString.Builder("");
	
	public BoxDrawingBuilder withCoord(Coord coord) {
		coords.add(coord);
		return this;
	}

	public Set<GlyphStringCoord> build() {
		Set<GlyphStringCoord> set = new HashSet<>();
		String[] walls = Coord.toAsciiString(coords).split("\n"); 
		String[] boxWalls = new BoxDrawingGenerator().transform(walls);
		Iterator<Coord> iter = coords.iterator();
		for(String row : boxWalls){
			for(String posChar : row.split("")){
				if(" ".equals(posChar)){
					continue;
				} else {
					set.add(new GlyphStringCoord(
								iter.next(),
								gBuilder.withBaseString(posChar).build()
							));
				}
			}
		}
		
		return new GlyphStringCollater().collate(set);
	}

	public BoxDrawingBuilder withLine(Coord origin, Coord end) {
		coords.addAll(new CoordLine(origin, end));
		return this;
	}
	
	public BoxDrawingBuilder withRect(Coord origin, Coord end) {
		coords.addAll(new CoordRect(origin, end));
		return this;
	}

	public BoxDrawingBuilder withBaseGlyph(Glyph baseGlyph) {
		gBuilder = new GlyphString.Builder(baseGlyph);
		return this;
	}
}
