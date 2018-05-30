package whorten.termgames.utils.graphs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;

public class GridNode {
	private Coord coord;
	private Map<Direction, GridNode> neighbors = new HashMap<>();
	private Map<GridNode, Integer> costs = new HashMap<>();
	
	public GridNode(Coord coord){
		this.coord = coord;
	}
	
	public void addNeighbor(GridNode node, Direction direction){
		neighbors.put(direction, node);
		costs.put(node, 1);
	}
	
	public void addNeighborWithCost(GridNode node, Direction direction, Integer cost){
		neighbors.put(direction, node);
		costs.put(node, cost);
	}
	
	public void removeNeighbor(Direction direction){
		GridNode n = neighbors.remove(direction);
		if(n != null){
			costs.remove(n);
		}
	}
	
	public Coord getLocation(){
		return coord;
	}
	
	public GridNode getNeighbor(Direction direction){
		return neighbors.get(direction);
	}
	
	public Integer getEdgeCost(GridNode neighbor){
		return costs.get(neighbor);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
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
		GridNode other = (GridNode) obj;
		if (coord == null) {
			if (other.coord != null)
				return false;
		} else if (!coord.equals(other.coord))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("GridNode: location:[%s] ", getLocation()));
		sb.append("neighbors: [ ");
		for(Direction key : neighbors.keySet()){
			sb.append(String.format(" %s (Cost %s),", key, costs.get(neighbors.get(key))));
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ]");
		return sb.toString();
	}

	public Set<Entry<Direction, GridNode>> getNeighbors() {
		return Collections.unmodifiableSet(neighbors.entrySet());
	}

}
