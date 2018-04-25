package whorten.termgames.animation.events;

import whorten.termgames.animation.Animation;
import whorten.termgames.events.Event;

public class StopAnimationEvent implements Event {
	private Animation animation;
	
	public StopAnimationEvent(Animation animation){
		this.animation = animation;
	}
	
	public Animation getAnimation(){
		return animation;
	}
}
