package whorten.termgames.games.quadtris.well;

import static whorten.termgames.utils.StringUtils.repeat;

import java.util.List;

import whorten.termgames.animation.Animation;
import whorten.termgames.animation.GlyphFrame;
import whorten.termgames.games.quadtris.cell.Cell;
import whorten.termgames.games.quadtris.cell.CellRenderer;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceRenderer;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GlyphStringCoord;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.BoxDrawingGenerator;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.StringUtils;

public class WellRenderer {

	private Coord originOffset;
	private Renderer renderer;
	private PieceRenderer pieceRenderer;
	private CellRenderer cellRenderer;
	private Glyph boxBorder;
	
	public void previewPiece(Piece piece){
		clearPreview();
		Coord center = new Coord(offsetCol(8),offsetRow(1));
		Piece centerpiece = new Piece.Builder(piece).withBaseCoord(center).build();
		drawPiece(centerpiece);
	}
	
	public void clearPreview(){
		GlyphString clearRow = new GlyphString.Builder(repeat(" ", 10)).build();
		for(int i = 0; i < 5; i++){
			renderer.drawAt(offsetRow(i+2),offsetCol(26),clearRow);
		}
	}

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

	public void drawBlockWell(){
		//Draw inner play area
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 20; i++){
			sb.append("##")
			  .append(repeat(" ", 20))
			  .append("##")
			  .append("\n");
		}
		sb.append(repeat("#", 24)).append("\n");
		//sb.append(StringUtils.repeat("#", 24));
		//String[] lines = new BoxDrawingGenerator().transform(sb.toString().split("\n"));
		String[] lines = sb.toString().split("\n");
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			for(int j = 0; j < lines[0].length(); j++){
				String cell = Character.toString(line.charAt(j));
				if(!" ".equals(cell)){
					renderer.drawAt(offsetRow(i), offsetCol(j-2), boxBorder);
				}
			}
		}
		
		drawPiecePreviewBox();
	}

	private int offsetCol(int j) {
		return originOffset.getCol()+j;
	}

	private int offsetRow(int i) {
		return originOffset.getRow() + i;
	}
	
	private void drawPiecePreviewBox(){
		StringBuilder sb = new StringBuilder();
		sb.append(repeat("#", 12)).append("\n");
		for(int i = 0; i < 5; i++){
			sb.append("#")
			  .append(repeat(" ", 10))
			  .append("#")
			  .append("\n");
		}
		sb.append(repeat("#", 12)).append("\n");
		String[] lines = new BoxDrawingGenerator()
				.transform(sb.toString().split("\n"));
		
		Glyph.Builder baseBuilder = new Glyph.Builder(" ")
				.withForegroundColor(0,0,255)
				.withBackgroundColor(0,120,255);
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			for(int j = 0; j < lines[0].length(); j++){
				String cell = Character.toString(line.charAt(j));
				if(!" ".equals(cell)){
					renderer.drawAt(offsetRow(i+1), offsetCol(j+25), 
							baseBuilder.withBase(cell).build());
				}
			}
		}
		GlyphString.Builder next = new GlyphString.Builder("Next piece:");
		renderer.drawAt(offsetRow(0), offsetCol(25), next.build());
	}
	
	public void drawWellCells(Well well){
		clearWellInterior();
		for(Cell cell : well.getCells()){
			cellRenderer.drawCell(cell);
		}
	}
	
	private void clearWellInterior() {
		GlyphString clearRow = new GlyphString.Builder(repeat(" ", 20)).build();
		for(int i = 0; i < 20; i++){
			renderer.drawAt(offsetRow(i),offsetCol(0),clearRow);
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
			wr.cellRenderer = new CellRenderer.Builder(renderer)
					.withOriginOffset(originOffset).build();
			wr.boxBorder = new Glyph.Builder("╳")
					.isBold(true)
					.withForegroundColor(0,125,255)
					.withBackgroundColor(0,0,255)
					.build();
			return wr;
		}
		
	}
}
