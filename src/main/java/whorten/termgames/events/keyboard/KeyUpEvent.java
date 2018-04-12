package whorten.termgames.events.keyboard;

public class KeyUpEvent implements KeyEvent {

	private String key;
	
	public KeyUpEvent(String key){
		this.key = key;
	}

	@Override
	public KeyEventType getKeyEventType() {
		return KeyEventType.UP;
	}

	@Override
	public String getKey() {
		return key;
	}
}
