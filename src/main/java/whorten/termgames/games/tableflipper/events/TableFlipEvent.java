package whorten.termgames.games.tableflipper.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.tableflipper.board.table.Table;

public class TableFlipEvent implements Event {

	private Table unflipped;
	private Table flipped;
	
	public TableFlipEvent(Table unflipped, Table flipped) {
		this.unflipped = unflipped;
		this.flipped= flipped;
	}

	public Table getUnflipped(){
		return unflipped;
	}
	
	public Table getFlipped(){
		return flipped;
	}

}
