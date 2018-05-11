package whorten.termgames.glyphs.loader;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class BoxDrawingProcessorTests {

	@Test
	public void mappingsProvided_appliesToGlyphStrings(){
		Processor sut = new BoxDrawingProcessor();
		
		sut.withInstruction("X GLYPH:[DEFAULT FG:255,75,150]");
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
		
		sut.withInstruction("X GLYPH:[DEFAULT FG:255,75,150]");
		sut.withInstruction("O GLYPH:[DEFAULT FG:255,75,150]");
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
		Glyph expectedGlyph = new Glyph.Builder(" ")
				.withForegroundColor(255, 75, 150).build();
		assertThat(result.stream()
				.map(g -> g.getGlyphString().getGlyphs())
				.flatMap(l -> l.stream())
				.collect(Collectors.toSet())).contains(expectedGlyph );
	
	}
}
