package whorten.termgames.games.quadtris.piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import whorten.termgames.games.quadtris.piece.Piece.Builder;
import whorten.termgames.glyphs.Block;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class PieceFactory {
	private static final List<Function<Coord, Piece>> pieceMethods = new ArrayList<>();
	static {	
	    pieceMethods.add((Coord bc) -> getI(bc));
	    pieceMethods.add((Coord bc) -> getJ(bc));
	    pieceMethods.add((Coord bc) -> getL(bc));
	    pieceMethods.add((Coord bc) -> getO(bc));
	    pieceMethods.add((Coord bc) -> getS(bc));
	    pieceMethods.add((Coord bc) -> getT(bc));	    
	    pieceMethods.add((Coord bc) -> getZ(bc));
	}
	
	
	/*
	 *  #
	 *  #
	 *  #
	 *  #
	 */
	public static Piece getI(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(0,1));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,-1));
		coords.add(new Coord(0,-2));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
				                    .withForegroundColor(0, 255, 255)
				                    .withBackgroundColor(0, 255, 255)
				                    .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}		
	
	/*
	 *   #
	 *   #
	 *  ##
	 */
	public static Piece getJ(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(0,-1));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(-1,1));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
					                    .withForegroundColor(0, 0, 255)
					                    .withBackgroundColor(0, 0, 255)
					                    .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}		
	
	/*
	 *  #
	 *  #
	 *  ##
	 */
	public static Piece getL(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(0,-1));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(1,1));
		Glyph defaultCellGlyph =  new Glyph.Builder(Block.FULL.toString())
						                .withForegroundColor(255, 127, 0)
						                .withBackgroundColor(255, 127, 0)
						                .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}	
	
	/*
	 *  ##
	 *  ##
	 */
	public static Piece getO(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(1,0));
		coords.add(new Coord(1,1));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
					                    .withForegroundColor(255, 255, 0)
					                    .withBackgroundColor(255, 255, 0)
					                    .build();	
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}		
	
	/*
	 *   ##
	 *  ##
	 */
	public static Piece getS(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(1,0));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(-1,1));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
					                    .withForegroundColor(0, 255, 0)
					                    .withBackgroundColor(0, 255, 0)
					                    .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}
	
	/*
	 *  ###
	 *   #
	 */
	public static Piece getT(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(1,0));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(-1,0));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
					                    .withForegroundColor(255, 0, 255)
					                    .withBackgroundColor(255, 0, 255)
					                    .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}
	
	/*
	 *  ##
	 *   ##
	 */
	public static Piece getZ(Coord baseCoord){
		Set<Coord> coords = new HashSet<>();
		coords.add(new Coord(-1,0));
		coords.add(new Coord(0,0));
		coords.add(new Coord(0,1));
		coords.add(new Coord(1,1));
		Glyph defaultCellGlyph = new Glyph.Builder(Block.FULL.toString())
				                    .withForegroundColor(255, 0, 0)
				                    .withBackgroundColor(255, 0, 0)
				                    .build();
		
		return new Builder(baseCoord)
				.withDefaultGlyph(defaultCellGlyph)
				.withOffsets(coords)
				.build();
	}
	
	public static Piece getRandomPiece(Coord baseCoord) {
		int index = new Random().nextInt(7);
		return pieceMethods.get(index).apply(baseCoord);
	}
}
