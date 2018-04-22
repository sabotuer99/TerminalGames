package whorten.termgames.utils;

import java.util.Random;

public class Coord {
	
	public Coord(int col, int row){
		this.col = col;
		this.row = row;
	}
	
	private int col = 0; //col
	private int row = 0; //row
	
	public int getCol(){
		return col;
	}
	
	public int getRow(){
		return row;
	}
	
	public static Coord getRandomCoord(int minx, int miny, int maxx, int maxy) {
		return new Coord(new Random().nextInt(maxx - minx) + minx,
                         new Random().nextInt(maxy - miny) + miny);
	}
	
	public static Coord rotateClockWise(Coord coord) {
		return new Coord(-coord.getRow(), coord.getCol());
	}

	public static Coord rotateCounterClockWise(Coord coord) {
		return new Coord(coord.getRow(), -coord.getCol());
	}
	
	public static Coord add(Coord a, Coord b){
		return new Coord(a.getCol() + b.getCol(), a.getRow() + b.getRow());
	}
	
	@Override
	public String toString() {
		return String.format("[Coord: col: [%d], row: [%d]]", col, row);
	}
	
	@Override
	public int hashCode() {
		return col + (row << 16);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coord){
			Coord o = (Coord) obj;
			return o.col == this.col && o.row == this.row;
		}
		return false;
	}
}
