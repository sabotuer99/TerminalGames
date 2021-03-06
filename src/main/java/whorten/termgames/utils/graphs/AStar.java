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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class AStar implements GraphSearch {

	private final static Logger logger = LogManager.getLogger(AStar.class);
	private Set<GridNode> lastSeen;
	private int limit;
	
	public AStar(){
		this(400);
	}
	
	public AStar(int limit){
		this.limit = limit;
	}
	
	@Override
	public List<Direction> findPath(GridNode start, GridNode end) {
		List<Direction> path = new ArrayList<>();
		if(start == null || end == null){
			if(start == null){logger.warn("Start node was null, wtf?");}
			if(end == null){logger.warn("End node was null, wtf?");}
			return path;
		}
		Set<GridNode> seen = new HashSet<>();
		Queue<PathNode> queue = new PriorityQueue<>(new Comparator<PathNode>(){
			@Override
			public int compare(PathNode o1, PathNode o2) {
				int d1 = (int) (Coord.gridDistance(o1.node.getLocation(), end.getLocation()) * 1.5 + o1.length);
				int d2 = (int) (Coord.gridDistance(o2.node.getLocation(), end.getLocation()) * 1.5 + o2.length);
				return Integer.compare(d1, d2);
			}		
		});
		queue.offer(new PathNode(start, null, null));
		while(queue.size() > 0 && queue.size() < limit){
			PathNode here = queue.poll();
			if(here.node.equals(end)){
				path = extractPath(here);
				break;
			}
			//logger.info(String.format("AStar looked at %s", here.node.getLocation()));
			seen.add(here.node);
			for(Entry<Direction,GridNode> neighbor : here.node.getNeighbors()){
				GridNode n = neighbor.getValue();
				Direction d = neighbor.getKey();
				if(!seen.contains(n)){
					queue.offer(new PathNode(n, here, d));
				}				
			}		
		}
		lastSeen = seen;
		
		//if the queue size hit the limit, return the best available path
		if(queue.size() == limit){
			PathNode bestAvailable = queue.poll();
			path = extractPath(bestAvailable); 
			logger.info(String.format("AStar did not finish, took the best available"));
		}
		
		
		return path;
	}

	public Set<GridNode> getLastSeenSet(){
		return new HashSet<>(lastSeen);
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

	class PathNode{
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
