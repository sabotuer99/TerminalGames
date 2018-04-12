package whorten.termgames.events.keyboard;

import whorten.termgames.events.Event;

public interface KeyEvent extends Event{
	KeyEventType getKeyEventType();
	String getKey();
}
