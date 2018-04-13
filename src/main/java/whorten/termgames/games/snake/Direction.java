package whorten.termgames.games.snake;

public enum Direction {
	DOWN(0,1), UP(0,-1), RIGHT(1,0), LEFT(-1,0);

	private int dx; //col
	private int dy; //row

	Direction(int dx, int dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getDx(){
		return dx;
	}
	
	public int getDy(){
		return dy;
	}
}
