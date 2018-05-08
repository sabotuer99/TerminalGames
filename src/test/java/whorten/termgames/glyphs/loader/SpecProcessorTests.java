package whorten.termgames.glyphs.loader;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class SpecProcessorTests {

	@Test
	public void instructionsProvided_appliesToGlyphStrings(){
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
	
	@Test
	public void defaultProvided_appliesToUnmappedKeys(){
		Processor sut = new SpecProcessor();
		
		sut.withInstruction("DEFAULT BASE:O");
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
	
	@Test
	public void emptyDefaultProvided_appliesDefaultToUnmappedKeys(){
		Processor sut = new SpecProcessor();
		
		sut.withInstruction("DEFAULT");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "X");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(5, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("X", gsc.getGlyphString().getBaseString());
		}
	}
	
	@Test
	public void noInstructionsProvided_appliesDefaultToGlyphStrings(){
		Processor sut = new SpecProcessor();
		
		sut.withInstruction("X");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "X");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(5, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("X", gsc.getGlyphString().getBaseString());
		}
	}
	
	
	@Test
	public void emptySpecProvided_appliesDefaultToGlyphStrings(){
		Processor sut = new SpecProcessor();
		
		sut.withInstruction("");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "X");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(5, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("X", gsc.getGlyphString().getBaseString());
		}
	}
}
