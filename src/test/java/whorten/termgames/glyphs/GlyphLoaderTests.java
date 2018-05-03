package whorten.termgames.glyphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import whorten.termgames.render.GlyphStringCoord;

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
		
		Set<GlyphStringCoord> result = sut.parse(getBais("TEST\n"
													   + "TEST\n" 		
									                   + "XXXXX"));
		
		assertEquals(1, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("XXXXX", gsc.getGlyphString().getBaseString());
		}
	}
	
	@Test
	public void mappingsProvided_appliesToGlyphStrings(){
		GlyphLoader sut = new GlyphLoader();
		
		Set<GlyphStringCoord> result = sut.parse(getBais("TEST\n"
													   + "X BASE:O\n"
													   + "TEST\n" 		
				                                       + "XXXXX"));
		
		assertEquals(1, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("OOOOO", gsc.getGlyphString().getBaseString());
		}
	}
	
	private InputStream getBais(String string){
		
		return new ByteArrayInputStream(string.getBytes());
	}
}
