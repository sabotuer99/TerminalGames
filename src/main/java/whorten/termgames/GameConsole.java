package whorten.termgames;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyboardEventDriver;
import whorten.termgames.games.snake.SnakeGame;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.render.OutputStreamRenderer;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.TerminalNavigator;

/**
 * Hello world!
 *
 */
public class GameConsole {
	private static GameConsole instance = new GameConsole();
	private KeyboardEventDriver ked;
	private InputStream inputSource = System.in;
	private static ExecutorService pool = Executors.newCachedThreadPool();
	private Renderer renderer = new OutputStreamRenderer(System.out, 80, 24);
	
	public static void main(String[] args) throws IOException {
		
		instance.ked = new KeyboardEventDriver.Builder()
				.withInputStream(System.in)
				.withListener((KeyEvent ke) -> handleKeyEvent(ke))
				.build();
		instance.runInThreadPool(() -> {
			try { instance.ked.listen();} 	
			catch (IOException e) {}
		});		
		
		new SnakeGame().plugIn(instance);
		Glyph bodyGlyph =  new Glyph.Builder("◆")
	            .withForegroundColor(FgColor.LIGHT_YELLOW)
	            .withBackgroundColor(BgColor.GREEN)
	            .build();
		
		for(int row = 0; row < 24; row++){
			for(int col = 0; col < 80; col++){
				instance.renderer.drawAt(row,col,bodyGlyph);
			}
		}
		
		instance.ked.die();
		pool.shutdown();
		System.out.println("PEACE!");
	}
	
	public KeyboardEventDriver getKeyboardEventDriver(){
		return ked;
	}
	
	public void runInThreadPool(Runnable runnable){
		pool.execute(runnable);
	}
	
	private static void handleKeyEvent(KeyEvent ke) {}

	public Renderer getRenderer() {
		return renderer;
	}
}
