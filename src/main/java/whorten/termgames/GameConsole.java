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
import whorten.termgames.utils.TerminalNavigator;

/**
 * Hello world!
 *
 */
public class GameConsole {
	private static TerminalNavigator nav = new TerminalNavigator(System.out);
	private static GameConsole instance = new GameConsole();
	private KeyboardEventDriver ked;
	private InputStream inputSource = System.in;
	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	public static void main(String[] args) throws IOException {
//		reset();		
		instance.ked = new KeyboardEventDriver.Builder()
				.withInputStream(System.in)
				.withListener((KeyEvent ke) -> handleKeyEvent(ke))
				.build();
		instance.runInThreadPool(() -> {
			try { instance.ked.listen();} 	
			catch (IOException e) {}
		});
		
		
		new SnakeGame().plugIn(instance);
		String bodyGlyph =  new Glyph.Builder("◆")
	            .withForegroundColor(FgColor.LIGHT_YELLOW)
	            .withBackgroundColor(BgColor.GREEN)
	            .build()
	            .toString();
		
		for(int row = 0; row < 24; row++){
			for(int col = 0; col < 80; col++){
				drawAt(col,row,bodyGlyph);
			}
		}
		
		instance.ked.die();
		pool.shutdown();
		System.out.println("PEACE!");
	}

	
	private static void drawAt(int x, int y, String payload){
		nav.positionCursor(y, x);
		System.out.print(payload);
		nav.cursorBack();
	}
	
//	public void setInputSource(InputStream in){
//		inputSource = in;
//	}
//	
//	public InputStream getInputSource(){
//		return inputSource;
//	}
	
	public KeyboardEventDriver getKeyboardEventDriver(){
		return ked;
	}
	
	public void runInThreadPool(Runnable runnable){
		pool.execute(runnable);
	}
	
	private static void handleKeyEvent(KeyEvent ke) {
		// ignore up events for now
//		if(ke.getKeyEventType() == KeyEventType.UP) return;
//		
//		switch (ke.getKey()) {
//		case "K":
//		case Keys.DOWN_ARROW:
//			nav.cursorDown();
//			System.out.print(' ');
//			nav.cursorBack();
//			break;
//		case "I":
//		case Keys.UP_ARROW:
//			nav.cursorUp();
//			System.out.print(' ');
//			nav.cursorBack();
//			break;
//		case "L":
//		case Keys.RIGHT_ARROW:
//			nav.cursorForward();
//			System.out.print(' ');
//			nav.cursorBack();
//			break;
//		case "J":
//		case Keys.LEFT_ARROW:
//			nav.cursorBack();
//			System.out.print(' ');
//			nav.cursorBack();
//			break;
//		default:
//			System.out.print(ke.getKey());
//			nav.cursorBack();
//			break;
//		}
	}
	
	private static void reset() {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < 23; row++) {
			for (int col = 0; col < 79; col++) {
				sb.append('#');
			}
			sb.append("\n\r");
		}

		nav.positionCursor(1, 1);
		System.out.print(sb);
		nav.positionCursor(1, 1);
	}
}
