package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public interface Entity {

	Coord getBaseCoord();
	Set<Coord> getCoords();
	Entity moveUp();
	Entity moveDown();
	Entity moveLeft();
	Entity moveRight();
	Entity move(Direction direction);
	Entity moveTo(Coord offset);
	GlyphStringCoord getGlyphStringCoord();
}
