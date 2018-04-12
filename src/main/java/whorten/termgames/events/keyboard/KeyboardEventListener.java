package whorten.termgames.events.keyboard;

import whorten.termgames.events.EventListener;

public interface KeyboardEventListener extends EventListener<KeyEvent> {

	@Override
	void handleEvent(KeyEvent event);
}
