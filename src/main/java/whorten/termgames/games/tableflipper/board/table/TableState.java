package whorten.termgames.games.tableflipper.board.table;

import java.util.TreeSet;

import whorten.termgames.entity.AbstractEntityState;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

public class TableState extends AbstractEntityState<TableState>{

	private static final Glyph baseGlyph = new Glyph.Builder(" ")
			                               .withForegroundColor(175, 255, 175)
			                               .build();
	private static final String UPRIGHT = "┬─┬";
	private static final String FLIPPED = "⊥⚊⊥";
	
	private String baseString = UPRIGHT;
	private boolean isFlipped = false;

	public static TableState getStartState(Coord baseCoord){
		TableState ps = new TableState();
		ps.baseString = UPRIGHT;
		ps.coords = new TreeSet<Coord>();
		ps.coords.add(baseCoord);
		for(int i = 1; i < 3; i++){
			ps.coords.add(Coord.right(baseCoord, i));
		}
		return ps;
	}
	
	@Override
	public TableState moveUp(int distance) {
		TableState up = new TableState();
		up.coords = new TreeSet<>(Coord.allUp(this.coords, distance));
		return up;
	}

	@Override
	public TableState moveDown(int distance) {
		TableState down = new TableState();
		down.coords = new TreeSet<>(Coord.allDown(this.coords, distance));
		return down;
	}

	@Override
	public TableState moveLeft(int distance) {
		TableState left = new TableState();
		left.coords = new TreeSet<>(Coord.allLeft(this.coords, distance));
		return left;
	}

	@Override
	public TableState moveRight(int distance) {
		TableState right = new TableState();
		right.coords = new TreeSet<>(Coord.allRight(this.coords, distance));
		return right;
	}
	
	@Override
	public String getBaseString() {
		return baseString;
	}
	
	@Override
	public Glyph getBaseGlyph() {
		return baseGlyph;
	}

	public TableState flip(){
		TableState ts = new TableState();
		ts.coords = new TreeSet<>(this.coords);
		ts.baseString = FLIPPED;
		ts.isFlipped = true;
		return ts;
	}
	
	public TableState unflip(){
		TableState ts = new TableState();
		ts.coords = new TreeSet<>(this.coords);
		ts.baseString = UPRIGHT;
		ts.isFlipped = false;
		return ts;
	}

	public boolean isFlipped() {
		return isFlipped ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((baseString == null) ? 0 : baseString.hashCode());
		result = prime * result + (isFlipped ? 1231 : 1237);
		result = prime * result + ((getBaseCoord() == null) ? 0 : getBaseCoord().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableState other = (TableState) obj;
		if (baseString == null) {
			if (other.baseString != null)
				return false;
		} else if (!baseString.equals(other.baseString))
			return false;
		if (isFlipped != other.isFlipped)
			return false;
		if (getBaseCoord() == null) {
			if (other.getBaseCoord() != null)
				return false;
		} else if (!getBaseCoord().equals(other.getBaseCoord()))
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return String.format("TableState: baseString:[%s] baseCoord:[%s] baseGlyph:[%s] isFlipped:[%b]",
				getBaseString(),
				getBaseCoord(),
				getBaseGlyph(),
				isFlipped);
	}

}
