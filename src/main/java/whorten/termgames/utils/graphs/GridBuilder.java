package whorten.termgames.utils.graphs;

import java.util.HashMap;
import java.util.Map;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class GridBuilder {

	boolean[][] grid;
	int height;
	int width;
	
	public GridBuilder(int height, int width){
		grid = new boolean[height][width];
		this.height = height;
		this.width = width;
	}
	
	public Map<Coord, GridNode> build(){
		Map<Coord, GridNode> map = new HashMap<>();
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				convertToNode(row, col, map);
			}
		}
		return map;
	}

	private void convertToNode(int row, int col, Map<Coord, GridNode> map) {
		if(isNode(row, col)){
			Coord location = new Coord(col, row);
			GridNode node = new GridNode(location);
			addNeighbor(node, row - 1, col, map, Direction.UP);
			addNeighbor(node, row, col - 1, map, Direction.LEFT);
			map.put(location, node);
		}
	}
	
	private void addNeighbor(GridNode node, int row, int col, Map<Coord, GridNode> map, Direction dir) {
		if(isNode(row, col)){
			GridNode here = map.get(new Coord(col, row));
			node.addNeighbor(here, dir);
			here.addNeighbor(node, dir.getOpposite());
		}
	}

	private boolean isNode(int row, int col) {
		return !outOfBounds(row, col) && grid[row][col];
	}
	
	public <K> GridBuilder withGrid(K[][] grid, K key){
		checkBounds(grid.length, grid[0].length);
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				this.grid[row][col] = grid[row][col].equals(key);
			}
		}
		
		return this;
	}
	
	public GridBuilder withGrid(String[] grid, String key) {
		checkBounds(grid.length, grid[0].length());
		for(int row = 0; row < grid.length; row++){
			String[] r = grid[row].split("");
			for(int col = 0; col < r.length; col++){
				this.grid[row][col] = r[col].equals(key);
			}
		}
		
		return this;
	}

	public GridBuilder withGrid(boolean[][] grid){
		checkBounds(grid.length, grid[0].length);
		
		for(int i = 0; i < grid.length; i++){
			System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
		}
		
		return this;
	}

	private void checkBounds(int height, int width) {
		if(height != this.grid.length || width != this.grid[0].length){
			throw new IllegalArgumentException("Grid must match height and width of builder!");
		}
	}
	
	public GridBuilder withNodeAt(Coord coord){
		int row = coord.getRow();
		int col = coord.getCol();
		if(outOfBounds(row, col)){
			throw new IllegalArgumentException("Node location is out of bounds!");
		}
		
		grid[row][col] = true;
		return this;
	}

	private boolean outOfBounds(int row, int col) {
		return row < 0 || row >= height || col < 0 || col >= width;
	}


}
