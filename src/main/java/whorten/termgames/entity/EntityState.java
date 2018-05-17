package whorten.termgames.entity;

import java.util.Set;

import whorten.termgames.geometry.Coord;

public interface EntityState {
	EntityState moveUp();
	EntityState moveDown();
	EntityState moveLeft();
	EntityState moveRight();
	Set<Coord> getCoords();
}
