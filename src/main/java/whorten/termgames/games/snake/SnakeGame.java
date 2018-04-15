package whorten.termgames.games.snake;

import whorten.termgames.GameConsole;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyboardEventListener;
import whorten.termgames.games.Game;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Keys;

public class SnakeGame extends Game {

	private volatile boolean running = true;
	private Direction direction = Direction.DOWN;
	private Snake snake;
	private Renderer renderer;
	private int maxcol;
	private int maxrow;

	@Override
	public void plugIn(GameConsole console) {
		System.out.println("Starting snake...");
		KeyboardEventListener listener = (KeyEvent k) -> {handleKeyEvent(k);};
		console.getKeyboardEventDriver().subscribe(listener);
		this.renderer = console.getRenderer();
		GameBorder gb = new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
							.withFgColor(FgColor.LIGHT_GREEN)
							.withDefaultLayout()
							.build();
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString title = new GlyphString.Builder(4, 67, "SNAKE!")
									.withBgColor(BgColor.LIGHT_CYAN)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawGlyphCollection(title.getGlyphCoords());
		
		maxrow = renderer.getCanvasHeight();
		maxcol = renderer.getCanvasWidth() - 21;
		snake = new Snake(maxrow, maxcol);
		
		run();
		console.getKeyboardEventDriver().unsubscribe(listener);
	}

	private void handleKeyEvent(KeyEvent ke) {
		if(ke.getKeyEventType() == KeyEventType.UP) return;
		
		//System.out.println("Key down event!");
		
		switch (ke.getKey()) {
		case "Q":
		case "q":
			running = false;
			break;
		case "K":
		case Keys.DOWN_ARROW:
			direction = Direction.DOWN;
			break;
		case "I":
		case Keys.UP_ARROW:
			direction = Direction.UP;
			break;
		case "L":
		case Keys.RIGHT_ARROW:
			direction = Direction.RIGHT;
			break;
		case "J":
		case Keys.LEFT_ARROW:
			direction = Direction.LEFT;
			break;
		default:
			break;
		}
	}

	private void run() {
		int count = 0;
		try {
			while (running) {
				
					Thread.sleep(70);
					if(snake.isLegalMove(direction)){					
						snake.move(direction);
						count++;
						if(count % 30 == 0){
							snake.eat();
						}
					}
			}
		} catch (InterruptedException e) {
			
			throw new RuntimeException();
		}			

	}
}
