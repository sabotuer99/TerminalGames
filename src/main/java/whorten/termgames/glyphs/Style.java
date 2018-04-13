package whorten.termgames.glyphs;

public enum Style {
	RESET_BOLD("\u001B[21m"),
	BOLD("\u001B[1m"),
	RESET_UNDERLINE("\u001B[24m"),
	UNDERLINE("\u001B[4m");
	
	private String ansi;

	Style(String ansi){
		this.ansi = ansi;
	}
	
	public String toAnsi(){
		return ansi;
	}
}
