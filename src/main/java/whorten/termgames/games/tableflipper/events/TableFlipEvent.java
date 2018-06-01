package whorten.termgames.games.tableflipper.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.tableflipper.board.table.Table;

public class TableFlipEvent implements Event {

	private Table table;
	
	public TableFlipEvent(Table table) {
		this.table = table;
	}

	public Table getTable(){
		return table;
	}

}
