package whorten.termgames.glyphs;

public enum FgColor {
	RESET_FG("\u001B[39m"),
	BLACK("\u001B[30m"),
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	MAGENTA("\u001B[35m"),
	CYAN("\u001B[36m"),
	LIGHT_GRAY("\u001B[37m"),
	DARK_GRAY("\u001B[90m"),
	LIGHT_RED("\u001B[91m"),
	LIGHT_GREEN("\u001B[92m"),
	LIGHT_YELLOW("\u001B[93m"),
	LIGHT_BLUE("\u001B[94m"),
	LIGHT_MAGENTA("\u001B[95m"),
	LIGHT_CYAN("\u001B[96m"),
	WHITE("\u001B[97m");
	
	private String ansi;

	FgColor(String ansi){
		this.ansi = ansi;
	}
	
	public String toAnsi(){
		return ansi;
	}
}
