package whorten.termgames.glyphs.collate;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Sets;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;

public class GlyphStringCollaterTests {

	private final Glyph base = new Glyph.Builder("X").build();
	
	@Test
	public void collate_oneCoord(){
		GlyphStringCollater sut = new GlyphStringCollater();
		
		Set<GlyphStringCoord> result = sut.collate(base, 
				Sets.newHashSet(new Coord(1,0)));
		
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.iterator().next().getCol()).isEqualTo(1);
		assertThat(result.iterator().next().getRow()).isEqualTo(0);
		assertThat(result.iterator().next().getGlyphString().getBaseString())
			.isEqualTo("X");
	}
	
	@Test
	public void collate_L_Shape(){
		GlyphStringCollater sut = new GlyphStringCollater();
		
		Set<GlyphStringCoord> result = sut.collate(base, 
				Sets.newHashSet(new Coord(1,0), new Coord(2,0), new Coord(3,0), 
						        new Coord(1,1), 
						        new Coord(1,2)));
		
		assertThat(result.size()).isEqualTo(3);
		assertThat(result.stream()
				.map((GlyphStringCoord g) -> new Coord(g.getCol(), g.getRow()))
				.collect(Collectors.toSet())).containsAllOf(
							new Coord(1,0),
							new Coord(1,1),
							new Coord(1,2)
						);
	}
	
	@Test
	public void collate_GlyphStringCoordSet(){
		GlyphStringCollater sut = new GlyphStringCollater();
		GlyphString test = new GlyphString.Builder("X").build();
		
		Set<GlyphStringCoord> result = sut.collate(
				Sets.newHashSet(new GlyphStringCoord(new Coord(1,0), test),
								new GlyphStringCoord(new Coord(2,0), test),
								new GlyphStringCoord(new Coord(3,0), test)));
		
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.iterator().next().getGlyphString().getBaseString())
		.isEqualTo("XXX");
	}
}
