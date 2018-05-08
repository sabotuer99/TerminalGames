package whorten.termgames.glyphs.loader;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class NoOpProcessor implements Processor{

	@Override
	public void withInstruction(String instruction) {
		
	}

	@Override
	public void apply(Coord coord, String key) {
		
	}

	@Override
	public int applicability(Coord coord, String key) {
		return 0;
	}

	@Override
	public Set<GlyphStringCoord> process() {
		return new HashSet<>();
	}

	private static NoOpProcessor instance = new NoOpProcessor();
	public static Processor getInstance() {
		return instance;
	}

}
