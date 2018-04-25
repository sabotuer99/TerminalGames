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
	public synchronized void drawAt(int row, int col, Glyph payload) {
		nav.positionCursor(row, col);
		out.print(payload);
		nav.cursorBack();
		
	}
	
	@Override
	public synchronized void drawAt(int row, int col, GlyphString payload) {
		nav.positionCursor(row, col);
		out.print(payload);		
	}
	
	@Override
	public synchronized void bufferedDrawAt(int row, int col, Glyph payload) {
		screenBuffer.bufferAt(row, col, payload);
		drawAt(row, col, payload);
	}

	@Override
	public synchronized void bufferedDrawAt(int row, int col, GlyphString payload) {
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
	public synchronized void drawGlyphCollection(Collection<GlyphCoord> glyphCoords) {
		for(GlyphCoord gc : glyphCoords){
			drawAt(gc.getRow(), gc.getCol(), gc.getGlyph());
		}
	}

	@Override
	public synchronized void clearScreen() {
		String line = repeat(" ", width);
		for(int row = 1; row <= height; row++){
			nav.positionCursor(row, 0);
			out.print(line);			
		}	
		screenBuffer.clearAll();
	}

	@Override
	public synchronized void revert(int row, int col) {
		Glyph old = screenBuffer.revertAt(row, col);
		nav.positionCursor(row, col);
		out.print(old);
	}

	@Override
	public synchronized void clear(int row, int col) {
		clearBuffer(row, col);
		nav.positionCursor(row, col);
		out.print(" ");
	}

	@Override
	public synchronized void clearBuffer(int row, int col) {
		screenBuffer.clearAt(row, col);
	}

	@Override
	public synchronized void turnOffCursor() {
		nav.cursorHide();
	}

	@Override
	public synchronized void turnOnCursor() {
		nav.cursorShow();
	}



}
