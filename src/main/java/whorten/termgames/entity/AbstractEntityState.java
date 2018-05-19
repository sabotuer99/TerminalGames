package whorten.termgames.entity;

import java.util.Set;
import java.util.TreeSet;

import whorten.termgames.geometry.Coord;

public abstract class AbstractEntityState<K extends EntityState<K>> 
	implements EntityState<K>{

	protected Set<Coord> coords = new TreeSet<>();
	
	@Override
	public abstract K moveUp();

	@Override
	public abstract K moveDown();

	@Override
	public abstract K moveLeft();

	@Override
	public abstract K moveRight();

	@Override
	public Set<Coord> getCoords() {
		return new TreeSet<>(coords);
	}

}
