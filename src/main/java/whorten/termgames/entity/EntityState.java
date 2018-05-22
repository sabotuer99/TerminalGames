package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

public interface EntityState<K extends EntityState<K>> {
	K moveUp();
	K moveDown();
	K moveLeft();
	K moveRight();
	Set<Coord> getCoords();
	String getBaseString();
	Coord getBaseCoord();
	Glyph getBaseGlyph();
}
