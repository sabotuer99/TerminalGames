package whorten.termgames.glyphs.loader;

import static org.junit.Assert.assertEquals;
import static com.google.common.truth.Truth.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class BoxDrawingProcessorTests {

	@Test
	public void mappingsProvided_appliesToGlyphStrings(){
		Processor sut = new BoxDrawingProcessor();
		
		sut.withInstruction("X FG:255,75,150");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "X");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(1, result.size());
		for(GlyphStringCoord gsc : result){
			assertEquals("═════", gsc.getGlyphString().getBaseString());
		}
	}
	
	@Test
	public void DefineTwoKeys_GetIndependentDrawings(){
		Processor sut = new BoxDrawingProcessor();
		
		sut.withInstruction("X FG:255,75,150");
		sut.withInstruction("O FG:255,75,150");
		sut.apply(new Coord(0,0), "X");
		sut.apply(new Coord(1,0), "X");
		sut.apply(new Coord(2,0), "O");
		sut.apply(new Coord(3,0), "X");
		sut.apply(new Coord(4,0), "X");
		
		Set<GlyphStringCoord> result = sut.process();
		
		assertEquals(3, result.size());
		assertThat(result.stream()
				         .map(g -> g.getGlyphString().getBaseString())
				         .collect(Collectors.toSet()))
		.containsAllOf("══", "▫");
	}
}
