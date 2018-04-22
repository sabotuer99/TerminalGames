package whorten.termgames.glyphs;

public enum Symbol {
	SOUND_ON("🔊"), SOUND_OFF("🔇"), MUSIC_NOTES("♫");
	
	private String rep;

	Symbol(String rep){
		this.rep = rep;
	}
	
	public String getSymbol(){
		return rep;
	}
}
