package whorten.termgames.games.snake;

import whorten.termgames.GameConsole;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyboardEventListener;
import whorten.termgames.games.Game;

public class SnakeGame extends Game {

	private volatile boolean running = true;

	@Override
	public void plugIn(GameConsole console) {
		KeyboardEventListener listener = (KeyEvent k) -> {handleKeyEvent(k);};
		console.getKeyboardEventDriver().subscribe(listener);		
		run();
		console.getKeyboardEventDriver().unsubscribe(listener);
	}

	private void handleKeyEvent(KeyEvent ke) {
		if(ke.getKeyEventType() == KeyEventType.UP) return;
		
		switch (ke.getKey()) {
		case "Q":
		case "q":
			running = false;
			break;
		default:
			break;
		}
	}

	private void run() {
		while (running) {

		}
	}
}
