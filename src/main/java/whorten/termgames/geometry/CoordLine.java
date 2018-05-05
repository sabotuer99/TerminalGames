package whorten.termgames.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CoordLine extends HashSet<Coord> {
	private static final long serialVersionUID = -6097179666156097748L;

	public CoordLine(Coord origin, Coord end){
		if(origin == null || end == null){
			throw new IllegalArgumentException("Cannot create CoordLine with null endpoint");
		}
				
		int origin_x = origin.getCol();
		int origin_y = origin.getRow();
		int end_x = end.getCol();
		int end_y = end.getRow();
		int direction_x = origin_x < end_x ? 1 : -1;
		int direction_y = origin_y < end_y ? 1 : -1;
		
		List<Integer> rows = new ArrayList<>();
		List<Integer> cols = new ArrayList<>();
		
		for(int i = 0; i <= Math.abs(origin_x - end_x); i++){
			int x = origin_x + i * direction_x;
			cols.add(x);
		}
		
		for(int i = 0; i <= Math.abs(origin_y - end_y); i++){
			int y = origin_y + i * direction_y;
			rows.add(y);
		}
		
		if(rows.size() > cols.size()){
			for(int i = 0; i < rows.size(); i++){
				int y = rows.get(i);
				int x = cols.get(interpolate(i, cols.size(), rows.size()));
				this.add(new Coord(x, y));
			}
		} else {
			for(int i = 0; i < cols.size(); i++){
				int x = cols.get(i);
				int y = rows.get(interpolate(i, rows.size(), cols.size()));
				this.add(new Coord(x, y));
			}
		}
		
	}

	private int interpolate(int i, int small, int big) {
		return (int) (((i * 1.0)/big) * small);
	}

}
