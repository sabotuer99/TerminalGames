package whorten.termgames.utils;

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
