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
	public final K moveUp() {
		return toBuilder().withState(getState().moveUp()).build();
	}

	@Override
	public final K moveDown() {
		return toBuilder().withState(getState().moveDown()).build();
	}

	@Override
	public final K moveLeft() {
		return toBuilder().withState(getState().moveLeft()).build();
	}

	@Override
	public final K moveRight() {
		return toBuilder().withState(getState().moveRight()).build();
	}
	
	@Override
	public final K move(Direction direction) {
		K next = toBuilder().build();
		switch(direction){
		case UP:
			next = moveUp();
			break;
		case DOWN:
			next = moveDown();
			break;
		case LEFT:
			next = moveLeft();
			break;
		case RIGHT:
			next = moveRight();
			break;
		default:
			break;		
		}
		return next;
	}	
	
	@Override
	public final K moveTo(Coord offset) {
		K next = toBuilder().build();
		Coord base = getState().getBaseCoord();
		int dx = offset.getCol() - base.getCol();
		int dy = offset.getRow() - base.getRow();
		Direction dirx = dx > 0 ? Direction.RIGHT : Direction.LEFT;
		Direction diry = dy > 0 ? Direction.DOWN : Direction.UP;
		for(int i = 0; i < Math.abs(dx); i++){
			next = next.move(dirx);
		}
		for(int i = 0; i < Math.abs(dy); i++){
			next = next.move(diry);
		}
		
		return next;
	}
	
	@Override
	public GlyphStringCoord getGlyphStringCoord() {
		GlyphString gs = new GlyphString.Builder(getState().getBaseGlyph())
				.withBaseString(getState().getBaseString()).build();
		return new GlyphStringCoord(getState().getBaseCoord(), gs);
	}
	
	@Override
	public Coord getBaseCoord() {
		return getState().getBaseCoord();
	}
	
	public abstract B toBuilder();
	
	public abstract S getState();

}
