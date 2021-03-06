package whorten.termgames.entity;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public abstract class AbstractEntity<K extends AbstractEntity<K,S,B>, 
									 S extends EntityState<S>,
									 B extends EntityBuilder<K,S,B>> implements Entity {
	
	@Override
	public Set<Coord> getCoords() {
		return new HashSet<>(getState().getCoords());
	}

	@Override
	public final K moveUp(int distance) {
		return toBuilder().withState(getState().moveUp(distance)).build();
	}

	@Override
	public final K moveDown(int distance) {
		return toBuilder().withState(getState().moveDown(distance)).build();
	}

	@Override
	public final K moveLeft(int distance) {
		return toBuilder().withState(getState().moveLeft(distance)).build();
	}

	@Override
	public final K moveRight(int distance) {
		return toBuilder().withState(getState().moveRight(distance)).build();
	}
	
	@Override
	public final K move(Direction direction, int distance) {
		K next = toBuilder().build();
		switch(direction){
		case UP:
			next = moveUp(distance);
			break;
		case DOWN:
			next = moveDown(distance);
			break;
		case LEFT:
			next = moveLeft(distance);
			break;
		case RIGHT:
			next = moveRight(distance);
			break;
		default:
			break;		
		}
		return next;
	}	
	
	@Override
	public final K moveTo(Coord offset) {
		K next = toBuilder().build();
		Coord base = getBaseCoord();
		int dx = offset.getCol() - base.getCol();
		int dy = offset.getRow() - base.getRow();
		Direction dirx = dx > 0 ? Direction.RIGHT : Direction.LEFT;
		Direction diry = dy > 0 ? Direction.DOWN : Direction.UP;
		next = next.move(dirx, Math.abs(dx));
		next = next.move(diry, Math.abs(dy));		
		return next;
	}
	
	@Override
	public GlyphStringCoord getGlyphStringCoord() {
		GlyphString gs = new GlyphString.Builder(getState().getBaseGlyph())
				.withBaseString(getState().getBaseString()).build();
		return new GlyphStringCoord(getBaseCoord(), gs);
	}
	
	@Override
	public Coord getBaseCoord() {
		return getState().getBaseCoord();
	}
	
	public abstract B toBuilder();
	
	public abstract S getState();

}
