package whorten.termgames.glyphs.gradient;

public class GradientSpec {

	private GradientSpec(){};
	private int[] startRGB = new int[3];
	private int[] endRGB = new int[3];
	private boolean isHorizontal;
	
	public int rows;
	public int cols;
	
	public int[] rgbFor(int row, int col){
		if(row < 0 || row >= rows || col < 0 || col >= cols){
			throw new IllegalArgumentException("Out of bounds");
		}
		
		if(row == 0 && col == 0){
			return startRGB.clone();
		}
		
		int[] offsets = null;
		if(isHorizontal){
			offsets = getDiffs(startRGB, endRGB, col, cols);
		} else {
			offsets = getDiffs(startRGB, endRGB, row, rows);
		}
		
		int[] result = add(startRGB, offsets);
		
		return result;
	}
	
	
	private int[] getDiffs(int[] start, int[] end, int index, int total) {
		int[] diffs = new int[3];
		double factor = (index * 1.0) / (total - 1);
		diffs[0] = (int) ((end[0] - start[0]) * factor);
		diffs[1] = (int) ((end[1] - start[1]) * factor);
		diffs[2] = (int) ((end[2] - start[2]) * factor);
		return diffs;
	}
	
	private int[] add(int[] a, int[] b){
		int[] sum = new int[3];
		sum[0] = a[0] + b[0];
		sum[1] = a[1] + b[1];
		sum[2] = a[2] + b[2];
		return sum;
	}

	public static class Builder{
		private int[] startRGB = new int[3];
		private int[] endRGB = new int[3];
		private boolean isHorizontal = false;
		private int rows = 1;
		private int cols = 1;
		
		public GradientSpec build(){
			GradientSpec gs = new GradientSpec();
			gs.startRGB = startRGB;
			gs.endRGB = endRGB;
			gs.rows = rows;
			gs.cols = cols;
			gs.isHorizontal = isHorizontal;
			return gs;
		}

		public Builder isHorizontal(){
			isHorizontal = true;
			return this;
		}
		
		public Builder isVertical(){
			isHorizontal = false;
			return this;
		}
		
		public Builder withStartRGB(int r, int g, int b){
			startRGB = setRGB(r,g,b);
			return this;
		}
		
		public Builder withEndRGB(int r, int g, int b){
			endRGB = setRGB(r,g,b);
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
			rgb[0] = Math.min(255, Math.max(r, 0));
			rgb[1] = Math.min(255, Math.max(g, 0));
			rgb[2] = Math.min(255, Math.max(b, 0));
			return rgb;
		}

		public Builder setHorizontal(boolean isHorizontal) {
			this.isHorizontal = isHorizontal;
			return this;
		}
	}
}
