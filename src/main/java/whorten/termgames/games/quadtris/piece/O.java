package whorten.termgames.games.quadtris.piece;

import whorten.termgames.glyphs.Block;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class O extends Piece{

	/*
	 *  ##
	 *  ##
	 */
	public O(Coord baseCoord){
		super(baseCoord);
		offSets.add(new Coord(0,0));
		offSets.add(new Coord(0,1));
		offSets.add(new Coord(1,0));
		offSets.add(new Coord(1,1));
		defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
				                    .withForegroundColor(255, 255, 0)
				                    .build();
	}
	
}
