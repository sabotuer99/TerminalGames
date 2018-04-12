package whorten.termgames.events.keyboard;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.junit.Test;

import whorten.termgames.events.EventListener;
import whorten.termgames.utils.Keys;

public class KeyboardEventDriverTests {

	@Test
	public void firingEvents_A_onInputStream_FiresKeyDownForA() throws IOException{
		//Arrange
		List<KeyEvent> fired = new ArrayList<>();
		KeyboardEventDriver sut = new KeyboardEventDriver.Builder()
						                .withInputStream(getInput("AX"))
						                .withStopCharacter('X')
						                .withListener((KeyEvent ke) -> { fired.add(ke); })
						                .build();
		
		//Act
		sut.listen();
		
		//Assert
		KeyEvent e = fired.get(0);
		assertNotNull(e);
		assertEquals(KeyEventType.DOWN, e.getKeyEventType());
		assertEquals("A", e.getKey());
	}
	
	@Test
	public void firingEvents_a_onInputStream_FiresKeyDownForA() throws IOException{
		//Arrange
		List<KeyEvent> fired = new ArrayList<>();
		KeyboardEventDriver sut = new KeyboardEventDriver.Builder()
						                .withInputStream(getInput("aX"))
						                .withStopCharacter('X')
						                .withListener((KeyEvent ke) -> { fired.add(ke); })
						                .build();
		
		//Act
		sut.listen();
		
		//Assert
		KeyEvent e = fired.get(0);
		assertNotNull(e);
		assertEquals(KeyEventType.DOWN, e.getKeyEventType());
		assertEquals("A", e.getKey());
	}
	
	@Test
	public void firingEvents_HashSymbol_onInputStream_FiresKeyDownSpecialChar() throws IOException{
		//Arrange
		List<KeyEvent> fired = new ArrayList<>();
		KeyboardEventDriver sut = new KeyboardEventDriver.Builder()
						                .withInputStream(getInput("#X"))
						                .withStopCharacter('X')
						                .withListener((KeyEvent ke) -> { fired.add(ke); })
						                .build();
		
		//Act
		sut.listen();
		
		//Assert
		KeyEvent e = fired.get(0);
		assertNotNull(e);
		assertEquals(KeyEventType.DOWN, e.getKeyEventType());
		assertEquals("#", e.getKey());
	}
	
	@Test
	public void firingEvents_UpArrow_onInputStream_FiresKeyDownEscapeSequence() throws IOException{
		//Arrange
		List<KeyEvent> fired = new ArrayList<>();
		KeyboardEventDriver sut = new KeyboardEventDriver.Builder()
						                .withInputStream(getInput(Keys.UP_ARROW + "X"))
						                .withStopCharacter('X')
						                .withListener((KeyEvent ke) -> { fired.add(ke); })
						                .build();
		
		//Act
		sut.listen();
		
		//Assert
		KeyEvent e = fired.get(0);
		assertNotNull(e);
		assertEquals(KeyEventType.DOWN, e.getKeyEventType());
		assertEquals(Keys.UP_ARROW, e.getKey());
	}
	
	@Test
	public void firingEvents_KeyDownWithShortDelay_OneKeyUpEvent() throws IOException{
		//Arrange
		List<KeyEvent> fired = new ArrayList<>();
		KeyboardEventDriver sut = new KeyboardEventDriver.Builder()
						                .withInputStream(getDelayInput("ABBBBBBBBBBBBBBBBBBBBBX", 10))
						                .withRepeatThreshold(100)
						                .withStopCharacter('X')
						                .withListener((KeyEvent ke) -> { 
						                	if(ke.getKeyEventType() == KeyEventType.UP)
						                		fired.add(ke); 
						                })
						                .build();
		//Act
		sut.listen();
		
		assertEquals(1, fired.size());
		KeyEvent e = fired.get(0);
		assertNotNull(e);
		assertEquals(KeyEventType.UP, e.getKeyEventType());
		assertEquals("A", e.getKey());
	}

	private InputStream getInput(String input) {
		return new ByteArrayInputStream(input.getBytes());
	}
	
	private InputStream getDelayInput(String input, long delay) {
		DelayQueueInputStream stream = new DelayQueueInputStream(delay);
		for(byte raw : input.getBytes()){
			stream.write((char)raw);
		}
		return stream;
	}
	
	class DelayQueueInputStream extends InputStream{

		private Deque<Character> queue = new ArrayDeque<>(); 
		private long delay;
		
		public DelayQueueInputStream(long delay) {
			this.delay = delay;
		}
		
		@Override
		public int read() throws IOException {
			try {
				Thread.sleep(delay);
				return (char)queue.poll();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException(e);
			}			
		}
		
		@Override
		public int available(){
			return queue.size();
		}
		
		public void write(Character c){
			queue.add(c);
		}
	}
}
