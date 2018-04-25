package whorten.termgames.games.quadtris.piece;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Coord;

public class PieceRenderer {

	private Coord originOffset;
	private Renderer renderer;
	private PieceRenderer(){}
	
	public void drawPiece(Piece piece){
		Set<Coord> transCoords = transformCoords(piece.getCoords());
		Glyph.Builder baseBuilder = new Glyph.Builder(piece.getDefaultCell());
		for(Coord c : transCoords){
			if(c.getCol() % 2 == originOffset.getCol() % 2){ // even column relative to originOffset
				baseBuilder.withBase("⌊").withForegroundColor(64, 64, 64);
			} else {
				baseBuilder.withBase("⌉").withForegroundColor(180, 180, 180);
			}
			renderer.drawAt(c.getRow(), c.getCol(), baseBuilder.build());
		}
	}
	
	public void clearPiece(Piece piece){
		Set<Coord> transCoords = transformCoords(piece.getCoords());
		for(Coord c : transCoords){
			renderer.clear(c.getRow(), c.getCol());
		}
	}
	
	private Set<Coord> transformCoords(Set<Coord> coords) {
		Set<Coord> t = new HashSet<>();
		for(Coord c : coords){
			t.add(new Coord(originOffset.getCol() + c.getCol() * 2, 
					        originOffset.getRow() + c.getRow()));
			t.add(new Coord(originOffset.getCol() + c.getCol() * 2 + 1, 
					        originOffset.getRow() + c.getRow()));
		}
		return t;
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
		
		public PieceRenderer build(){
			PieceRenderer pr = new PieceRenderer();
			pr.originOffset = this.originOffset;
			pr.renderer = this.renderer;
			return pr;
		}
		
	}
}
