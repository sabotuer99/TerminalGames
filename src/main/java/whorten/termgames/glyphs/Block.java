package whorten.termgames.glyphs;

public enum Block {
	
	//FULL BLOCK
	FULL("█"),
	//HALF BLOCKS
	TOP_HALF("▀"),
	BOTTOM_HALF("▄"),	
	LEFT_HALF("▌"),
	RIGHT_HALF("▐"),
	//ONE QUARTER BLOCKS
	QT_BOTTOM_LEFT("▖"),
	QT_BOTTOM_RIGHT("▗"),
	QT_TOP_LEFT("▘"),
	QY_TOP_RIGHT("▝"),
	//THREE QUARTER BLOCKS
	QTX3_BOTTOM_TOP_LEFT("▙"),
	QTX3_BOTTOM_TOP_RIGHT("▟"),
	QTX3_TOP_BOTTOM_LEFT("▛"),
	QTX3_TOP_BOTTOM_RIGHT("▜"),
	//DIAGONALS
	CC_TOP_LEFT_BOTTOM_RIGHT("▚"),
	CC_TOP_RIGHT_BOTTOM_LEFT("▞");
	
	private String rep;

	Block(String rep){
		this.rep = rep;
	}
	
	@Override
	public String toString() {
		return rep;
	}

}
