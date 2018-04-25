package whorten.termgames.animation;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GlyphCoord;
import whorten.termgames.render.GlyphStringCoord;
import whorten.termgames.render.Renderer;

public class GlyphFrame implements Frame {

	Set<GlyphCoord> cells;
	Set<GlyphStringCoord> lines;
	long frameLength;
	
	private GlyphFrame(){}
	
	@Override
	public void drawFrame(Renderer renderer) {
		for(GlyphStringCoord gsc : lines){
			renderer.drawAt(gsc.getRow(), gsc.getCol(), gsc.getGlyphString());
		}
		for(GlyphCoord gc : cells){
			renderer.drawAt(gc.getRow(), gc.getCol(), gc.getGlyph());
		}
	}

	@Override
	public long getFrameLength() {
		return frameLength;
	}

	@Override
	public Frame getMaskingFrame() {
		GlyphFrame mask = new GlyphFrame();
		mask.cells = new HashSet<>();
		mask.lines = new HashSet<>();
		Glyph.Builder cellMaskBuilder = new Glyph.Builder(" ");
		for(GlyphCoord cell : cells){
			mask.cells.add(new GlyphCoord(cell.getRow(), cell.getCol(), cellMaskBuilder.build()));
		}
		GlyphString.Builder lineMaskBuilder = new GlyphString.Builder(" ");
		for(GlyphStringCoord line : lines){
			String base = line.getGlyphString().getBaseString();
			lineMaskBuilder.withBaseString(base);
			mask.lines.add(new GlyphStringCoord(line.getRow(), line.getCol(), lineMaskBuilder.build()));
		}
		return mask;	
	}
	
	public static class Builder{
		private Set<GlyphStringCoord> lines = new HashSet<>();
		private long frameLength;
		private Set<GlyphCoord> cells = new HashSet<>();
		
		public Builder withFrameLength(long frameLength){
			this.frameLength = frameLength;
			return this;
		}
		
		public Builder withLines(Set<GlyphStringCoord> lines){
			this.lines = new HashSet<>(lines);
			return this;
		}
		
		public Builder addLine(GlyphStringCoord line){
			this.lines.add(line);
			return this;
		}
		
		public Builder withCells(Set<GlyphCoord> cells){
			this.cells = new HashSet<>(cells);
			return this;
		}
		
		public Builder addCell(GlyphStringCoord line){
			this.lines.add(line);
			return this;
		}
		
		public GlyphFrame build(){
			GlyphFrame gsf = new GlyphFrame();
			gsf.lines = new HashSet<>(this.lines);
			gsf.cells = new HashSet<>(this.cells);
			gsf.frameLength = this.frameLength;
			return gsf;
		}
		
	}
	

}
