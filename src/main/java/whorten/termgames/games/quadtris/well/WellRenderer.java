package whorten.termgames.games.quadtris.well;

import java.util.List;

import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceRenderer;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
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
	
	public void drawFlashes(List<Integer> rows){
		GlyphString[] lineReps = new GlyphString[2];
		GlyphString.Builder baseBuilder = new GlyphString.Builder(StringUtils.repeat(" ", 20));
		lineReps[0] = baseBuilder.build();
		lineReps[1] = baseBuilder.withBgColor(BgColor.WHITE).build();
		
		//i must be odd so that the last iteration is even i.e. black to clear
		for(int i = 0; i < 25; i++){
			GlyphString line = lineReps[i % 2];
			for(Integer row : rows){
				renderer.drawAt(originOffset.getRow() + row, originOffset.getCol(), line);
			}
			pause(20);
		}

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
	
	private void pause(long millis){
		try{
			Thread.sleep(millis);
		} catch (Exception ex){
			//gulp
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
