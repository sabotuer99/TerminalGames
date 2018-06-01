package whorten.termgames.games.tableflipper.board.table;

import whorten.termgames.entity.AbstractEntity;
import whorten.termgames.entity.EntityBuilder;
import whorten.termgames.geometry.Coord;

public class Table extends AbstractEntity<Table, TableState, Table.Builder>  {

	private TableState state;

	public static Table newInstance(Coord coord) {
		Table t = new Table();
		t.state = TableState.getStartState(coord);
		return t;
	}
	
	@Override
	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder implements EntityBuilder<Table, TableState, Builder> {
		
		private TableState state;

		public Builder(Table table) {
			state = table.getState();
		}

		@Override
		public Builder withState(TableState state) {
			this.state = state;
			return this;
		}
		
		@Override
		public Table build() {
			Table t = new Table();
			t.state = state;
			return t;
		}
		
	}

	@Override
	public TableState getState() {
		return state;
	}

	public Table unflip() {
		Table t = new Table();
		t.state = state.unflip();
		return t;
	}
	
	public Table flip() {
		Table t = new Table();
		t.state = state.flip();
		return t;
	}

	public boolean isFlipped() {
		return state.isFlipped();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}


}
