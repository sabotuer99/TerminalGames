package whorten.termgames.events.keyboard;

import static org.junit.Assert.*;

import org.junit.Test;

import whorten.termgames.events.EventBus;
import whorten.termgames.events.EventListener;

public class EventBusTests {

	
	@Test
	public void registerForType_getEvents(){
		EventBus sut = new EventBus();
		final boolean[] touched = new boolean[1];
		sut.subscribe(KeyDownEvent.class, new EventListener<KeyDownEvent>(){
			@Override
			public void handleEvent(KeyDownEvent e) {
				touched[0] = true;
			}});
		sut.fire(new KeyDownEvent("X"));
		
		assertTrue(touched[0]);
	}
	
	@Test
	public void registerForMultipleType_getEventsForCorrectType(){
		EventBus sut = new EventBus();
		final boolean[] touched = new boolean[2];
		sut.subscribe(KeyDownEvent.class, new EventListener<KeyDownEvent>(){
			@Override
			public void handleEvent(KeyDownEvent e) {
				touched[0] = true;
			}});
		sut.subscribe(KeyUpEvent.class, new EventListener<KeyUpEvent>(){
			@Override
			public void handleEvent(KeyUpEvent e) {
				touched[1] = true;
			}});
		sut.fire(new KeyDownEvent("X"));
		
		assertTrue(touched[0]);
		assertFalse(touched[1]);
	}
	
	@Test
	public void registerMultipleForSameType_allGetEventsForCorrectType(){
		EventBus sut = new EventBus();
		final boolean[] touched = new boolean[2];
		sut.subscribe(KeyDownEvent.class, new EventListener<KeyDownEvent>(){
			@Override
			public void handleEvent(KeyDownEvent e) {
				touched[0] = true;
			}});
		sut.subscribe(KeyDownEvent.class, new EventListener<KeyDownEvent>(){
			@Override
			public void handleEvent(KeyDownEvent e) {
				touched[1] = true;
			}});

		sut.fire(new KeyDownEvent("X"));
		
		assertTrue(touched[0]);
		assertTrue(touched[1]);
	}
}
