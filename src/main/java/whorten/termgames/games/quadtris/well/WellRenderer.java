package whorten.termgames.games.quadtris.well;

import java.util.List;

import whorten.termgames.animation.Animation;
import whorten.termgames.animation.GlyphFrame;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceRenderer;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GlyphStringCoord;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.StringUtils;

public class WellRenderer {

	public Coord originOffset;
	public Renderer renderer;
	public PieceRenderer pieceRenderer;
	

	public void drawPiece(Piece piece){
		pieceRenderer.drawPiece(piece);
	}
	
	public void clearPiece(Piece piece){
		pieceRenderer.clearPiece(piece);
	}
	
	public Animation createLineFlashAnimation(List<Integer> rows){
		GlyphString[] lineReps = new GlyphString[2];
		GlyphString.Builder baseBuilder = new GlyphString.Builder(StringUtils.repeat(" ", 20));
		lineReps[0] = baseBuilder.build();
		lineReps[1] = baseBuilder.withBgColor(BgColor.WHITE).build();

		GlyphFrame.Builder aBuilder = new GlyphFrame.Builder().withFrameLength(10);
		GlyphFrame.Builder bBuilder = new GlyphFrame.Builder().withFrameLength(10);
		for(Integer row : rows){
			bBuilder.addLine(new GlyphStringCoord(originOffset.getRow() + row,
						                            originOffset.getCol(), 
						                            lineReps[0]));
			aBuilder.addLine(new GlyphStringCoord(originOffset.getRow() + row,
								                    originOffset.getCol(), 
								                    lineReps[1]));
		}
		
		return new Animation.Builder()
				.addFrame(aBuilder.build())
				.addFrame(bBuilder.build())
				.withRenderer(renderer)
				.withLoopCount(11)
				.build();
	}
	
	public void drawBlockWell() {
		//Draw inner play area
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 20; i++){
			sb.append("##")
			  .append(StringUtils.repeat(" ", 20))
			  .append("##")
			  .append("\n");
		}
		sb.append(StringUtils.repeat("#", 24)).append("\n");
		//sb.append(StringUtils.repeat("#", 24));
		//String[] lines = new BoxDrawingGenerator().transform(sb.toString().split("\n"));
		String[] lines = sb.toString().split("\n");
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			for(int j = 0; j < lines[0].length(); j++){
				String cell = Character.toString(line.charAt(j));
				if(!" ".equals(cell)){
					Glyph glyph = new Glyph.Builder("╳")
							.isBold(true)
							.withForegroundColor(0,125,255)
							.withBackgroundColor(0,0,255)
							.build();
					renderer.drawAt(originOffset.getRow()+i, originOffset.getCol()+j-2, glyph);
				}
			}
		}
	}
	
	public static class Builder{
		Coord originOffset = new Coord(0,0);
		Renderer renderer;
		
		public Builder(Renderer renderer){
			this.renderer = renderer;
		}
		
		public Builder withOriginOffset(Coord originOffset){
			this.originOffset = originOffset;
			return this;
		}
		
		public WellRenderer build(){
			WellRenderer wr = new WellRenderer();
			wr.originOffset = this.originOffset;
			wr.renderer = this.renderer;
			wr.pieceRenderer = new PieceRenderer.Builder(renderer)
					.withOriginOffset(originOffset).build();
			return wr;
		}
		
	}
}
