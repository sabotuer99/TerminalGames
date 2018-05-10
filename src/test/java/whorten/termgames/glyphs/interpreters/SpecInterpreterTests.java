package whorten.termgames.glyphs.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;

public class SpecInterpreterTests {

	@Test
	public void parse_emptySpec_returnsDefaultGlyph(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("");
		
		assertEquals(new Glyph.Builder(" ").build(), result);
	}
	
	@Test
	public void parse_specOnlyHasChar_returnsDefaultGlyphWithBaseChar(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	
	@Test
	public void parse_BASEspec_returnsGlyphWithBaseReset(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BASE:O");
		
		assertEquals(new Glyph.Builder("O").build(), result);
	}
	
	@Test
	public void parse_BASEspec_InvalidValue_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BASE:OOOO");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_BASEspec_EmptyValue_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BASE:");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_FGspec_returnsGlyphWithFgColorSet(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X FG:255,75,150");
		
		assertEquals(new Glyph.Builder("X").withForegroundColor(255, 75, 150).build(), result);
	}
	
	@Test
	public void parse_FGspec_TooManyValues_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X FG:255,75,150,65");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_FGspec_TooFewValues_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X FG:255,75");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_FGspec_Empty_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X FG:");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_BGspec_returnsGlyphWithBgColorSet(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BG:255,75,150");
		
		assertEquals(new Glyph.Builder("X").withBackgroundColor(255, 75, 150).build(), result);
	}
	
	@Test
	public void parse_BGspec_TooManyValues_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BG:255,75,150,65");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_BGspec_TooFewValues_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BG:255,75");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_BGspec_Empty_Ignores(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BG:");
		
		assertEquals(new Glyph.Builder("X").build(), result);
	}
	
	@Test
	public void parse_BGspec_BgColorName_returnsGlyphWithBgColorSet(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BG:CYAN");
		
		assertEquals(new Glyph.Builder("X").withBackgroundColor(BgColor.CYAN).build(), result);
	}
	
	@Test
	public void parse_BOLDspec_returnsGlyphWithBold(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X BOLD");
		
		assertEquals(new Glyph.Builder("X").isBold(true).build(), result);
	}
	
	@Test
	public void parse_UNDERLINEspec_returnsGlyphWithUnderline(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("X UNDERLINE");
		
		assertEquals(new Glyph.Builder("X").isUnderline(true).build(), result);
	}
	
	@Test
	public void parse_DEFAULTspec_returnsSpaceGlyphWithDefaultStyle(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("DEFAULT");
		
		assertEquals(new Glyph.Builder(" ").build(), result);
	}
	
	@Test
	public void parse_DEFAULTspec_HasFG_returnsSpaceGlyphWithFGSet(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("DEFAULT FG:CYAN");
		
		assertEquals(new Glyph.Builder(" ").withForegroundColor(FgColor.CYAN).build(), result);
	}
	
	@Test
	public void parse_DEFAULTspec_HasFGandBg_returnsSpaceGlyphWithFGandBGSet(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parse("DEFAULT FG:0,0,255 BG:255,0,0");
		
		assertEquals(new Glyph.Builder(" ").withForegroundColor(0,0,255).withBackgroundColor(255,0,0).build(), result);
	}
	
	@Test
	public void parse_GLYPHspec_returnsGLYPH(){
		SpecInterpreter sut = new SpecInterpreter();
		
		Glyph result = sut.parseGlyphSpec("GLYPH:[DEFAULT FG:LIGHT_GREEN BG:0,0,255]", null);
		
		assertNotNull(result);
		assertEquals(new Glyph.Builder(" ")
				.withForegroundColor(FgColor.LIGHT_GREEN)
				.withBackgroundColor(0,0,255).build(), result);
	}
}
