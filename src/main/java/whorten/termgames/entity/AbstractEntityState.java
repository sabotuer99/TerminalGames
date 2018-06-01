package whorten.termgames.entity;

import java.util.Set;
import java.util.TreeSet;

import whorten.termgames.geometry.Coord;

public abstract class AbstractEntityState<K extends EntityState<K>> 
	implements EntityState<K>{

	protected Set<Coord> coords = new TreeSet<>();
	
	@Override
	public abstract K moveUp(int distance);

	@Override
	public abstract K moveDown(int distance);

	@Override
	public abstract K moveLeft(int distance);

	@Override
	public abstract K moveRight(int distance);

	@Override
	public Set<Coord> getCoords() {
		return new TreeSet<>(coords);
	}
	
	@Override
	public Coord getBaseCoord() {
		return getCoords().iterator().next();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
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
		AbstractEntityState<?> other = (AbstractEntityState<?>) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		return true;
	}

}
