package whorten.termgames.glyphs.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;

public class SpecProcessor implements Processor {

	Map<String, Glyph> map = new HashMap<>();
	SpecInterpreter si = new SpecInterpreter();
	Set<GlyphStringCoord> gscs = new HashSet<>();
	
	@Override
	public void withInstruction(String instruction) {
		String key = instruction.substring(0, 1);
		Glyph glyph = si.parse(instruction);
		map.put(key, glyph);
	}

	@Override
	public void apply(Coord coord, String key) {
		Glyph baseGlyph = map.get(key);
		GlyphString gs = new GlyphString.Builder(baseGlyph).build();
		gscs.add(new GlyphStringCoord(coord, gs));
	}

	@Override
	public Set<GlyphStringCoord> process() {
		return new HashSet<>(gscs);
	}

}
