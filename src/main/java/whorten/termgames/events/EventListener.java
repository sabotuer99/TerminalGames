package whorten.termgames.events;

public interface EventListener<K extends Event>{
	void handleEvent(K e);
}
