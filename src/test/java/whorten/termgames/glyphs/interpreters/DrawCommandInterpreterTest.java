package whorten.termgames.glyphs.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.render.GlyphStringCoord;

public class DrawCommandInterpreterTest {

	@Test
	public void sanity(){
		DrawCommandInterpreter sut = new DrawCommandInterpreter();
		
		Set<GlyphStringCoord> result = sut.parse("");
		
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	public void parse_LINEspec_HorizontalLineWithDefaultSpec(){
		DrawCommandInterpreter sut = new DrawCommandInterpreter();
		sut.setDefaultGlyph(new Glyph.Builder("X").build());
		
		Set<GlyphStringCoord> result = sut.parse("LINE ORIGIN:0,0 END:5,0");
		
		assertEquals("XXXXX", result.iterator().next().getGlyphString().getBaseString());
	}
	
	@Test
	public void parse_LINEspec_DiagonalLineWithDefaultSpec(){
		DrawCommandInterpreter sut = new DrawCommandInterpreter();
		sut.setDefaultGlyph(new Glyph.Builder("X").build());
		
		Set<GlyphStringCoord> result = sut.parse("LINE ORIGIN:0,0 END:5,5");
		
		assertEquals(5, result.size());
	}
}
