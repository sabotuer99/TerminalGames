package whorten.termgames.games;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventBus;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.MidiStartEvent;
import whorten.termgames.sounds.events.MidiStopEvent;
import whorten.termgames.sounds.events.PlaySoundEvent;

public abstract class Game {

	private final static Logger logger = LogManager.getLogger(Game.class);
	protected EventBus eventBus;
	protected int maxcol;
	protected int maxrow;
	protected Renderer renderer;
	protected GameConsole console;
	protected volatile boolean running;
	
	public abstract void plugIn(GameConsole console);
	public abstract String getDisplayName();
	
	protected void playSound(String sound){
		logger.debug(String.format("Playing sound: %s", sound));
		eventBus.fire(new PlaySoundEvent(sound));
	}
	
	protected void playMusic(String music){
		logger.debug(String.format("Playing midi: %s", music));
		eventBus.fire(new MidiStartEvent(music));
	}
	
	protected void stopMusic() {
		eventBus.fire(new MidiStopEvent());
	}
	
	protected void resetGameState(GameConsole console) {
		this.running = true;
		this.console = console;
		this.eventBus = console.getEventBus();
		this.renderer = console.getRenderer();
		renderer.turnOffCursor();
		this.maxrow = renderer.getCanvasHeight();
		this.maxcol = renderer.getCanvasWidth() - 21;	
	}
}
