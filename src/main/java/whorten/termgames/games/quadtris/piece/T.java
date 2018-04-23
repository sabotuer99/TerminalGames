package whorten.termgames.games.quadtris.piece;

import whorten.termgames.glyphs.Block;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class T extends Piece{
	/*
	 * ###
	 *  #
	 */
	public T(Coord baseCoord){
		super(baseCoord);
		offSets.add(new Coord(1,0));
		offSets.add(new Coord(0,0));
		offSets.add(new Coord(0,1));
		offSets.add(new Coord(-1,0));
		defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
				                    .withForegroundColor(255, 0, 255)
				                    .build();
	}
}
