package whorten.termgames.games.tableflipper.renderer;

import java.util.Set;

import whorten.termgames.GameConsole;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class TableFlipperRenderer {

	private static final String TABLEFLIPPER_BOARD_FILE = "";
	private GameConsole console;

	public TableFlipperRenderer(GameConsole console){
		this.console = console;
	}
	
	public void drawBoard(){
		Set<GlyphStringCoord> g = console.loadFromFile(TABLEFLIPPER_BOARD_FILE);
		console.getRenderer().drawGlyphStringCollection(g);
	}
	
}
