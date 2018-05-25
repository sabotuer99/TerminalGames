package whorten.termgames.utils.graphs;

import static com.google.common.truth.Truth.*;
import static whorten.termgames.geometry.Direction.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class AStarTests {

	@Test
	public void emptyGrid_StraightShot(){
		GraphSearch sut = new AStar();
		String[] grid   = {"   ",
						   "   ", //start is first column, end is last, in this row
						   "   "};
		Map<Coord,GridNode> graph = new GridBuilder(3,3).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(0,1));
		GridNode end = graph.get(new Coord(2,1));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(RIGHT, RIGHT);
	}
	
	@Test
	public void gridWithObstacle_GoesAround(){
		GraphSearch sut = new AStar();
		String[] grid   = {"   ",
						   " X ", //start is first column, end is last, in this row
						   " X "};
		Map<Coord,GridNode> graph = new GridBuilder(3,3).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(0,1));
		GridNode end = graph.get(new Coord(2,1));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(UP, RIGHT, RIGHT, DOWN);
	}
	
	@Test
	public void gridWithComplexObstacle_FindsShortestRoute(){
		GraphSearch sut = new AStar();
		String[] grid   = {"       ",  //should go around to the left
				           " XXXXX ",
				           "     X ",  //then straight down
						   "x X  X ", 
						   "x X  X ",
						   "x x    "};
		Map<Coord,GridNode> graph = new GridBuilder(6,7).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(6,0));
		GridNode end = graph.get(new Coord(1,5));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(LEFT, LEFT, LEFT, LEFT, LEFT, LEFT, 
				DOWN, DOWN, RIGHT, DOWN, DOWN, DOWN);
	}
	
}
