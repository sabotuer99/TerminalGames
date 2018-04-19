package whorten.termgames.sounds.events;

import whorten.termgames.events.Event;

public class MidiStartEvent implements Event {

	private String path;

	public MidiStartEvent(String path){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
}
