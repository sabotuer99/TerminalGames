package whorten.termgames.games.quadtris.piece;

import whorten.termgames.glyphs.Block;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class I extends Piece{
	
	/*
	 *  #
	 *  #
	 *  #
	 *  #
	 */
	public I(Coord baseCoord){
		super(baseCoord);
		offSets.add(new Coord(0,1));
		offSets.add(new Coord(0,0));
		offSets.add(new Coord(0,-1));
		offSets.add(new Coord(0,-2));
		defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
				                    .withForegroundColor(0, 255, 255)
				                    .build();
	}
}
