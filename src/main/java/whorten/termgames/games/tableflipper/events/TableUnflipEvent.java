package whorten.termgames.games.tableflipper.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.tableflipper.board.table.Table;

public class TableUnflipEvent implements Event {

	private Table flipped;
	private Table unflipped;

	public TableUnflipEvent(Table flipped, Table unflipped) {
		this.flipped = flipped;
		this.unflipped = unflipped;
	}

	public Table getFlipped() {
		return flipped;
	}

	public Table getUnflipped() {
		return unflipped;
	}

}
