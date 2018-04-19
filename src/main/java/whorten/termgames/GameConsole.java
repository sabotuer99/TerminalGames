package whorten.termgames;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import whorten.termgames.events.EventBus;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyUpEvent;
import whorten.termgames.events.keyboard.KeyboardEventDriver;
import whorten.termgames.games.snake.SnakeGame;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.render.OutputStreamRenderer;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.SoundPlayer;
import whorten.termgames.sounds.events.PlaySoundEvent;
import whorten.termgames.utils.TerminalNavigator;

/**
 * Hello world!
 *
 */
public class GameConsole {
	private static GameConsole instance = new GameConsole();
	private KeyboardEventDriver ked;
	private static ExecutorService pool = Executors.newCachedThreadPool();
	private Renderer renderer = new OutputStreamRenderer(System.out, 80, 24);
	private final EventBus eventBus = new EventBus();
	private final SoundPlayer soundPlayer = getSoundPlayer();
	private ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	
	public static void main(String[] args) throws IOException {
		
		initEventSystem();
		
		instance.runInThreadPool(() -> {
			try { instance.ked.listen();} 	
			catch (IOException e) {}
		});	
				
		for(int i = 0; i < 5; i++){
			instance.renderer.clearScreen();
			new SnakeGame().plugIn(instance);
		}
		
		Glyph bodyGlyph =  new Glyph.Builder("◆")
	            .withForegroundColor(FgColor.LIGHT_YELLOW)
	            .withBackgroundColor(BgColor.GREEN)
	            .build();
		
		for(int row = 1; row <= 24; row++){
			for(int col = 1; col <= 80; col++){
				instance.renderer.drawAt(row,col,bodyGlyph);
			}
		}
		
		instance.ked.die();
		pool.shutdown();
	}

	private static void initEventSystem() {
		instance.ked = new KeyboardEventDriver.Builder()
				.withInputStream(System.in)
				.withListener((KeyEvent ke) -> instance.handleKeyEvent(ke))
				.build();
		
		instance.eventBus.subscribe(PlaySoundEvent.class, 
				(PlaySoundEvent pse) -> {instance.handlePlaySoundEvent(pse);});
	}
	
	private void handlePlaySoundEvent(PlaySoundEvent pse) {
		String path = pse.getPath();
		InputStream soundFile = classLoader.getResourceAsStream(path);
		instance.soundPlayer.play(soundFile);
	}

	public EventBus getEventBus(){
		return this.eventBus;
	}
	
	public void runInThreadPool(Runnable runnable){
		pool.execute(runnable);
	}
	
	
	private void handleKeyEvent(KeyEvent ke) {
		if(ke.getKeyEventType() == KeyEventType.UP){
			eventBus.fire((KeyUpEvent) ke);
		}
		if(ke.getKeyEventType() == KeyEventType.DOWN){
			eventBus.fire((KeyDownEvent) ke);
		}
	}

	public Renderer getRenderer() {
		return renderer;
	}
	
	private SoundPlayer getSoundPlayer() {
		Sequencer sequencer = null;
		try{
			sequencer = MidiSystem.getSequencer();
		} catch(Exception ex){
			// gulp
		}

		return new SoundPlayer.Builder().withSequencer(sequencer).build();
	}
}
