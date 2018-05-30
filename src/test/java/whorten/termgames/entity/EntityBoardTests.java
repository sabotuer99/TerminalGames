package whorten.termgames.entity;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import whorten.termgames.games.tableflipper.board.npc.NPC;
import whorten.termgames.geometry.Coord;

public class EntityBoardTests {

	@Test
	public void sanity(){
		EntityBoard sut = getSut();
		assertNotNull(sut);
	}
	
	@Test
	public void canSetBoardHeightAndWidth(){
		EntityBoard sut = new EntityBoard.Builder()
				.withHeight(26)
				.withWidth(80)
				.build();
		
		assertEquals(80, sut.getWidth());
		assertEquals(26, sut.getHeight());
	}
	
	@Test
	public void addEntityToBoardAtLegalCoordinate(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		Coord coord = new Coord(40,13);
		when(test.getCoords()).thenReturn(Sets.newSet(coord));
		
		sut.addEntity(test);
		
		assertEquals(test, sut.entityAt(coord));
	}
	
	@Test
	public void canRemoveEntity(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		Coord coord = new Coord(40,13);
		when(test.getCoords()).thenReturn(Sets.newSet(coord));
		
		sut.addEntity(test);
		sut.removeEntity(test);
		
		assertNull(sut.entityAt(coord));
	}
	
	@Test
	public void canNotRemoveEntityWithDifferentOverlapingEntity(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		Entity b = getEntity();
		Coord coord = new Coord(40,13);
		when(a.getCoords()).thenReturn(Sets.newSet(coord));
		when(b.getCoords()).thenReturn(Sets.newSet(coord));
		
		sut.addEntity(a);
		sut.removeEntity(b);
		
		assertEquals(a, sut.entityAt(coord));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addEntityOutOfBounds_ThrowException(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		when(test.getCoords()).thenReturn(Sets.newSet(new Coord(120,-23)));
		
		sut.addEntity(test);
	}
	
	@Test
	public void addWideEntity_RetrieveFromAnyOccupiedCell(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		Coord coord1 = new Coord(41,13);
		Coord coord2 = new Coord(42,13);
		Coord coord3 = new Coord(43,13);
		when(test.getCoords()).thenReturn(Sets.newSet(coord1, coord2, coord3));
		
		sut.addEntity(test);
		
		assertEquals(test, sut.entityAt(coord1));
		assertEquals(test, sut.entityAt(coord2));
		assertEquals(test, sut.entityAt(coord3));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addEntityToOccupiedSpace_ThrowException(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		Coord coord1 = new Coord(41,13);
		Coord coord2 = new Coord(42,13);
		Coord coord3 = new Coord(43,13);
		when(test.getCoords()).thenReturn(Sets.newSet(coord1, coord2, coord3));
		
		sut.addEntity(test);
		sut.addEntity(test);
	}
	
	@Test
	public void canAddEntityToOccupiedSpace_ReturnsFalse(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		when(test.getCoords()).thenReturn(Sets.newSet(new Coord(41,13)));
		
		sut.addEntity(test);
		
		assertFalse(sut.canAdd(test));
	}
	
	@Test
	public void canAddEntityOutOfBounds_ReturnsFalse(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		when(test.getCoords()).thenReturn(Sets.newSet(new Coord(120,-13)));
		
		assertFalse(sut.canAdd(test));
	}
	
	@Test
	public void canAdd_NullEntity_ReturnsFalse(){
		EntityBoard sut = getSut();
		
		assertFalse(sut.canAdd(null));
	}
	
	@Test
	public void canAddEntityToBoardAtLegalCoordinate_returnsTrue(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		when(test.getCoords()).thenReturn(Sets.newSet(new Coord(40,13)));
		
		assertTrue(sut.canAdd(test));
	}
	
	@Test
	public void nullEntity_noOp(){
		EntityBoard sut = getSut();
		
		sut.addEntity(null);
		
		assertNull(sut.entityAt(new Coord(40,13)));
	}
	
	@Test
	public void getUpNeighbors_returnsEntityAdjacentAbove(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(41,12), new Coord(42,12)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		
		Set<Entity> n = sut.getUpNeighbors(a);
		
		assertThat(n).contains(b);
		assertThat(n).doesNotContain(a);
	}
	
	@Test
	public void getUpNeighbors_twoEntitiesAbove_returnsBoth(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(41,12)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(42,12)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		sut.addEntity(c);
		
		Set<Entity> n = sut.getUpNeighbors(a);
		
		assertThat(n).containsAllOf(b, c);
		assertThat(n).doesNotContain(a);
	}
	
	@Test
	public void getUpNeighbors_twoEntitiesAbove_OneFarAway_returnsOnlyAdjacent(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(41,12)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(42,10)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		sut.addEntity(c);
		
		Set<Entity> n = sut.getUpNeighbors(a);
		
		assertThat(n).contains(b);
		assertThat(n).containsNoneOf(a, c);
	}
	
	@Test
	public void getDownNeighbors_twoEntitiesBelow_OneFarAway_returnsOnlyAdjacent(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(41,14)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(42,24)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		sut.addEntity(c);
		
		Set<Entity> n = sut.getDownNeighbors(a);
		
		assertThat(n).contains(b);
		assertThat(n).containsNoneOf(a, c);
	}
	
	@Test
	public void getRightNeighbors_twoEntitiesRight_OneFarAway_returnsOnlyAdjacent(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(43,13)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(60,13)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		sut.addEntity(c);
		
		Set<Entity> n = sut.getRightNeighbors(a);
		
		assertThat(n).contains(b);
		assertThat(n).containsNoneOf(a, c);
	}
	
	@Test
	public void getLeftNeighbors_twoEntitiesLeft_OneFarAway_returnsOnlyAdjacent(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(40,13)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(12,13)));
		
		sut.addEntity(a);
		sut.addEntity(b);
		sut.addEntity(c);
		
		Set<Entity> n = sut.getLeftNeighbors(a);
		
		assertThat(n).contains(b);
		assertThat(n).containsNoneOf(a, c);
	}
	
