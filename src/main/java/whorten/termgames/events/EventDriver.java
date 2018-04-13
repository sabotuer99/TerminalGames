package whorten.termgames.events;

public interface EventDriver<K extends EventListener<?>> {
	 void subscribe(K listener);
	 void unsubscribe(K listener);
}
