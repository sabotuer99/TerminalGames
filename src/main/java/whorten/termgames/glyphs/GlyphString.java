package whorten.termgames.glyphs;

import java.util.ArrayList;
import java.util.List;

public class GlyphString {
	
	private List<Glyph> glyphs = new ArrayList<>();
	public String stringRep;

	private GlyphString(){};

	public List<Glyph> getGlyphs(){
		return new ArrayList<>(glyphs);
	}
	
	@Override
	public String toString(){
		return stringRep;
	}
	
	public GlyphString append(GlyphString next){
		glyphs.addAll(next.getGlyphs());
		stringRep += next.toString();
		return this;
	}
	
	public static class Builder{
		
		private String base;
		private Glyph.Builder baseGlyphBuilder = new Glyph.Builder(" ");	
		
		public Builder(String base){
			this.base = base;
		}
		
		public Builder(Glyph baseGlyph){
			this.base = baseGlyph.getBase();
			this.baseGlyphBuilder = new Glyph.Builder(baseGlyph);
		}
		
		public GlyphString build(){
			
			GlyphString gs = new GlyphString();
			
			char[] rowChars = base.toCharArray();
			for(int i = 0; i < base.length(); i++){
				String baseChar = Character.toString(rowChars[i]);
				Glyph glyph = baseGlyphBuilder.withBase(baseChar).build();
				gs.glyphs.add(glyph);
			}
			
			
			StringBuilder sb = new StringBuilder();
			for(Glyph g : gs.glyphs){
				sb.append(g.toString());
			}
			gs.stringRep = sb.toString();
			
			return gs;
		}
		
		public Builder withBaseString(String base){
			this.base = base;
			return this;
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
