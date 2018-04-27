package whorten.termgames.render;

import java.util.Collection;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;

public interface Renderer {
	void drawAt(int row, int col, Glyph payload);
	void drawAt(int row, int col, GlyphString payload);
	void bufferedDrawAt(int row, int col, Glyph payload);
	void bufferedDrawAt(int row, int col, GlyphString payload);
	void revert(int row, int col);
	void clear(int row, int col);
	void clearBuffer(int row, int col);
	int getCanvasWidth();
	int getCanvasHeight();
	void drawGlyphCollection(Collection<GlyphCoord> glyphCoords);
	void clearScreen();
	void clearRowRange(int row, int col, int length);
	void turnOffCursor();
	void turnOnCursor();
}
