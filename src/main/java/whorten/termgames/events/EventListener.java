package whorten.termgames.events;

public interface EventListener<K> {
	void handleEvent(K event);
}
