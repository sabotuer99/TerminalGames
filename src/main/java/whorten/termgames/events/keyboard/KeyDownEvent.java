package whorten.termgames.events.keyboard;

public class KeyDownEvent implements KeyEvent {

	private String key;
	
	public KeyDownEvent(String key){
		this.key = key;
	}

	@Override
	public KeyEventType getKeyEventType() {
		return KeyEventType.DOWN;
	}

	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return String.format("KeyDownEvent: key: [%s]", key);
	}
}
