package whorten.termgames.utils.graphs;

import static com.google.common.truth.Truth.assertThat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class GridBuilderTests {

	@Test
	public void fromGrid_buildGraph(){
		GridBuilder sut = new GridBuilder(3,3);
		boolean[][] grid = {{true, true,  true},
							{true, false, false},
							{true, false, false}};
		Map<Coord,GridNode> result = sut.withGrid(grid).build();
		Set<Coord> expected = toCoords(grid);
		
		assertThat(result.keySet()).containsAllIn(expected);
		assertThat(result.get(new Coord(0,0)).getNeighbor(Direction.RIGHT))
			.isEqualTo(result.get(new Coord(1,0)));
	}
	
	@Test
	public void fromStringGrid_buildGraph(){
		GridBuilder sut = new GridBuilder(3,3);
		String[][] grid = {{"X", "X",  "X"},
						   {"X", " ", " "},
						   {"X", " ", " "}};
		Map<Coord,GridNode> result = sut.withGrid(grid, "X").build();
		Set<Coord> expected = toCoords(grid, "X");
		
		assertThat(result.keySet()).containsAllIn(expected);
		assertThat(result.get(new Coord(0,0)).getNeighbor(Direction.RIGHT))
			.isEqualTo(result.get(new Coord(1,0)));
	}
	
	@Test
	public void fromStringArray_buildGraph(){
		GridBuilder sut = new GridBuilder(3,3);
		String[] grid   = {"   ",
						   " ##",
						   " ##"};
		Map<Coord,GridNode> result = sut.withGrid(grid, " ").build();
		Set<Coord> expected = toCoords(grid, " ");
		
		assertThat(result.keySet()).containsAllIn(expected);
		assertThat(result.get(new Coord(0,0)).getNeighbor(Direction.RIGHT))
			.isEqualTo(result.get(new Coord(1,0)));
	}

	private Set<Coord> toCoords(boolean[][] grid) {		
		Set<Coord> set = new HashSet<>();
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				if(grid[row][col]){
					set.add(new Coord(col, row));					
				}
			}
		}
		return set;
	}
	
	private Set<Coord> toCoords(String[][] grid, String key) {		
		Set<Coord> set = new HashSet<>();
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				if(grid[row][col].equals(key)){
					set.add(new Coord(col, row));					
				}
			}
		}
		return set;
	}
	
	private Set<Coord> toCoords(String[] grid, String key) {		
		Set<Coord> set = new HashSet<>();
		for(int row = 0; row < grid.length; row++){
			String[] r = grid[row].split("");
			for(int col = 0; col < r.length; col++){
				if(r[col].equals(key)){
					set.add(new Coord(col, row));					
				}
			}
		}
		return set;
	}
}
