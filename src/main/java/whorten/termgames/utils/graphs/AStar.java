package whorten.termgames.utils.graphs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class AStar implements GraphSearch {

	@Override
	public List<Direction> findPath(GridNode start, GridNode end) {
		List<Direction> path = new ArrayList<>();
		Set<GridNode> seen = new HashSet<>();
		Queue<PathNode> queue = new PriorityQueue<>(new Comparator<PathNode>(){
			@Override
			public int compare(PathNode o1, PathNode o2) {
				double d1 = Coord.distance(o1.node.getLocation(), end.getLocation()) + o1.length;
				double d2 = Coord.distance(o2.node.getLocation(), end.getLocation()) + o2.length;
				return Double.compare(d1, d2);
			}		
		});
		
		queue.offer(new PathNode(start, null, null));
		while(queue.size() > 0){
			PathNode here = queue.poll();
			if(here.node.equals(end)){
				path = extractPath(here);
				break;
			}
			seen.add(here.node);
			for(Entry<Direction,GridNode> neighbor : here.node.getNeighbors()){
				GridNode n = neighbor.getValue();
				Direction d = neighbor.getKey();
				if(!seen.contains(n)){
					queue.offer(new PathNode(n, here, d));
				}				
			}		
		}
		
		return path;
	}

	private List<Direction> extractPath(PathNode end) {
		LinkedList<Direction> path = new LinkedList<>();
		PathNode here = end;
		while(here.parent != null){
			path.push(here.incoming);
			here = here.parent;
		}
		return path;
	}

	private class PathNode{
		GridNode node;
		PathNode parent;
		Direction incoming;
		Integer length;
		
		public PathNode(GridNode node, PathNode parent, Direction incoming){
			this.node = node;
			this.parent = parent;
			this.incoming = incoming;
			this.length = parent == null ? 0 : parent.length + 1;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PathNode other = (PathNode) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			return true;
		}
		private AStar getOuterType() {
			return AStar.this;
		}
		
		
	}
	
}
