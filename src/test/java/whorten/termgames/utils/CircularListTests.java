package whorten.termgames.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class CircularListTests {

	@Test
	public void get_negSize_returnsFirstItem(){
		List<Integer> sut = new CircularList<>();
		sut.add(1);
		sut.add(2);
		sut.add(3);
		
		int result = sut.get(-3);
		
		assertEquals(1, result);
	}
}
