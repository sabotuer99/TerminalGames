package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;

public interface Entity {

	Set<Coord> getCoords();
	Entity moveUp();
	Entity moveDown();
	Entity moveLeft();
	Entity moveRight();
	
}
