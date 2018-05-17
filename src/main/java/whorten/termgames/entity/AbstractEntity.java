package whorten.termgames.entity;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;

public abstract class AbstractEntity<K extends Entity> implements Entity {

	protected EntityState state;
	
	@Override
	public Set<Coord> getCoords() {
		return new HashSet<>(state.getCoords());
	}

	@Override
	public final Entity moveUp() {
		return toBuilder(this).withState(state.moveUp()).build();
	}

	@Override
	public final Entity moveDown() {
		return toBuilder(this).withState(state.moveDown()).build();
	}

	@Override
	public final Entity moveLeft() {
		return toBuilder(this).withState(state.moveLeft()).build();
	}

	@Override
	public final Entity moveRight() {
		return toBuilder(this).withState(state.moveRight()).build();
	}
	
	public abstract EntityBuilder toBuilder(AbstractEntity<K> abstractEntity);
	

}
