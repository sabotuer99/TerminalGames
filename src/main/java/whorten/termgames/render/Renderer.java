package whorten.termgames.render;

import java.util.Collection;

import whorten.termgames.glyphs.Glyph;

public interface Renderer {
	void drawAt(int row, int col, Glyph payload);
	int getCanvasWidth();
	int getCanvasHeight();
	void drawGlyphCollection(Collection<GlyphCoord> glyphCoords);
	void clearScreen();
}
