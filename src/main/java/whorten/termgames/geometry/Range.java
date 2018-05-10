package whorten.termgames.geometry;

public class Range {

	public int minCol;
	public int minRow;
	public int maxCol;
	public int maxRow;

	private Range(){}
	
	public boolean inRange(Coord subject){
		return subject.getCol() >= minCol &&
				subject.getCol() <= maxCol &&
				subject.getRow() >= minRow &&
				subject.getRow() <= maxRow;
	}
	
	
	
	public static class Builder{
		
		//by default range is all coords
		int minCol = Integer.MIN_VALUE;
		int minRow = Integer.MIN_VALUE;
		int maxCol = Integer.MAX_VALUE;
		int maxRow = Integer.MAX_VALUE;
		
		public Range build(){
			Range r = new Range();
			r.minCol = this.minCol;
			r.minRow = this.minRow;
			r.maxCol = this.maxCol;
			r.maxRow = this.maxRow;
			return r;
		}
		
		public Builder withMinRow(int minRow){
			this.minRow = minRow;
			return this;
		}	
		
		public Builder withMinCol(int minCol){
			this.minCol = minCol;
			return this;
		}	
		
		public Builder withMaxRow(int maxRow){
			this.maxRow = maxRow;
			return this;
		}	
		
		public Builder withMaxCol(int maxCol){
			this.maxCol = maxCol;
			return this;
		}
		
		public Builder betweenCoords(Coord topLeft, Coord bottomRight){
			minCol = topLeft.getCol();
			minRow = topLeft.getRow();
			maxCol = bottomRight.getCol();
			maxRow = bottomRight.getRow();		
			return this;
		}
		
	}
}
