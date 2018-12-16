package whorten.termgames.events.keyboard;

import java.io.IOException;

import whorten.termgames.events.EventDriver;

public interface KeyboardEventDriver extends EventDriver<KeyboardEventListener>{

	void listen() throws IOException ;
	boolean isListening();
}
