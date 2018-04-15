package whorten.termgames.glyphs;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.render.GlyphCoord;
import whorten.termgames.render.GameBorder.Builder;

public class GlyphString {
	
	private GlyphString(){};
	
	List<GlyphCoord> string = new ArrayList<>();
	
	public List<GlyphCoord> getGlyphCoords(){
		return new ArrayList<>(string);
	}
	
	public static class Builder{
		
		private int row;
		private int col;
		private String base;
		private Glyph.Builder baseGlyphBuilder = new Glyph.Builder(" ");		
		
		public Builder(int row, int col, String base){
			this.row = row;
			this.col = col;
			this.base = base;
		}
		
		public GlyphString build(){
			
			GlyphString gs = new GlyphString();
			
			char[] rowChars = base.toCharArray();
			for(int i = 0; i < base.length(); i++){
				String baseChar = Character.toString(rowChars[i]);
				Glyph glyph = baseGlyphBuilder.withBase(baseChar).build();
				GlyphCoord glyphCoord = new GlyphCoord(row, col + i, glyph);
				gs.string.add(glyphCoord);
			}
			
			return gs;
		}
		
		public Builder withFgColor(FgColor color){
			baseGlyphBuilder.withForegroundColor(color);
			return this;
		}
		
		public Builder withBgColor(BgColor color){
			baseGlyphBuilder.withBackgroundColor(color);
			return this;
		}

	}
}
