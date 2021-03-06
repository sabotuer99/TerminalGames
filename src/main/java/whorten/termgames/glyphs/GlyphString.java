package whorten.termgames.glyphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class GlyphString {
	
	private List<Glyph> glyphs = new ArrayList<>();
	public String stringRep;
	private String baseString;

	private GlyphString(){};

	public List<Glyph> getGlyphs(){
		return new ArrayList<>(glyphs);
	}
	
	//TODO Make this immutable
	public GlyphString append(GlyphString next){
		GlyphString appended = new GlyphString();
		appended.glyphs.addAll(glyphs);
		appended.glyphs.addAll(next.glyphs);
		appended.stringRep = stringRep + next.toString();
		appended.baseString = baseString + next.baseString;
		return appended;
	}
	
	//TODO Make this immutable
	public GlyphString prepend(Glyph first){
		GlyphString prepended = new GlyphString();
		prepended.glyphs.addAll(glyphs);
		prepended.glyphs.add(0, first);
		prepended.stringRep = first.toString() + stringRep;
		prepended.baseString = first.getBase() + baseString;
		return prepended;
	}
	

	public String getBaseString() {
		return baseString;
	}
	
	public int length(){
		return baseString.length();
	}
	
	@Override
	public String toString(){
		return stringRep;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GlyphString){
			GlyphString o = (GlyphString) obj;
			return o.toString().equals(this.toString());
		}
		return false;
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
			gs.baseString = this.base;
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

		public Builder withFgColor(int red, int green, int blue) {
			baseGlyphBuilder.withForegroundColor(red, green, blue);
			return this;
		}
		
		public Builder withBgColor(int red, int green, int blue) {
			baseGlyphBuilder.withBackgroundColor(red, green, blue);
			return this;
		}

		public Builder isBold(boolean bold) {
			baseGlyphBuilder.isBold(bold);
			return this;
		}

		public Builder clearBgColor() {
			baseGlyphBuilder.clearBgColor();
			return this;
		}
		
		public int length(){
			return base.length();
		}
	}
	
	public static class Appender{
		
		private List<Glyph> aglyphs = new ArrayList<>();

		public Appender append(Glyph glyph){
			aglyphs .add(glyph);
			return this;
		}
		
		public GlyphString build(){
			GlyphString gs = new GlyphString();
			gs.glyphs = new ArrayList<>(this.aglyphs);
			
			StringBuilder sb = new StringBuilder();
			StringBuilder base = new StringBuilder();
			for(Glyph g : gs.glyphs){
				sb.append(g.toString());
				base.append(g.getBase());
			}
			gs.stringRep = sb.toString();
			gs.baseString = base.toString();
			return gs;
		}

		public int length() {
			return aglyphs.size();
		}
	}

	public static Set<GlyphStringCoord> offSetCollection(Set<GlyphStringCoord> glyphs, Coord offset) {
		return glyphs.stream()
				.map(gsc -> new GlyphStringCoord(Coord.add(gsc.getCoord(), offset), gsc.getGlyphString()))
				.collect(Collectors.toSet());
	}

}
