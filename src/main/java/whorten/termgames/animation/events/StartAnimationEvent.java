package whorten.termgames.animation.events;

import whorten.termgames.animation.Animation;
import whorten.termgames.events.Event;

public class StartAnimationEvent implements Event{

	private Animation animation;
	
	public StartAnimationEvent(Animation animation){
		this.animation = animation;
	}
	
	public Animation getAnimation(){
		return animation;
	}
}
