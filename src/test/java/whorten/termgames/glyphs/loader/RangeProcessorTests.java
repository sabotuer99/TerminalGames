package whorten.termgames.glyphs.loader;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class RangeProcessorTests {

	@Test
	public void newRangeProcessor_withGlyph_anyCoordMatches(){
		String instruction = "GLYPH:[DEFAULT FG:CYAN]";
		Processor sut = new RangeProcessor();
		sut.withInstruction(instruction);
		sut.apply(new Coord(0,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		Glyph base = new Glyph.Builder("X").withForegroundColor(FgColor.CYAN).build();
		GlyphString expected = new GlyphString.Appender()
				.append(base).build();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(expected, result.iterator().next().getGlyphString());
		
	}
}
