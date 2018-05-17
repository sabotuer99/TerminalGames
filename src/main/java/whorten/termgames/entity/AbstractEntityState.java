package whorten.termgames.entity;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;

public abstract class AbstractEntityState implements EntityState{

	Set<Coord> coords = new HashSet<>();
	
	@Override
	public abstract EntityState moveUp();

	@Override
	public abstract EntityState moveDown();

	@Override
	public abstract EntityState moveLeft();

	@Override
	public abstract EntityState moveRight();

	@Override
	public Set<Coord> getCoords() {
		return new HashSet<>(coords);
	}

}
