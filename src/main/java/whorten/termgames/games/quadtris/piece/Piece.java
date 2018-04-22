package whorten.termgames.games.quadtris.piece;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Piece {

	protected Coord baseCoord;
	protected Set<Coord> offSets = new HashSet<>();
	protected Glyph defaultCellGlyph = null;
	
	public Set<Coord> rotateClockwise(){
		offSets = offSets.stream()
		       .map(this::rotateCW)
		       .collect(Collectors.toSet());  
		return offSets;
	};
	public Set<Coord> rotateCounterClockwise(){
		offSets = offSets.stream()
			       .map(this::rotateCCW)
			       .collect(Collectors.toSet());  
			return offSets;
	}
	public void moveDown(int blocks){
		baseCoord = new Coord(baseCoord.getCol(), baseCoord.getRow() + blocks);
	}
	public Set<Coord> getCoords(){
		return offSets.stream()
				.map((Coord c) -> new Coord(baseCoord.getCol() + c.getCol(),
						                    baseCoord.getRow() + c.getRow()))
				.collect(Collectors.toSet());
	};
	
	public Glyph getDefaultCell(){
		return defaultCellGlyph;
	}

	private Coord rotateCW(Coord coord){
		return new Coord(-coord.getRow(), coord.getCol());
	}
	
	private Coord rotateCCW(Coord coord){
		return new Coord(coord.getRow(), -coord.getCol());
	}

}
