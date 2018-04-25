package whorten.termgames.animation;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import whorten.termgames.GameConsole;

public class AnimationController {

	ConcurrentMap<Animation, Runnable> animations = new ConcurrentHashMap<>();
	
	GameConsole console;
	public AnimationController(GameConsole console){
		this.console = console;
	}
	
	public void playAnimation(final Animation animation){
		Runnable player = new Runnable() {		
			@Override
			public void run() {
				animation.run();
				animations.remove(animation);
			}
		};
		animations.put(animation, player);	
		console.runInThreadPool(player);		
	}
	
	public void stopAnimation(Animation animation){
		animation.stop();
	}
	
	public void stopAllAnimations(){
		for(Animation animation : new HashSet<>(animations.keySet())){
			animation.stop();
		}
	}
}
