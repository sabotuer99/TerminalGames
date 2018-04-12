package whorten.termgames;

import java.io.IOException;

import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyboardEventDriver;
import whorten.termgames.utils.Keys;
import whorten.termgames.utils.TerminalNavigator;

/**
 * Hello world!
 *
 */
public class App {
	private static TerminalNavigator nav = new TerminalNavigator(System.out);

	
	public static void main(String[] args) throws IOException {
		reset();
		KeyboardEventDriver ked = new KeyboardEventDriver.Builder()
				.withInputStream(System.in)
				.withListener((KeyEvent ke) -> handleKeyEvent(ke))
				.withStopCharacter('Q')
				.withStopCharacter('q')
				.build();

		ked.listen();
		System.out.println("PEACE!");
	}

	private static void handleKeyEvent(KeyEvent ke) {
		// ignore up events for now
		if(ke.getKeyEventType() == KeyEventType.UP) return;
		
		switch (ke.getKey()) {
		case "K":
		case Keys.DOWN_ARROW:
			nav.cursorDown();
			System.out.print(' ');
			nav.cursorBack();
			break;
		case "I":
		case Keys.UP_ARROW:
			nav.cursorUp();
			System.out.print(' ');
			nav.cursorBack();
			break;
		case "L":
		case Keys.RIGHT_ARROW:
			nav.cursorForward();
			System.out.print(' ');
			nav.cursorBack();
			break;
		case "J":
		case Keys.LEFT_ARROW:
			nav.cursorBack();
			System.out.print(' ');
			nav.cursorBack();
			break;
		default:
			System.out.print(ke.getKey());
			nav.cursorBack();
			break;
		}
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
