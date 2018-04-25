package whorten.termgames.games.quadtris.events;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.events.Event;

public class FullRowsEvent implements Event{

	private List<Integer> rows;
	
	public FullRowsEvent(List<Integer> rows){
		rows = new ArrayList<>(rows);
	}
	
	public List<Integer> getRows(){
		return new ArrayList<>(rows);
	}
}
