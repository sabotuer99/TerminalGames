package whorten.termgames.games.quadtris.cell;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Coord;

public class CellRenderer {

	
	public Coord originOffset;
	public Renderer renderer;

	public void drawCell(Cell cell){
		Set<Coord> transCoords = transformCoord(cell.getCoord());
		Glyph.Builder baseBuilder = new Glyph.Builder(cell.getBaseGlyph());
		for(Coord c : transCoords){
			if(c.getCol() % 2 == originOffset.getCol() % 2){ // even column relative to originOffset
				baseBuilder.withBase("⌊").withForegroundColor(64, 64, 64);
			} else {
				baseBuilder.withBase("⌉").withForegroundColor(180, 180, 180);
			}
			renderer.drawAt(c.getRow(), c.getCol(), baseBuilder.build());
		}
	}
	
	public void clearPiece(Cell cell){
		Set<Coord> transCoords = transformCoord(cell.getCoord());
		for(Coord c : transCoords){
			renderer.clear(c.getRow(), c.getCol());
		}
	}
	
	private Set<Coord> transformCoord(Coord coord) {
		Set<Coord> t = new HashSet<>();

			t.add(new Coord(originOffset.getCol() + coord.getCol() * 2, 
					        originOffset.getRow() + coord.getRow()));
			t.add(new Coord(originOffset.getCol() + coord.getCol() * 2 + 1, 
					        originOffset.getRow() + coord.getRow()));

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
		
		public CellRenderer build(){
			CellRenderer cr = new CellRenderer();
			cr.originOffset = this.originOffset;
			cr.renderer = this.renderer;
			return cr;
		}
		
	}
}
