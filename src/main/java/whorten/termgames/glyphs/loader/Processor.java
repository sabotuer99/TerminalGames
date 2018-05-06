package whorten.termgames.glyphs.loader;

import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public interface Processor {
	void withInstruction(String instruction);
	void apply(Coord coord, String key);
	Set<GlyphStringCoord> process();
}
