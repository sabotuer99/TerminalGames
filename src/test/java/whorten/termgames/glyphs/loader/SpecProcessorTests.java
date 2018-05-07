package whorten.termgames.glyphs.loader;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class SpecProcessorTests {

	@Test
	public void mappingsProvided_appliesToGlyphStrings(){
		Processor sut = new SpecProcessor();
		
		sut.withInstruction("X BASE:O");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "X");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(5, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("O", gsc.getGlyphString().getBaseString());
		}
	}
}
