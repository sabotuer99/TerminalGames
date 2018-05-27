package whorten.termgames.games.tableflipper.board.table;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;

public class Table extends AbstractEntity<Table, TableState, Table.Builder>  {

	@Override
	public Builder toBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class Builder implements EntityBuilder<Table, TableState, Builder> {
		
		@Override
		public Builder withState(TableState state) {
			return null;
		}
		
		@Override
		public Table build() {
			return null;
		}
		
	}

	@Override
	public TableState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public Table unflip() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Table flip() {
		// TODO Auto-generated method stub
		return null;
	}
}
