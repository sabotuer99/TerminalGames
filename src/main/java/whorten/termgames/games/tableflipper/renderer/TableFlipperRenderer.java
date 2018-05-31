package whorten.termgames.games.tableflipper.renderer;

import java.util.Set;

import whorten.termgames.GameConsole;
import whorten.termgames.entity.Entity;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class TableFlipperRenderer {

	private static final String TABLEFLIPPER_BOARD_FILE = 
			"gspecs/tableflipper_background.gstxt";
	private GameConsole console;

	public TableFlipperRenderer(GameConsole console){
		this.console = console;
	}
	
	public void drawBoard(){
		Set<GlyphStringCoord> g = console.loadFromFile(TABLEFLIPPER_BOARD_FILE);
		console.getRenderer().drawGlyphStringCollection(g);
	}
	
	public synchronized void drawEntity(Entity e){
		GlyphStringCoord gsc = e.getGlyphStringCoord().offset(new Coord(2,2));
		console.getRenderer().drawAt(gsc.getRow(), gsc.getCol(), gsc.getGlyphString());
	}

	public synchronized void clearEntity(Entity e) {
		GlyphStringCoord g = e.getGlyphStringCoord().offset(new Coord(2,2));
		console.getRenderer().clearRowRange(g.getRow(), g.getCol(), 
				g.getGlyphString().getGlyphs().size());
	}

	public synchronized void moveEntity(Entity from, Entity to) {
		GlyphStringCoord draw = to.getGlyphStringCoord().offset(new Coord(2,2));
		GlyphStringCoord clear = from.getGlyphStringCoord().offset(new Coord(2,2));
		console.getRenderer().clearRowRange(clear.getRow(), clear.getCol(), 
				clear.getGlyphString().getBaseString().length());
		console.getRenderer().drawAt(draw.getRow(), draw.getCol(), draw.getGlyphString());
	}
	
}
