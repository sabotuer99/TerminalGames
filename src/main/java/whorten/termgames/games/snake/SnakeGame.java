package whorten.termgames.games.snake;

import whorten.termgames.GameConsole;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyboardEventListener;
import whorten.termgames.games.Game;
import whorten.termgames.utils.Keys;

public class SnakeGame extends Game {

	private volatile boolean running = true;
	private Direction direction = Direction.DOWN;
	private Snake snake = new Snake();

	@Override
	public void plugIn(GameConsole console) {
		System.out.println("Starting snake...");
		KeyboardEventListener listener = (KeyEvent k) -> {handleKeyEvent(k);};
		console.getKeyboardEventDriver().subscribe(listener);		
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
				
					Thread.sleep(200);
					snake.move(direction);
					count++;
					if(count % 30 == 0){
						snake.eat();
					}
			}
		} catch (InterruptedException e) {
			
			throw new RuntimeException();
		}			

	}
}
