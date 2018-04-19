package whorten.termgames.render;

import static whorten.termgames.utils.StringUtils.repeat;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.utils.TerminalNavigator;

public class OutputStreamRenderer implements Renderer {

	private PrintStream out;
	private TerminalNavigator nav;
	private int width;
	private int height;
	private ScreenBuffer screenBuffer; 

	public OutputStreamRenderer(PrintStream out, int width, int height){
		this.out = out;
		this.nav = new TerminalNavigator(out);
		this.width = width;
		this.height = height;
		this.screenBuffer = new ScreenBuffer(height, width);
	}
	
	@Override
	public void drawAt(int row, int col, Glyph payload) {
		nav.positionCursor(row, col);
		out.print(payload);
		nav.cursorBack();
		
	}
	
	@Override
	public void drawAt(int row, int col, GlyphString payload) {
		nav.positionCursor(row, col);
		out.print(payload);		
	}
	
	@Override
	public void bufferedDrawAt(int row, int col, Glyph payload) {
		screenBuffer.bufferAt(row, col, payload);
		drawAt(row, col, payload);
	}

	@Override
	public void bufferedDrawAt(int row, int col, GlyphString payload) {
		List<Glyph> glyphs = payload.getGlyphs();
		for(int i = 0; i < glyphs.size() && i + col <= width; i++){
			screenBuffer.bufferAt(row, col + i, glyphs.get(i));
		}
		drawAt(row, col, payload);
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

	@Override
	public void clearScreen() {
		String line = repeat(" ", width);
		for(int row = 1; row <= height; row++){
			nav.positionCursor(row, 0);
			out.print(line);			
		}	
		screenBuffer.clearAll();
	}

	@Override
	public void revert(int row, int col) {
		Glyph old = screenBuffer.revertAt(row, col);
		nav.positionCursor(row, col);
		out.print(old);
	}

	@Override
	public void clear(int row, int col) {
		clearBuffer(row, col);
		nav.positionCursor(row, col);
		out.print(" ");
	}

	@Override
	public void clearBuffer(int row, int col) {
		screenBuffer.clearAt(row, col);
	}

	@Override
	public void turnOffCursor() {
		nav.cursorHide();
	}

	@Override
	public void turnOnCursor() {
		nav.cursorShow();
	}



}
