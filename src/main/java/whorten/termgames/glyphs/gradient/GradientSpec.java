package whorten.termgames.glyphs.gradient;

public class GradientSpec {

	private GradientSpec(){};
	private int[] startRGB = new int[3];
	private int[] rowEndRGB = new int[3];
	private int[] colEndRGB = new int[3];
	public int rows;
	public int cols;
	
	public int[] rgbFor(int row, int col){
		if(row < 0 || row >= rows || col < 0 || col >= cols){
			throw new IllegalArgumentException("Out of bounds");
		}
		int[] rowOffsets = getDiffs(startRGB, rowEndRGB, row, rows);
		int[] colOffsets = getDiffs(startRGB, colEndRGB, col, cols);
		
		int[] result = new int[3];
		result[0] = startRGB[0] + rowOffsets[0] + colOffsets[0];
		result[1] = startRGB[1] + rowOffsets[1] + colOffsets[1];
		result[2] = startRGB[2] + rowOffsets[2] + colOffsets[2];
		
		return result;
	}
	
	
	private int[] getDiffs(int[] start, int[] end, int index, int total) {
		int[] diffs = new int[3];
		double factor = index / (total - 1);
		diffs[0] = (int) ((end[0] - start[0]) * factor);
		diffs[1] = (int) ((end[1] - start[1]) * factor);
		diffs[2] = (int) ((end[2] - start[2]) * factor);
		return diffs;
	}


	public static class Builder{
		private int[] startRGB = new int[3];
		private int[] rowEndRGB = new int[3];
		private int[] colEndRGB = new int[3];
		private int rows = 1;
		private int cols = 1;
		
		public GradientSpec build(){
			GradientSpec gs = new GradientSpec();
			gs.startRGB = this.startRGB;
			gs.rowEndRGB = this.rowEndRGB;
			gs.colEndRGB = this.colEndRGB;
			gs.rows = this.rows;
			gs.cols = this.cols;
			return gs;
		}
		
		public Builder withStartRGB(int r, int g, int b){
			startRGB = setRGB(r,g,b);
			return this;
		}
		
		public Builder withRowEndRGB(int r, int g, int b){
			rowEndRGB = setRGB(r,g,b);
			return this;
		}		
		
		public Builder withColEndRGB(int r, int g, int b){
			colEndRGB = setRGB(r,g,b);
			return this;
		}
		
		public Builder withRows(int rows){
			this.rows = rows;
			return this;
		}
		
		public Builder withCols(int cols){
			this.cols = cols;
			return this;
		}
		
		private int[] setRGB(int r, int g, int b){
			int[] rgb = new int[3];
			rgb[0] = r;
			rgb[1] = g;
			rgb[2] = b;
			return rgb;
		}
	}
}
