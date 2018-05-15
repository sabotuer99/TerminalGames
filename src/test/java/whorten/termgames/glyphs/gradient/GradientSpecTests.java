package whorten.termgames.glyphs.gradient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import whorten.termgames.glyphs.gradient.GradientSpec;

public class GradientSpecTests {

	@Test
	public void smallGradientGrid_3x3OnlyRowsVary_centerProperlyInfered(){
		GradientSpec sut = new GradientSpec.Builder()
				                           .withStartRGB(254,0,0)
				                           .withEndRGB(0,254,254)
				                           .withCols(3)
				                           .withRows(3)
				                           .build();

		                                                                             
		int[][][] expected = new int[][][]{{{254,  0,  0},{254,  0,  0},{254,  0,  0}},
			                               {{127,127,127},{127,127,127},{127,127,127}},
			                               {{  0,254,254},{  0,254,254},{  0,254,254}}};
		
		
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				int[] shouldBe = expected[row][col];
				int[] result = sut.rgbFor(row, col);
				assertEquals(shouldBe[0], result[0]);
				assertEquals(shouldBe[1], result[1]);
				assertEquals(shouldBe[2], result[2]);
			}
		}
	}
	
	@Test
	public void smallGradientGrid_3x3OnlyColsVary_centerProperlyInfered(){
		GradientSpec sut = new GradientSpec.Builder()
				                           .withStartRGB(254,0,0)
				                           .withEndRGB(0,254,254)
				                           .isHorizontal()
				                           .withCols(3)
				                           .withRows(3)
				                           .build();
		
		int[][][] expected = new int[][][]{{{254,0,0},{127,127,127},{0,254,254}},
			                               {{254,0,0},{127,127,127},{0,254,254}},
			                               {{254,0,0},{127,127,127},{0,254,254}}};
		
		
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				int[] shouldBe = expected[row][col];
				int[] result = sut.rgbFor(row, col);
				assertEquals(shouldBe[0], result[0]);
				assertEquals(shouldBe[1], result[1]);
				assertEquals(shouldBe[2], result[2]);
			}
		}
	}

}
