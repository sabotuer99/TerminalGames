package whorten.termgames.events;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.events.keyboard.KeyEvent;

public class EventBus {

	List<EventListener<KeyEvent>> keyUpListeners = new ArrayList<>();
	List<EventListener<KeyEvent>> keyDownListeners = new ArrayList<>();
	
	public void subscribeKeyUp(EventListener<KeyEvent> listener){
		keyUpListeners.add(listener);
	}
	
	public void unsubscribeKeyUp(EventListener<KeyEvent> listener){
		keyUpListeners.remove(listener);
	}	
	
	public void subscribeKeyDown(EventListener<KeyEvent> listener){
		keyDownListeners.add(listener);
	}
	
	public void unsubscribeKeyDown(EventListener<KeyEvent> listener){
		keyDownListeners.add(listener);
	}
	
	
}
