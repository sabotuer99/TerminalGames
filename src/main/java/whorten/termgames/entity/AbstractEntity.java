package whorten.termgames.entity;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.geometry.Coord;
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
	public GlyphStringCoord getGlyphStringCoord() {
		GlyphString gs = new GlyphString.Builder(getState().getBaseGlyph())
				.withBaseString(getState().getBaseString()).build();
		return new GlyphStringCoord(getState().getBaseCoord(), gs);
	}
	
	public abstract B toBuilder();
	
	public abstract S getState();

}
