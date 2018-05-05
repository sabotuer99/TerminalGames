package whorten.termgames.geometry;

import java.util.HashSet;

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
		
		
		int run = end_x - origin_x;
		int rise = end_y - origin_y;
		int arun = Math.abs(run);
		int arise = Math.abs(rise);
		int sign_run = run < 0 ? -1 : 1;
		int sign_rise = rise < 0 ? -1 : 1;
		int x = origin_x;
		int y = origin_y;
		for(int i = 0; i <= Math.max(arun, arise); i++){
			int next_x = x;
			int next_y = y;
			
			if(arun > arise){
				next_x += 1 * sign_run;
				if(rise != 0){
					next_y = getFractionalOffset(origin_y, run, i + 1);
				}			
			} else if(arun < arise){
				if(run != 0){
					next_x = getFractionalOffset(origin_x, rise, i + 1);					
				}
				next_y += 1 * sign_rise;				
			} else { //special case for 45 degrees
				next_x += 1 * sign_run;
				next_y += 1 * sign_rise;
			}
			
			this.add(new Coord(x,y));
			
			x = next_x;
			y = next_y;
		}
	}

	private int getFractionalOffset(int origin, int range, int i) {
		return (int) (origin + Math.round((i * 1.0)/range));
	}
}
