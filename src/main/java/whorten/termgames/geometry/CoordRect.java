package whorten.termgames.geometry;

import java.util.HashSet;

public class CoordRect extends HashSet<Coord> {
	private static final long serialVersionUID = 7527092388663018414L;

	public CoordRect(Coord origin, Coord end){
		Coord corner1 = new Coord(origin.getCol(), end.getRow());
		Coord corner2 = new Coord(end.getCol(), origin.getRow());

		// create 4 lines of coords and union them
		this.addAll(new CoordLine(origin,corner1));
		this.addAll(new CoordLine(origin,corner2));
		this.addAll(new CoordLine(corner1,end));
		this.addAll(new CoordLine(corner2,end));
	}
}
