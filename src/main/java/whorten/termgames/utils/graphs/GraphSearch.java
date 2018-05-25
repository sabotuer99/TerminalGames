package whorten.termgames.utils.graphs;

import java.util.List;

import whorten.termgames.geometry.Direction;

public interface GraphSearch {

	List<Direction> findPath(GridNode start, GridNode end);
}
