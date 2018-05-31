package whorten.termgames.games.tableflipper.board.npc;

import static org.junit.Assert.*;

import org.junit.Test;

public class NPCStringsTests {

	@Test
	public void StringsAreAllLength7(){
		assertEquals(7, NPCStrings.RESTORE_LEFT.length());
		assertEquals(7, NPCStrings.RESTORE_RIGHT.length());
		assertEquals(7, NPCStrings.STAND_STILL.length());
		assertEquals(7, NPCStrings.WALK_DOWN.length());
		assertEquals(7, NPCStrings.WALK_LEFT.length());
		assertEquals(7, NPCStrings.WALK_RIGHT.length());
		assertEquals(7, NPCStrings.WALK_UP.length());
	}
}
