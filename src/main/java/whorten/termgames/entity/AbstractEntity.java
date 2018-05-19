package whorten.termgames.entity;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;

public abstract class AbstractEntity<K extends AbstractEntity<K,S,B>, 
									 S extends EntityState<S>,
									 B extends EntityBuilder<K,S,B>> implements Entity {

	protected S state;
	
	@Override
	public Set<Coord> getCoords() {
		return new HashSet<>(state.getCoords());
	}

	@Override
	public final K moveUp() {
		return toBuilder().withState(state.moveUp()).build();
	}

	@Override
	public final K moveDown() {
		return toBuilder().withState(state.moveDown()).build();
	}

	@Override
	public final K moveLeft() {
		return toBuilder().withState(state.moveLeft()).build();
	}

	@Override
	public final K moveRight() {
		return toBuilder().withState(state.moveRight()).build();
	}
	
	public abstract B toBuilder();
	

}
