package whorten.termgames.games.tableflipper.board.npc;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class NPCTests {

	@Test
	public void moveTo_NPCIsAtTargetPosition(){
		NPC sut = NPC.newInstance(new Coord(0,0));
		
		NPC moved = sut.moveTo(new Coord(20,20));
		
		assertThat(moved.getBaseCoord()).isEqualTo(new Coord(20,20));
	}
	
	@Test
	public void getBaseCoord_notNull(){
		NPC sut = NPC.newInstance(new Coord(0,0));
		
		Coord base = sut.getBaseCoord();
		
		assertThat(base).isNotNull();
		assertThat(base).isEqualTo(new Coord(0,0));
	}
	
	@Test
	public void getGlyphStringCoord_returnsExpected(){
		NPC sut = NPC.newInstance(new Coord(0,0));
		
		NPC moved = sut.moveDown(1).moveRight(1);
		
		GlyphStringCoord gsc = moved.getGlyphStringCoord();
		
		assertThat(gsc.getGlyphString().getBaseString()).isEqualTo(NPCStrings.WALK_RIGHT);
		assertThat(gsc.getCoord()).isEqualTo(new Coord(1,1));
	}
}
