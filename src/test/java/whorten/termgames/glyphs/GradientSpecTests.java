package whorten.termgames.glyphs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import whorten.termgames.glyphs.gradient.GradientSpec;

public class GradientSpecTests {

	@Test
	public void smallGradientGrid_3x3_centerProperlyInferedwww(){
		GradientSpec sut = new GradientSpec.Builder()
				                           .withStartRGB(254,0,0)
				                           .withColEndRGB(0,254,0)
				                           .withRowEndRGB(0,0,254)
				                           .withCols(3)
				                           .withRows(3)
				                           .build();
		int[] result = sut.rgbFor(1, 1);
		//int[] expected = new int[]{127,127,127};
		assertEquals(127, result[0]);
		assertEquals(127, result[1]);
		assertEquals(127, result[2]);
	}
}
