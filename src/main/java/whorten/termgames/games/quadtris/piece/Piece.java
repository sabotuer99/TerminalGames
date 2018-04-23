package whorten.termgames.games.quadtris.piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Piece {

	//TODO Write some unit tests...
	protected Coord baseCoord;
	protected Set<Coord> offSets = new HashSet<>();
	protected Glyph defaultCellGlyph = null;
	private static final List<Class<? extends Piece>> pieces = new ArrayList<>(7);
	static {
		pieces.add(O.class);
		pieces.add(I.class);
		pieces.add(S.class);
		pieces.add(Z.class);
		pieces.add(T.class);
		pieces.add(J.class);
		pieces.add(L.class);
	}
	
	public Piece(Coord baseCoord){
		this.baseCoord = baseCoord;
	}

	public Set<Coord> rotateClockwise() {
		//TODO refactor this to return a new Piece with coordinates rotated (think string.toUpperCase())
		offSets = offSets.stream().map(Coord::rotateClockWise).collect(Collectors.toSet());
		return offSets;
	};

	public Set<Coord> rotateCounterClockwise() {
		//TODO refactor this to return a new Piece with coordinates rotated (think string.toUpperCase())
		offSets = offSets.stream().map(Coord::rotateCounterClockWise).collect(Collectors.toSet());
		return offSets;
	}

	public void moveDown(int blocks) {
		//TODO refactor this to return a new Piece with baseCoord changed (think string.toUpperCase())
		baseCoord = new Coord(baseCoord.getCol(), baseCoord.getRow() + blocks);
	}

	public Set<Coord> getCoords() {
		return offSets.stream()
				.map((Coord c) -> Coord.add(baseCoord, c))
				.collect(Collectors.toSet());
	};

	public Glyph getDefaultCell() {
		return defaultCellGlyph;
	}

	public static Piece getRandomPiece() {
		int index = new Random().nextInt(7);
		Class<? extends Piece> c = pieces.get(index);
		try {
			return c.newInstance();
		} catch (Exception e) {
			// gulp
		}

		return null;
	}

}
