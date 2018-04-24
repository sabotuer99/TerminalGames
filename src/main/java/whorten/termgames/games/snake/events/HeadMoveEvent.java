package whorten.termgames.games.snake.events;

import whorten.termgames.events.Event;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Direction;

public class HeadMoveEvent implements Event{
	
	private Coord from;
	private Coord to;
	private Direction direction;

	public HeadMoveEvent(Coord from, Coord to, Direction direction){
		this.from = from;
		this.to = to;
		this.direction = direction;
	}
	
	public Coord getFrom(){
		return from;
	}
	
	public Coord getTo(){
		return to;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	@Override
	public String toString() {
		return String.format("HeadMoveEvent: from: [%s], to: [%s], direction: [%s]", from, to, direction);
	}
}	

