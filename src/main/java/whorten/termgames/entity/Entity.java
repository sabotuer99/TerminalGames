package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public interface Entity {

	Coord getBaseCoord();
	Set<Coord> getCoords();
	Entity moveUp(int distance);
	Entity moveDown(int distance);
	Entity moveLeft(int distance);
	Entity moveRight(int distance);
	Entity move(Direction direction, int distance);
	Entity moveTo(Coord offset);
	GlyphStringCoord getGlyphStringCoord();
}
