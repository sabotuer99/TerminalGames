package whorten.termgames.games.tableflipper.board;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

import whorten.termgames.events.EventBus;
public class TableFlipperBoardTests {

	private EventBus eventbus;
	
	@Before
	public void setup(){
		eventbus = new EventBus();
	}
	
	@Test
	public void builder_DefaultSettings(){
		TableFlipperBoard sut = new TableFlipperBoard.Builder(eventbus).build();
		
		assertThat(sut).isNotNull();
	}
	
	@Test
	public void builder_AddPlayer(){
		TableFlipperBoard sut = new TableFlipperBoard.Builder(eventbus).build();
		sut.movePlayerDown(100);
		
		assertThat(sut).isNotNull();
	}
	
	@Test
	public void tick_doesNotExplode(){
		TableFlipperBoard sut = new TableFlipperBoard.Builder(eventbus).build();
		sut.tick(100);
		sut.tick(500);
		
		assertThat(sut).isNotNull();
	}
}
