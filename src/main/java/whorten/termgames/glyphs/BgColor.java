package whorten.termgames.glyphs;

public enum BgColor {
	RESET_FG("\u001B[49m"),
	BLACK("\u001B[40m"),
	RED("\u001B[41m"),
	GREEN("\u001B[42m"),
	YELLOW("\u001B[43m"),
	BLUE("\u001B[44m"),
	MAGENTA("\u001B[45m"),
	CYAN("\u001B[46m"),
	LIGHT_GRAY("\u001B[47m"),
	DARK_GRAY("\u001B[100m"),
	LIGHT_RED("\u001B[101m"),
	LIGHT_GREEN("\u001B[102m"),
	LIGHT_YELLOW("\u001B[103m"),
	LIGHT_BLUE("\u001B[104m"),
	LIGHT_MAGENTA("\u001B[105m"),
	LIGHT_CYAN("\u001B[106m"),
	WHITE("\u001B[107m");
	
	private String ansi;

	BgColor(String ansi){
		this.ansi = ansi;
	}
	
	public String toAnsi(){
		return ansi;
	}
}
