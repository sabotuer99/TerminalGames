package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

public interface EntityState<K extends EntityState<K>> {
	K moveUp(int distance);
	K moveDown(int distance);
	K moveLeft(int distance);
	K moveRight(int distance);
	Set<Coord> getCoords();
	String getBaseString();
	Coord getBaseCoord();
	Glyph getBaseGlyph();
}
