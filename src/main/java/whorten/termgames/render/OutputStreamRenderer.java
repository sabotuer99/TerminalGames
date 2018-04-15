package whorten.termgames.render;

import java.io.PrintStream;
import java.util.Collection;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.TerminalNavigator;

public class OutputStreamRenderer implements Renderer {

	private PrintStream out;
	private TerminalNavigator nav;
	private int width;
	private int height;

	public OutputStreamRenderer(PrintStream out, int width, int height){
		this.out = out;
		this.nav = new TerminalNavigator(out);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawAt(int row, int col, Glyph payload) {
		nav.positionCursor(row, col);
		out.print(payload);
		nav.cursorBack();
	}

	@Override
	public int getCanvasWidth() {
		return width;
	}

	@Override
	public int getCanvasHeight() {
		return height;
	}

	@Override
	public void drawGlyphCollection(Collection<GlyphCoord> glyphCoords) {
		for(GlyphCoord gc : glyphCoords){
			drawAt(gc.getRow(), gc.getCol(), gc.getGlyph());
		}
	}

}