	@Test
	public void canMove_validMove_returnsTrue(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(42,13), new Coord(43,13)));
		
		sut.addEntity(a);

		boolean result = sut.canMove(a, b);
		
		assertThat(result).isTrue();		
	}
	
	@Test
	public void canMove_blockedMove_returnsFalse(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(42,13), new Coord(43,13)));
		Entity c = getEntity();
		when(c.getCoords()).thenReturn(Sets.newSet(new Coord(43,13), new Coord(44,13)));
		
		sut.addEntity(a);
		sut.addEntity(c);

		boolean result = sut.canMove(a, b);
		
		assertThat(result).isFalse();		
	}
	
	@Test
	public void leftOf_validCoord(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getBaseCoord()).thenReturn(new Coord(41,13));
		Entity b = getEntity();
		when(b.getCoords()).thenReturn(Sets.newSet(new Coord(42,13), new Coord(43,13)));
		
		Coord left = sut.leftOf(a, b);
		
		assertThat(left).isEqualTo(new Coord(39,13));
	}
	
	@Test
	public void rightOf_validCoord(){
		EntityBoard sut = getSut();
		Entity a = getEntity();
		when(a.getCoords()).thenReturn(Sets.newSet(new Coord(41,13), new Coord(42,13)));
		when(a.getBaseCoord()).thenReturn(new Coord(41,13));
		Entity b = getEntity();
		
		Coord left = sut.rightOf(a, b);
		
		assertThat(left).isEqualTo(new Coord(43,13));
	}
	
	@Test
	public void getLegalPositionsGrid_worksAsExpected(){
		EntityBoard sut = getSut(3,7);
		Entity a = NPC.newInstance(new Coord(0,0));
		
		boolean[][] result = sut.getLegalPositionsGrid(a);
		boolean[][] expected = {{true, false, false, false, false, false, false},
				                {true, false, false, false, false, false, false},
				                {true, false, false, false, false, false, false}};
		
		drawGrid(result);
		
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				assertThat(result[row][col]).isEqualTo(expected[row][col]);
			}
		}
	}
	
	@Test
	public void getLegalPositionsGrid_LargeBoard_DoesNotFreakOut(){
		EntityBoard sut = getSut(30,80);
		Entity a = NPC.newInstance(new Coord(0,0));
		
		boolean[][] result = sut.getLegalPositionsGrid(a);
		
		drawGrid(result);
		
		for(int row = 0; row < 30; row++){
			for(int col = 0; col < 80; col++){
				if(col <= 73){
					assertThat(result[row][col]).isTrue();					
				} else {
					assertThat(result[row][col]).isFalse();
				}
			}
		}
	}
	
	

	private void drawGrid(boolean[][] result) {
		StringBuilder sb = new StringBuilder();
		for(int row = 0; row < result.length; row++){
			for(int col = 0; col < result[row].length; col++){
				sb.append(result[row][col] ? '.' : 'X');
			}
			sb.append('\n');
		}
		System.out.println(sb);
	}

	private Entity getEntity() {
		return mock(Entity.class);
	}

	private EntityBoard getSut() {
		return new EntityBoard.Builder()
				.withHeight(26)
				.withWidth(80)
				.build();
	}
	
	private EntityBoard getSut(int height, int width) {
		return new EntityBoard.Builder()
				.withHeight(height)
				.withWidth(width)
				.build();
	}
}
