package whorten.termgames.glyphs.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class GlyphLoaderTests {

	@Test
	public void sanity(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais(""));
		
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	public void noMappingsProvided_returnsUndecoratedText(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais("APPLY:\n"		
									                   + "XXXXX"));
		GlyphStringCoord gsc = result.iterator().next();
		assertEquals(1, result.size());
		assertEquals("XXXXX", gsc.getGlyphString().getBaseString());
		assertEquals(0, gsc.getCol());
		assertEquals(0, gsc.getRow());
	}
	
	@Test
	public void noMappingsProvided_hasSpaces_returnsUndecoratedTextAtLocation(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais("APPLY:\n"	
													   + "    \n"												   
									                   + "  XXX"));
		GlyphStringCoord gsc = result.iterator().next();
		assertEquals(1, result.size());
		assertEquals("XXX", gsc.getGlyphString().getBaseString());
		assertEquals(2, gsc.getCol());
		assertEquals(1, gsc.getRow());
	}
	
	@Test
	public void mappingsProvided_appliesToGlyphStrings(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais("SPEC:X BASE:O\n"
													   + "APPLY:\n" 		
				                                       + "XXXXX"));
		
		assertEquals(1, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("OOOOO", gsc.getGlyphString().getBaseString());
		}
	}
	
	@Test
	public void mappingsProvided_includesDEFAULT_appliesToUnmappedChars(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais("SPEC:DEFAULT FG:CYAN\n"
													   + "APPLY:\n" 		
				                                       + "XXXXX"));
		
		assertEquals(1, result.size());
		Glyph base = new Glyph.Builder("X").withForegroundColor(FgColor.CYAN).build();
		GlyphString expected = new GlyphString.Appender()
                .append(base).append(base).append(base)
                .append(base).append(base).build();
		
		assertEquals(expected, result.iterator().next().getGlyphString());
	}
	
	private InputStream getBais(String string){
		
		return new ByteArrayInputStream(string.getBytes());
	}
}
