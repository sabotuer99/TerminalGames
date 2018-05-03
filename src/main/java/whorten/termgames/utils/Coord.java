package whorten.termgames.utils;

import java.util.Collection;
import java.util.Random;

public final class Coord {
	
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
	
	public static Coord up(Coord coord, int up){
		return new Coord(coord.getCol(), coord.getRow() - up);
	}
	
	public static Coord down(Coord coord, int down){
		return new Coord(coord.getCol(), coord.getRow() + down);
	}
	
	public static Coord left(Coord coord, int left){
		return new Coord(coord.getCol() - left, coord.getRow());
	}
	
	public static Coord right(Coord coord, int right){
		return new Coord(coord.getCol() + right, coord.getRow());
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
	
	public static String toAsciiString(Collection<Coord> set){
		//determine the limits and offsets
		int minrow = Integer.MAX_VALUE;
		int mincol = Integer.MAX_VALUE;
		int maxrow = Integer.MIN_VALUE;
		int maxcol = Integer.MIN_VALUE;
		for(Coord offset : set){
			minrow = Math.min(minrow, offset.getRow());
			mincol = Math.min(mincol, offset.getCol());
			maxrow = Math.max(maxrow, offset.getRow());
			maxcol = Math.max(maxcol, offset.getCol());
		}
		
		int width = maxcol - mincol + 1;
		int height = maxrow - minrow + 1;
		int rowOffset = 0 - minrow;
		int colOffset = 0 - mincol;
		
		boolean[][] grid = new boolean[height][width];
		for(Coord offset : set){
			grid[offset.getRow() + rowOffset][offset.getCol() + colOffset] = true;
		}
		
		StringBuilder sb = new StringBuilder();
		for(boolean[] row : grid){
			for(boolean cell : row){
				sb.append(cell ? "#" : " ");
			}
			sb.append("\n");
		}
		//drop the last newline
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return String.format("Coord: col: [%d], row: [%d]", col, row);
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
