package whorten.termgames.entity;

import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Sets;

import whorten.termgames.utils.Coord;

public class EntityBoard {
	
	private int width;
	private int height;
	private Set<Coord> occupied = new HashSet<>();
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
		occupied.addAll(test.getCoords());		
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
	
	public static class Builder{

		private int width;
		private int height;
		private int minCol = 1;
		private int minRow = 1;
		
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
		if (Sets.intersection(test.getCoords(), occupied).size() > 0){
			throw new IllegalArgumentException("Entity location overlaps with existing entity!");
		}
	}
	
	private void checkOutOfBounds(Entity test) {
		if (test.getCoords().stream().anyMatch((Coord c) -> c.getCol() < minCol || c.getCol() > width || 
						                                    c.getRow() < minRow || c.getRow() > height)){
			throw new IllegalArgumentException("Entity location is out of bounds!");
		}
	}
		
	private Set<Entity> getNeighborsWithLambda(Entity subject, Function<Coord, Coord> lambda) {
		return subject.getCoords()
			      .stream()
			      .map((Coord c) -> entityAt(lambda.apply(c)))				  
			      .filter((Entity e) -> e != subject)
			      .collect(toSet());
	}

}
