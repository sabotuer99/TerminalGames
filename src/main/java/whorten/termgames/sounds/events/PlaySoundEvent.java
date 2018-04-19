package whorten.termgames.sounds.events;

import whorten.termgames.events.Event;

public class PlaySoundEvent implements Event  {
	
	private String path;

	public PlaySoundEvent(String path){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
}
