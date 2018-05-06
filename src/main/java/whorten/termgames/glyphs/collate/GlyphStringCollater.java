package whorten.termgames.glyphs.collate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;

public class GlyphStringCollater {

	public Set<GlyphStringCoord> collate(Glyph base, Set<Coord> coordSet) {
		List<Coord> coords = new ArrayList<>(coordSet);
		Collections.sort(coords);
		Set<GlyphStringCoord> glyphs = new HashSet<>();
		
		GlyphString.Appender b = new GlyphString.Appender();
		//if two coords are adjacent in the row, append to same 
		//GlyphString, otherwise reset dump appender and start new
		Coord next = coords.get(0);
		for(int i = 0; i < coords.size(); i++){
			if(i == 0){
				b.append(base);
			} else {
				Coord last = coords.get(i - 1);
				Coord here = coords.get(i);
				//same row and adjacent columns, append
				if(last.getRow() == here.getRow() &&
				   Math.abs(last.getCol() - here.getCol()) == 1){
					//same row, start coord "next" doesn't change
					//just append another base glyph
					b.append(base);
				} else {
					//need to start a new GlyphString and add the 
					//current Appender buffer to a new GSC
					glyphs.add(new GlyphStringCoord(next,b.build()));
					b = new GlyphString.Appender();
					b.append(base);
					next = here;
				}
			}
		}
		if(b.length() > 0){
			glyphs.add(new GlyphStringCoord(next,b.build()));
		}
				
		return glyphs;
	}
	
	
	public Set<GlyphStringCoord> collate(Set<GlyphStringCoord> input) {
		Set<GlyphStringCoord> sortedInput = new TreeSet<>(input);
		Set<GlyphStringCoord> collated = new HashSet<>();
		Iterator<GlyphStringCoord> iter = sortedInput.iterator();

		GlyphStringCoord last = iter.next();
		while(iter.hasNext()){
			GlyphStringCoord here = iter.next();
			//same row and adjacent columns, append
			if(last.getRow() == here.getRow() &&
			   Math.abs(last.getCol() - here.getCol()) == last.length()){
				last = last.append(here);
			} else {
				collated.add(last);	
				last = here;
			}
		}
		collated.add(last);		
		
		return collated;
	}
}
