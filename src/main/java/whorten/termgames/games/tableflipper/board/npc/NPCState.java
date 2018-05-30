package whorten.termgames.games.tableflipper.board.npc;

import java.util.TreeSet;

import whorten.termgames.entity.AbstractEntityState;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

public class NPCState extends AbstractEntityState<NPCState> {

	private String baseString;
	private Glyph baseGlyph;

	private NPCState(String baseString) {
		this.baseGlyph = new Glyph.Builder(" ").withForegroundColor(125,255,255).build(); //bright pale blue
		this.baseString = baseString;
		this.coords = new TreeSet<>();
	}

	public NPCState(NPCState base) {
		this.baseGlyph = base.baseGlyph;
		this.baseString = base.baseString;
		this.coords = new TreeSet<>(base.coords);
	}

	public static NPCState getStartState(Coord baseCoord){
		NPCState ps = new NPCState(NPCStrings.STAND_STILL);
		ps.coords = new TreeSet<Coord>();
		ps.coords.add(baseCoord);
		for(int i = 1; i < 7; i++){
			ps.coords.add(Coord.right(baseCoord, i));
		}
		return ps;
	}

	@Override
	public String getBaseString() {	
		return baseString;
	}

	@Override
	public Coord getBaseCoord() {
		return coords.iterator().next();
	}

	@Override
	public NPCState moveUp(int distance) {
		NPCState up = new NPCState(NPCStrings.WALK_UP);
		up.coords = new TreeSet<>(Coord.allUp(this.coords, distance));
		return up;
	}

	@Override
	public NPCState moveDown(int distance) {
		NPCState down = new NPCState(NPCStrings.WALK_DOWN);
		down.coords = new TreeSet<>(Coord.allDown(this.coords, distance));
		return down;
	}

	@Override
	public NPCState moveLeft(int distance) {
		NPCState left = new NPCState(NPCStrings.WALK_LEFT);
		left.coords = new TreeSet<>(Coord.allLeft(this.coords, distance));
		return left;
	}

	@Override
	public NPCState moveRight(int distance) {
		NPCState right = new NPCState(NPCStrings.WALK_RIGHT);
		right.coords = new TreeSet<>(Coord.allRight(this.coords, distance));
		return right;
	}
	
	public NPCState unflipLeft() {
		NPCState left = new NPCState(NPCStrings.RESTORE_LEFT);
		left.coords = new TreeSet<>(this.coords);
		return left;
	}

	public NPCState unflipRight() {
		NPCState right = new NPCState(NPCStrings.RESTORE_RIGHT);
		right.coords = new TreeSet<>(this.coords);
		return right;
	}

	@Override
	public Glyph getBaseGlyph() {
		return baseGlyph;
	}
	
	@Override
	public String toString() {
		return String.format("NPCState: baseString:[%s] baseCoord:[%s] baseGlyph:[%s]",
				getBaseString(),
				getBaseCoord(),
				getBaseGlyph());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseString == null) ? 0 : baseString.hashCode());
		result = prime * result + ((getBaseCoord() == null) ? 0 : getBaseCoord().hashCode());
		result = prime * result + ((getBaseGlyph() == null) ? 0 : getBaseGlyph().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NPCState other = (NPCState) obj;
		return (this.baseString.equals(other.baseString) 
		     && this.getBaseCoord().equals(other.getBaseCoord())
		     && this.getBaseGlyph().equals(other.getBaseGlyph()));
	}

	public NPCState stand() {
		NPCState right = new NPCState(NPCStrings.STAND_STILL);
		right.coords = new TreeSet<>(this.coords);
		return right;
	}

}
