package whorten.termgames.testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.OutputStreamRenderer;

public class ArrayRenderer extends OutputStreamRenderer {

	private String[][] screen;
	private int width;
	private int height;
	private boolean preserveChars = false;
	public ArrayRenderer(int width, int height){
		super(new PrintStream(new ByteArrayOutputStream()), width, height);
		//assume 1 based indexing
		screen = new String[height + 1][width + 1];
		this.height = height;
		this.width = width;
	}
	
	public void setPreserveChars(boolean preserveChars){
		this.preserveChars = preserveChars;
	}
	
	@Override
	public void drawAt(int row, int col, Glyph payload) {
		screen[row][col] = preserveChars ? payload.getBase() : "X";
	}

	@Override
	public void drawAt(int row, int col, GlyphString payload) {
		for(int i = 0; i < payload.getBaseString().length(); i++){
			screen[row][col+i] = preserveChars ? Character.toString(payload.getBaseString().charAt(i)) : "X";
		}
	}
	
	@Override
	public synchronized void clear(int row, int col) {
		super.clear(row, col);
		screen[row][col] = null;
	}
	
	@Override
	public void clearRowRange(int row, int col, int length) {
		super.clearRowRange(row, col, length);
		for(int i = 0; i < length; i++){
			screen[row][col + i] = null;
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int row = 1; row <= height; row++){
			for(int col = 1; col <= width; col++){
				sb.append(screen[row][col] == null ? " " : screen[row][col]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
