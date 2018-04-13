package whorten.termgames.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
	
	Map<String, HandlerGroup<?>> map = new HashMap<>();
	
	public <K extends Event> void subscribe(Class<K> c, EventListener<K> e){
		HandlerGroup<K> hg = getHandlerGroup(c.getName());
		hg.addListener(e);
		map.put(c.getName(), hg);
	}
	
	public <K extends Event> void unsubscribe(Class<K> c, EventListener<K> e){
		HandlerGroup<K> hg = getHandlerGroup(e.getClass().getName());
		hg.removeListener(e);
		map.put(c.getName(), hg);
	}
	
	public <K extends Event> void fire(K e){
		HandlerGroup<K> hg = getHandlerGroup(e.getClass().getName());
		hg.fire(e);
	}
	
	private <K extends Event> HandlerGroup<K> getHandlerGroup(String e){
		@SuppressWarnings("unchecked")
		HandlerGroup<K> hg = (HandlerGroup<K>) map.get(e);
		if(hg == null){
			hg = new HandlerGroup<>();
		}
		return hg;
	}
	
	private class HandlerGroup<K extends Event>{

		List<EventListener<K>> listeners = new ArrayList<>();
		
		void addListener(EventListener<K> el){
			listeners.add(el);
		}
		
		void removeListener(EventListener<K> el){
			listeners.remove(el);
		}
		
		void fire(K e){
			for(EventListener<K> handler : listeners){
				handler.handleEvent(e);
			}
		}
	}
}