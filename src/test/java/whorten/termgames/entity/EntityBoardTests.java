package whorten.termgames.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import whorten.termgames.utils.Coord;

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
	public void canAddEntityToBoardAtLegalCoordinate(){
		EntityBoard sut = getSut();
		Entity test = getEntity();
		Coord coord = new Coord(40,13);
		when(test.getCoords()).thenReturn(Sets.newSet(coord));
		
		sut.addEntity(test);
		
		assertEquals(test, sut.entityAt(coord));
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

	private Entity getEntity() {
		return mock(Entity.class);
	}

	private EntityBoard getSut() {
		return new EntityBoard.Builder()
				.withHeight(26)
				.withWidth(80)
				.build();
	}
}
