package whorten.termgames.games.quadtris.piece;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;

public class Piece {

	// TODO Write some unit tests...
	private Coord baseCoord;
	private Set<Coord> offSets = new HashSet<>();
	private Glyph defaultCellGlyph = null;

	private Piece(Coord baseCoord) {
		this(baseCoord, new HashSet<>());
	}

	private Piece(Coord baseCoord, Set<Coord> offsets) {
		this.baseCoord = baseCoord;
		this.offSets = new HashSet<>(offsets);
	}

	public Piece rotateClockwise() {
		Set<Coord> newOffSets = offSets.stream().map(Coord::rotateClockWise).collect(Collectors.toSet());
		return new Builder(this).withOffsets(newOffSets).build();
	};

	public Piece rotateCounterClockwise() {
		Set<Coord> newOffSets = offSets.stream().map(Coord::rotateCounterClockWise).collect(Collectors.toSet());
		return new Builder(this).withOffsets(newOffSets).build();
	}

	public Piece moveDown(int blocks) {
		Coord newBaseCoord = new Coord(baseCoord.getCol(), baseCoord.getRow() + blocks);
		return new Builder(this).withBaseCoord(newBaseCoord).build();
	}

	public Piece moveLeft(int blocks) {
		Coord newBaseCoord = new Coord(baseCoord.getCol() - blocks, baseCoord.getRow());
		return new Builder(this).withBaseCoord(newBaseCoord).build();
	}

	public Piece moveRight(int blocks) {
		Coord newBaseCoord = new Coord(baseCoord.getCol() + blocks, baseCoord.getRow());
		return new Builder(this).withBaseCoord(newBaseCoord).build();
	}

	public Set<Coord> getCoords() {
		return offSets.stream().map((Coord c) -> Coord.add(baseCoord, c)).collect(Collectors.toSet());
	};

	public Glyph getDefaultCell() {
		return defaultCellGlyph;
	}

	@Override
	public String toString() {
		return String.format("Piece: baseCoord: [%s], defaultCellGlyph: [%s], asciiRep: \n %s", 
				baseCoord, defaultCellGlyph, toAsciiString());
	}
	
	public String toAsciiString(){
		return Coord.toAsciiString(offSets);
	}
	
 	public static class Builder {

		private Coord baseCoord;
		private Set<Coord> offSets;
		private Glyph defaultGlyph;

		public Builder(Coord baseCoord) {
			this.baseCoord = baseCoord;
		}

		public Builder(Piece basePiece) {
			this.baseCoord = basePiece.baseCoord;
			this.offSets = new HashSet<>(basePiece.offSets);
			this.defaultGlyph = basePiece.defaultCellGlyph;
		}

		public Piece build() {
			if (baseCoord == null || offSets == null || offSets.size() != 4 || defaultGlyph == null) {
				throw new IllegalStateException("Must set baseCoord, 4 Offset Coords, and a default Glyph!");
			}

			Piece p = new Piece(this.baseCoord, this.offSets);
			p.defaultCellGlyph = defaultGlyph;
			return p;
		}

		public Builder withDefaultGlyph(Glyph defaultGlyph) {
			this.defaultGlyph = defaultGlyph;
			return this;
		}

		public Builder withOffsets(Set<Coord> offSets) {
			this.offSets = new HashSet<>(offSets);
			return this;
		}

		public Builder withBaseCoord(Coord baseCoord) {
			this.baseCoord = baseCoord;
			return this;
		}
	}
}
