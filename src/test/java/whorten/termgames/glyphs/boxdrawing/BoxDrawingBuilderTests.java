package whorten.termgames.glyphs.boxdrawing;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class BoxDrawingBuilderTests {

	@Test
	public void AddSingleCoord_GetOneGlyphStringBack(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		
		Set<GlyphStringCoord> result = bdb.withCoord(new Coord(0,0)).build();
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		String base = result.iterator().next().getGlyphString().getBaseString();
		assertThat(base).isEqualTo("▫");
	}
	
	@Test
	public void setBaseGlyph_SetsGlyphStringStyle(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		bdb.withCoord(new Coord(0,0));
		bdb.withBaseGlyph(new Glyph.Builder("X").withForegroundColor(FgColor.CYAN).build());
		
		Set<GlyphStringCoord> result = bdb.build();

		GlyphString gs = result.iterator().next().getGlyphString();
		GlyphString expected = new GlyphString.Builder("▫").withFgColor(FgColor.CYAN).build();
		assertThat(gs).isEqualTo(expected);
	}
	
	@Test
	public void AddLineOfCoords_GetOneGlyphStringBack(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		bdb.withCoord(new Coord(0,0));
		bdb.withCoord(new Coord(1,0));
		bdb.withCoord(new Coord(2,0));
		Set<GlyphStringCoord> result = bdb.build();
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		String base = result.iterator().next().getGlyphString().getBaseString();
		assertThat(base).isEqualTo("═══");
	}
	
	@Test
	public void AddLine_GetOneGlyphStringBack(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		bdb.withLine(new Coord(0,0), new Coord(2,0));
		Set<GlyphStringCoord> result = bdb.build();
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		String base = result.iterator().next().getGlyphString().getBaseString();
		assertThat(base).isEqualTo("═══");
	}
	
	@Test
	public void AddRect_GetMultipleGlyphStringBack(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		bdb.withRect(new Coord(0,0), new Coord(2,2));
		Set<GlyphStringCoord> result = bdb.build();
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(4);
		assertThat(result.stream()
				.map((GlyphStringCoord gsc) -> { return gsc.getGlyphString().getBaseString(); })
				.collect(Collectors.toSet()))
		.containsAllOf("║", "╚═╝", "╔═╗");
	}
	
	@Test
	public void AddRectAndLine_GetCorrectGlyphStringBack(){
		BoxDrawingBuilder bdb = new BoxDrawingBuilder();
		bdb.withRect(new Coord(0,0), new Coord(4,2));
		bdb.withLine(new Coord(2,0), new Coord(2,2));
		Set<GlyphStringCoord> result = bdb.build();
		
		assertThat(result).isNotNull();
		assertThat(result).hasSize(5);
		assertThat(result.stream()
				.map((GlyphStringCoord gsc) -> { return gsc.getGlyphString().getBaseString(); })
				.collect(Collectors.toSet()))
		.containsAllOf("║", "╚═╩═╝", "╔═╦═╗");
	}
}
