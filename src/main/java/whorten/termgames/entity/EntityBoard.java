package whorten.termgames.entity;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import whorten.termgames.geometry.Coord;

public class EntityBoard {
	
	private int width;
	private int height;
	private Map<Coord, Entity> entities = new HashMap<>();
	private int minCol;
	private int minRow;

	private EntityBoard(){}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	//invariant: Entity must be immutable
	public synchronized void addEntity(Entity test) {
		if(test == null){ return; }
		checkOverlapsOccupied(test); 
        checkOutOfBounds(test);
		
		for(Coord coord : test.getCoords()){
			entities.put(coord, test);
		}		
	}

	public synchronized Entity entityAt(Coord coord) {
		return entities.get(coord);
	}
	
	public synchronized Set<Entity> getUpNeighbors(Entity subject) {
		return getNeighborsWithLambda(subject, (Coord c) -> Coord.up(c,1));
	}
	
	public synchronized Set<Entity> getDownNeighbors(Entity subject) {
		return getNeighborsWithLambda(subject, (Coord c) -> Coord.down(c,1));
	}
	
	public synchronized Set<Entity> getRightNeighbors(Entity subject) {
		return getNeighborsWithLambda(subject, (Coord c) -> Coord.right(c,1));
	}
	
	public synchronized Set<Entity> getLeftNeighbors(Entity subject) {
		return getNeighborsWithLambda(subject, (Coord c) -> Coord.left(c,1));
	}
	
	public void removeEntity(Entity entity) {
		if(entity == null){ return; }
		for(Coord coord : entity.getCoords()){
			if(entities.get(coord) == entity){
				entities.remove(coord);
			}
		}
	}
	
	public synchronized boolean canAdd(Entity entity) {
		return entity != null && !isOverlapping(entity) && !isOutOfBounds(entity);
	}
	
	public synchronized boolean canMove(Entity from, Entity to){
		if(from != null && to != null && !isOutOfBounds(to)){
			return entities.keySet()
				.stream()
				.filter( c -> to.getCoords().contains(c) )
				.filter( c -> !entityAt(c).equals(from) )
				.collect(Collectors.toSet())
				.size() == 0;		
		}
		return false;
	}
	
	public synchronized void move(Entity from, Entity to){
		if(canMove(from, to)){
			removeEntity(from);
			addEntity(to);
		}
	}
	
	public static class Builder{

		private int width;
		private int height;
		private int minCol = 0;
		private int minRow = 0;
		
		public EntityBoard build() {
			EntityBoard b = new EntityBoard();
			b.width = this.width;
			b.height = this.height;
			b.minCol = this.minCol;
			b.minRow = this.minRow;
			return b;
		}

		public Builder withHeight(int height) {
			this.height = height;
			return this;
		}

		public Builder withWidth(int width) {
			this.width = width;
			return this;
		}
		
	}

	private void checkOverlapsOccupied(Entity test) {
		if (isOverlapping(test)){
			throw new IllegalArgumentException("Entity location overlaps with existing entity!");
		}
	}
	
	private boolean isOverlapping(Entity test) {
		return Sets.intersection(test.getCoords(), entities.keySet()).size() > 0;
	}
	
	private void checkOutOfBounds(Entity test) {
		if (isOutOfBounds(test)){
			throw new IllegalArgumentException("Entity location is out of bounds!");
		}
	}

	private boolean isOutOfBounds(Entity test) {
		return test.getCoords().stream().anyMatch((Coord c) -> c.getCol() < minCol || c.getCol() >= width || 
						                                    c.getRow() < minRow || c.getRow() >= height);
	}
		
	private Set<Entity> getNeighborsWithLambda(Entity subject, Function<Coord, Coord> lambda) {
		return subject.getCoords()
			      .stream()
			      .map(c -> entityAt(lambda.apply(c)))				  
			      .filter(e -> e != subject)
			      .collect(toSet());
	}

	public synchronized void addAll(Collection<? extends Entity> entities) {
		for(Entity e : entities){
			addEntity(e);
		}
	}

	public synchronized Coord leftOf(Entity target, Entity neighbor) {
		int left = Coord.calculateWidth(neighbor.getCoords());
		return Coord.left(target.getBaseCoord(), left);
	}

	public synchronized Coord rightOf(Entity target, Entity neighbor) {
		int right = Coord.calculateWidth(target.getCoords());
		return Coord.right(target.getBaseCoord(), right);
	}

	public synchronized boolean canAdd(Entity entity, Coord offset) {
		return canAdd(entity.moveTo(offset));
	}
	
	public synchronized Set<Coord> getLegalPositions(Entity entity){
		boolean[][] legals = getLegalPositionsGrid(entity);
		Set<Coord> coords = new HashSet<>();
		for(int row = 0; row < legals.length; row++){
			for(int col = 0; col < legals[row].length; col++){
				if(legals[row][col]){
					coords.add(new Coord(col, row));
				}
			}
		}
		return coords;
	}
	
	public synchronized boolean[][] getLegalPositionsGrid(Entity entity){
		boolean [][] positions = new boolean[height][width];
		Entity dummy = entity.moveTo(new Coord(0,0));
		for(int row = 0; row < positions.length; row++){
			for(int col = 0; col < positions[row].length; col++){
				dummy = dummy.moveTo(new Coord(col, row));
				positions[row][col] = canMove(entity, dummy);
			}
		}
		return positions;
	}

}
