package whorten.termgames.glyphs.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.render.Renderer;
import whorten.termgames.testing.ArrayRenderer;

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
		
		assertEquals("XXXXXX", result.iterator().next().getGlyphString().getBaseString());
	}
	
	@Test
	public void parse_LINEspec_DiagonalLineWithDefaultSpec(){
		DrawCommandInterpreter sut = new DrawCommandInterpreter();
		sut.setDefaultGlyph(new Glyph.Builder("X").build());
		
		Set<GlyphStringCoord> result = sut.parse("LINE ORIGIN:0,0 END:5,5");
		
		assertEquals(6, result.size());
	}
	
	@Test
	public void parse_BOXspec_DrawsABox(){
		DrawCommandInterpreter sut = new DrawCommandInterpreter();
		sut.setDefaultGlyph(new Glyph.Builder("X").build());
		
		Set<GlyphStringCoord> result = sut.parse("BOX ORIGIN:1,1 END:6,6");
		
		
		String output = drawCollection(result, 6, 6);
		String expectedOutput = "XXXXXX\n"
				              + "X    X\n"
				              + "X    X\n"
				              + "X    X\n"
				              + "X    X\n"
				              + "XXXXXX\n";
		assertEquals(10, result.size());
		assertEquals(expectedOutput, output);
	}
	
	private String drawCollection(Set<GlyphStringCoord> gsc, int height, int width){
		Renderer renderer = new ArrayRenderer(height, width);
		renderer.drawGlyphStringCollection(gsc);
		return renderer.toString();
	}
}
