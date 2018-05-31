package whorten.termgames.utils.graphs;

import static com.google.common.truth.Truth.assertThat;
import static whorten.termgames.geometry.Direction.DOWN;
import static whorten.termgames.geometry.Direction.LEFT;
import static whorten.termgames.geometry.Direction.RIGHT;
import static whorten.termgames.geometry.Direction.UP;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	
	@Test
	public void destinationHasZeroValue_calculatesCorrectly(){
		GraphSearch sut = new AStar();
		String[] grid   = {"       ",  //should go around to the left
				           " XXXXX ",
				           "     X ",  //then straight down
						   "x X  X ", 
						   "x X  X ",
						   "x x    "};
		Map<Coord,GridNode> graph = new GridBuilder(6,7).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(1,4));
		GridNode end = graph.get(new Coord(0,0));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(UP, UP, LEFT, UP, UP);
	}
	
	@Test
	public void destinationHasZeroValue_BlankGrid_calculatesCorrectly(){
		AStar sut = new AStar();
		String[] grid   = {"                 ",  
				           "                 ",
				           "                 ",
						   "                 ", 
						   "                 ",
						   "                 "};
		Map<Coord,GridNode> graph = new GridBuilder(6,17).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(16,5));
		GridNode end = graph.get(new Coord(0,0));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(UP, UP, UP, UP, UP, LEFT, LEFT, LEFT, 
				                         LEFT, LEFT, LEFT, LEFT, LEFT, LEFT, 
				                         LEFT, LEFT, LEFT, LEFT, LEFT, LEFT, LEFT);
		
		System.out.println(Coord.toAsciiString(
				sut.getLastSeenSet()
				   .stream()
				   .map(gn -> gn.getLocation())
				   .collect(Collectors.toSet())));
	}
	
	@Test
	public void destinationHasZeroValue_WallInGrid_calculatesCorrectly(){
		AStar sut = new AStar();
		String[] grid   = {"                 ",  
				           "         xxxxx   ",
				           "         xxxxx   ",
						   "         xxxxx   ", 
						   "         xxxxx   ",
						   "         xxxxx   "};
		Map<Coord,GridNode> graph = new GridBuilder(6,17).withGrid(grid, " ").build();
		
		GridNode start = graph.get(new Coord(16,5));
		GridNode end = graph.get(new Coord(0,0));
		List<Direction> path = sut.findPath(start, end);
		
		assertThat(path).containsExactly(UP, UP, UP, UP, UP, LEFT, LEFT, LEFT, 
				                         LEFT, LEFT, LEFT, LEFT, LEFT, LEFT, 
				                         LEFT, LEFT, LEFT, LEFT, LEFT, LEFT, LEFT);
		
		System.out.println(Coord.toAsciiString(
				sut.getLastSeenSet()
				   .stream()
				   .map(gn -> gn.getLocation())
				   .collect(Collectors.toSet())));
	}
	
	
}
