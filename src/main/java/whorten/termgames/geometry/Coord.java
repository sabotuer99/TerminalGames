package whorten.termgames.geometry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class Coord implements Comparable<Coord>{
	
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
	
	public static double distance(Coord a, Coord b){
		int rise = a.getRow() - b.getRow();
		int run = a.getCol() - b.getCol();
		return Math.sqrt(rise * rise + run * run);
	}
	
	public static int gridDistance(Coord a, Coord b){
		int rise = a.getRow() - b.getRow();
		int run = a.getCol() - b.getCol();
		return Math.abs(rise) + Math.abs(run);
	}
	
	public static Set<Coord> allUp(Set<Coord> coords, int up){
		Set<Coord> moved = new HashSet<>();
		for(Coord coord : coords){
			moved.add(up(coord, up));
		}
		return moved;
	}
	
	public static Set<Coord> allDown(Set<Coord> coords, int down){
		Set<Coord> moved = new HashSet<>();
		for(Coord coord : coords){
			moved.add(down(coord, down));
		}
		return moved;
	}
	
	public static Set<Coord> allLeft(Set<Coord> coords, int left){
		Set<Coord> moved = new HashSet<>();
		for(Coord coord : coords){
			moved.add(left(coord, left));
		}
		return moved;
	}
	
	public static Set<Coord> allRight(Set<Coord> coords, int right){
		Set<Coord> moved = new HashSet<>();
		for(Coord coord : coords){
			moved.add(right(coord, right));
		}
		return moved;
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
	
	public static int calculateWidth(Collection<Coord> set){

		int mincol = Integer.MAX_VALUE;
		int maxcol = Integer.MIN_VALUE;
		for(Coord offset : set){
			mincol = Math.min(mincol, offset.getCol());
			maxcol = Math.max(maxcol, offset.getCol());
		}
		return maxcol - mincol + 1;
	}
	
	public static int calculateHeight(Collection<Coord> set){
		//determine the limits and offsets
		int minrow = Integer.MAX_VALUE;
		int maxrow = Integer.MIN_VALUE;
		for(Coord offset : set){
			minrow = Math.min(minrow, offset.getRow());
			maxrow = Math.max(maxrow, offset.getRow());
		}
		return maxrow - minrow + 1;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		Coord other = (Coord) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public int compareTo(Coord o) {
		if(this.row == o.getRow()){
			return this.col - o.getCol();
		}
		return this.row - o.getRow();
	}
}
