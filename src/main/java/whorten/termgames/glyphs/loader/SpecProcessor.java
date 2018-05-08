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
	
	public SpecProcessor(){
		map.put("DEFAULT", new Glyph.Builder(" ").build());
	}
	
	@Override
	public void withInstruction(String instruction) {
		if(instruction == null || instruction.length() == 0) return;
		String key = getKey(instruction);
		Glyph glyph = si.parse(instruction);
		map.put(key, glyph);
	}

	@Override
	public void apply(Coord coord, String key) {
		Glyph baseGlyph = getBaseGlyph(key);
		GlyphString gs = new GlyphString.Builder(baseGlyph).build();
		gscs.add(new GlyphStringCoord(coord, gs));
	}

	@Override
	public Set<GlyphStringCoord> process() {
		return new HashSet<>(gscs);
	}

	@Override
	public int applicability(Coord coord, String key) {
		return map.containsKey(key) ? 9 : 1;
	}
	
	private String getKey(String instruction) {
		int index = instruction.indexOf(" ");
		String key = instruction;
		if(index != -1){
			key = instruction.substring(0, index);
		}
		return key;
	}
	
	private Glyph getBaseGlyph(String key) {
		Glyph baseGlyph = map.get(key);
		if(baseGlyph == null){
			baseGlyph = getDefaultGlyph(key);
		}
		return baseGlyph;
	}	

	private Glyph getDefaultGlyph(String key) {
		Glyph baseGlyph;
		Glyph d = map.get("DEFAULT");
		String base = d.getBase() == " " && key.length() == 1 ? key : d.getBase();
		baseGlyph = new Glyph.Builder(d)
				             .withBase(base)
				             .build();
		return baseGlyph;
	}

}
