package whorten.termgames.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import whorten.termgames.glyphs.Glyph;

public class ArrayRendererTests {

	@Test
	public void drawAt_MarksWithX() {
		ArrayRenderer sut = new ArrayRenderer(5, 3);
		Glyph g = glyph("#");
		sut.drawAt(2, 1, g);
		sut.drawAt(2, 2, g);
		sut.drawAt(2, 3, g);
		sut.drawAt(2, 4, g);
		sut.drawAt(2, 5, g);

		String expected = "     \n" + 
		                  "XXXXX\n" + 
				          "     \n";
		String result = sut.toString();
		assertEquals(expected, result);
	}

	@Test
	public void clear_NotMarksWithX() {
		ArrayRenderer sut = new ArrayRenderer(5, 3);
		Glyph g = glyph("#");
		sut.drawAt(2, 1, g);
		sut.drawAt(2, 2, g);
		sut.drawAt(2, 3, g);
		sut.drawAt(2, 4, g);
		sut.drawAt(2, 5, g);
		sut.clear(2, 3);

		String expected = "     \n" + 
                          "XX XX\n" + 
		                  "     \n";
		String result = sut.toString();
		assertEquals(expected, result);
	}
	
	@Test
	public void drawAt_perserveCharsTrue_MarksWithHash() {
		ArrayRenderer sut = new ArrayRenderer(5, 3);
		sut.setPreserveChars(true);
		Glyph g = glyph("#");
		sut.drawAt(2, 1, g);
		sut.drawAt(2, 2, g);
		sut.drawAt(2, 3, g);
		sut.drawAt(2, 4, g);
		sut.drawAt(2, 5, g);

		String expected = "     \n" + 
		                  "#####\n" + 
				          "     \n";
		String result = sut.toString();
		assertEquals(expected, result);
	}
	
	@Test
	public void bufferedDraw_revert_backToPreviousCharacter() {
		ArrayRenderer sut = new ArrayRenderer(5, 3);
		sut.setPreserveChars(true);
		Glyph g = glyph("#");
		Glyph h = glyph("A");
		sut.bufferedDrawAt(2, 1, g);
		sut.bufferedDrawAt(2, 2, g);
		sut.bufferedDrawAt(2, 3, g);
		sut.bufferedDrawAt(2, 4, g);
		sut.bufferedDrawAt(2, 5, g);
		sut.bufferedDrawAt(2, 5, h);
		
		String expected = "     \n" + 
                          "####A\n" + 
		                  "     \n";
		String result = sut.toString();
		assertEquals(expected, result);
		
		sut.revert(2, 5);
		
		expected = "     \n" + 
                   "#####\n" + 
		           "     \n";
		result = sut.toString();
		assertEquals(expected, result);
	}

	private Glyph glyph(String base) {
		return new Glyph.Builder(base).build();
	}

}
