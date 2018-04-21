package whorten.termgames.games.snake.events;

import whorten.termgames.events.Event;
import whorten.termgames.utils.Coord;

public class TailMoveEvent implements Event{
	
	private Coord from;
	private Coord to;

	public TailMoveEvent(Coord from, Coord to){
		this.from = from;
		this.to = to;
	}
	
	public Coord getFrom(){
		return from;
	}
	
	public Coord getTo(){
		return to;
	}
	
	@Override
	public String toString() {
		return String.format("TailMoveEvent: from: [%s], to: [%s]", from, to);
	}
}

