package whorten.termgames.games.snake;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.snake.events.EatFruitEvent;
import whorten.termgames.games.snake.events.HeadMoveEvent;
import whorten.termgames.games.snake.events.TailMoveEvent;
import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Direction;


public class Snake {

	private LinkedList<Coord> coords = new LinkedList<>();
	private Set<Coord> coordSet = new HashSet<>();
	private int length = 1;
	private boolean alive = true;
	
	private EventBus eventBus = null;
	
	public Snake(){
		coords.add(new Coord(2,2));
	}
	
	public Snake(EventBus eventBus){
		coords.add(new Coord(2,2));
		this.eventBus = eventBus;
	}

	public void kill(){
		this.alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public Coord getHead(){
		return coords.peekFirst();
	}
	
	public Set<Coord> getOccupiedSet(){
		return coordSet;
	}
	
	public void setEventBus(EventBus eventBus){
		this.eventBus = eventBus;
	}
	
	public void eat(Fruit fruit){
		length++;
		eventBus.fire(new EatFruitEvent(fruit, length));
	}
	
	public void move(Direction direction){

		Coord first = coords.peekFirst();
		Coord last = coords.peekLast();
		Coord next = new Coord(first.getCol() + direction.getDx(),
	                           first.getRow() + direction.getDy());
		coords.addFirst(next);
		coordSet.add(next);

		eventBus.fire(new HeadMoveEvent(first, next, direction));
		
		if(length < coords.size()){			
			coords.removeLast();
			coordSet.remove(last);
			Coord nextLast = coords.peekLast();
			eventBus.fire(new TailMoveEvent(last, nextLast));
		}
	}
}
