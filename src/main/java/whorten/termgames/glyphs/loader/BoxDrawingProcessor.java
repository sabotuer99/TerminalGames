package whorten.termgames.glyphs.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.boxdrawing.BoxDrawingBuilder;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;

public class BoxDrawingProcessor implements Processor{

	private SpecInterpreter si = new SpecInterpreter();
	private Map<String, BoxDrawingBuilder> map = new HashMap<>();

	@Override
	public void withInstruction(String instruction) {
		String key = instruction.substring(0, 1);
		Glyph glyph = si.parseGlyphSpec(instruction, new Glyph.Builder(" ").build());
		map.put(key, new BoxDrawingBuilder().withBaseGlyph(glyph));		
	}

	@Override
	public void apply(Coord coord, String key) {
		BoxDrawingBuilder bdb = map.get(key);
		bdb.withCoord(coord);
	}

	@Override
	public Set<GlyphStringCoord> process() {
		Set<GlyphStringCoord> gscs = new HashSet<>();
		for(BoxDrawingBuilder bdb : map.values()){
			gscs.addAll(bdb.build());
		}
		return gscs;
	}

	@Override
	public int applicability(Coord coord, String key) {
		return map.containsKey(key) ? 10 : 0;
	}

	
	
}
