package whorten.termgames.games.snake.events;

import whorten.termgames.events.Event;
import whorten.termgames.utils.Coord;

public class HeadMoveEvent implements Event{
	
	private Coord from;
	private Coord to;

	public HeadMoveEvent(Coord from, Coord to){
		this.from = from;
		this.to = to;
	}
	
	public Coord getFrom(){
		return from;
	}
	
	public Coord getTo(){
		return to;
	}
}	

