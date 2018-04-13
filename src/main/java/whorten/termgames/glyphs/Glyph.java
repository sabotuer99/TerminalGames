package whorten.termgames.glyphs;

import whorten.termgames.utils.Colors;

public class Glyph {

	private Glyph(){}
	private String base;
	private boolean bold;
	private boolean underline;
	private String fg = "";
	private String bg = "";
	public String postfg = "";
	public String postbg = "";
	public boolean resetbold;
	public boolean resetunderline;
	

	@Override
	public String toString() {
		String composite = "";
		composite += bold ? Style.BOLD.toAnsi() : "";
		composite += underline ? Style.UNDERLINE.toAnsi() : "";
		composite += fg;
		composite += bg;
		composite += base;
		composite += postfg;
		composite += postbg;
		composite += resetbold ? Style.RESET_BOLD.toAnsi() : "";
		composite += resetunderline ? Style.RESET_UNDERLINE.toAnsi() : "";
		return composite;
	}
	
	public static class Builder{
		private String base;
		private boolean bold;
		private boolean underline;
		private String fg = "";
		private String bg = "";
		private String postbg = BgColor.RESET_BG.toAnsi();
		private String postfg = FgColor.RESET_FG.toAnsi();
		private boolean resetbold = true;
		private boolean resetunderline = true;

		public Builder(String base){
			if(base.length() != 1){
				throw new IllegalArgumentException("Glyph can represent only a single character");
			}
			this.base = base;	
		}
		
		public Glyph build(){
			Glyph glyph = new Glyph();
			glyph.base = this.base;
			glyph.bold = this.bold;
			glyph.underline = this.underline;
			glyph.fg = this.fg;
			glyph.bg = this.bg;
			glyph.postfg = this.postfg;
			glyph.postbg = this.postbg;
			glyph.resetbold = this.resetbold;
			glyph.resetunderline = this.resetunderline;
			return glyph;
		}
		
		public Builder isBold(boolean bold){
			this.bold = bold;
			return this;
		}
		
		public Builder isUnderline(boolean underline){
			this.underline = underline;
			return this;
		}
		
		public Builder withForegroundColor(int red, int green, int blue){
			this.fg = Colors.foreground(red, green, blue);
			return this;
		}
		
		public Builder withForegroundColor(FgColor color){
			this.fg = color.toAnsi();
			return this;
		}
		
		public Builder withBackgroundColor(int red, int green, int blue){
			this.bg = Colors.background(red, green, blue);
			return this;
		}
		
		public Builder withBackgroundColor(BgColor color){
			this.bg = color.toAnsi();
			return this;
		}
		
		public Builder withFgAfter(int red, int green, int blue){
			this.postfg = Colors.foreground(red, green, blue);
			return this;
		}
		
		public Builder withFgAfter(FgColor color){
			this.postfg = color.toAnsi();
			return this;
		}
		
		public Builder withBgAfter(int red, int green, int blue){
			this.postbg = Colors.background(red, green, blue);
			return this;
		}
		
		public Builder withBgAfter(BgColor color){
			this.postbg = color.toAnsi();
			return this;
		}
		
		public Builder resetBoldAfter(boolean resetbold){
			this.resetbold = resetbold;
			return this;
		}
		
		public Builder resetUnderlineAfter(boolean resetunderline){
			this.resetunderline = resetunderline;
			return this;
		}
		
	}
}
