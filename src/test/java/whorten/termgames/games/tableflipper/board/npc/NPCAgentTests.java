package whorten.termgames.games.tableflipper.board.npc;

import org.mockito.*;
import org.junit.Test;

import whorten.termgames.entity.EntityBoard;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.tableflipper.board.TableFlipperBoard;
import whorten.termgames.games.tableflipper.board.table.Table;
import whorten.termgames.geometry.Coord;

public class NPCAgentTests {

	@Test
	public void tick_timeIntervalTooShort_DoesNothing(){
		EntityBoard eb = new EntityBoard.Builder()
				.withHeight(10)
				.withWidth(20)
				.build();
		TableFlipperBoard tbf = Mockito.mock(TableFlipperBoard.class);
		EventBus eventbus = new EventBus();
		NPC npc = NPC.newInstance(new Coord(0,0));
		NPCAgent sut = new NPCAgent.Builder(eb, npc)
				.withEventBus(eventbus)
				.withTableFlipperBoard(tbf)
				.build();
		
		Table table = Table.newInstance(new Coord(7,0)).flip();
		eb.addEntity(table);	
		sut.addTable(table);
		
		sut.tick(1000);
		sut.tick(2000);
	}
}
