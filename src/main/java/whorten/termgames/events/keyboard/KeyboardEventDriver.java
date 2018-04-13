package whorten.termgames.events.keyboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import whorten.termgames.events.EventDriver;
import whorten.termgames.events.EventListener;
import whorten.termgames.utils.Keys;

public class KeyboardEventDriver implements EventDriver<KeyboardEventListener>{	
	private InputStream in;
	private List<String> stops;
	private List<KeyboardEventListener> listeners = new ArrayList<>();
	private int repeatThreshold;	
	
	private KeyboardEventDriver(){}
	
	@Override
	public void subscribe(KeyboardEventListener listener) {
	    listeners.add(listener);
	}
	
	@Override
	public void unsubscribe(KeyboardEventListener listener) {
		listeners.remove(listener);
	}
	
	public void listen() throws IOException{
		
        String last = null;
        while(!stops.contains(last) && !die){           	
        	if ( in.available() != 0 ) {
                int c = in.read();
                
                if(c ==  0x1B){
                	// escape character requires special handling              	
                	c = in.read();
            		if(c == '['){
            			c = in.read();
            		}
            		switch(c){
            		case 'B':
	            	case Keys.DOWN_ARROW_BYTE:
	            		last = Keys.DOWN_ARROW;
	            		break;
	            	case 'A':
	            	case Keys.UP_ARROW_BYTE:
	            		last = Keys.UP_ARROW;
	            		break;
	            	case 'C':
	            	case Keys.RIGHT_ARROW_BYTE:
	            		last = Keys.RIGHT_ARROW;
	            		break;
	            	case 'D':
	            	case Keys.LEFT_ARROW_BYTE:
	            		last = Keys.LEFT_ARROW;	
	            		break;
            		}            		           		
                } else {
                	// every other character, uppered
                	last = Character.toString((char)c);          
                } 
                
                KeyEvent k = new KeyDownEvent(last.toString().toUpperCase()); 
                fire(k);
                checkForKeyUp(last);              
            }
        }
	}
	
	private Map<String,Long> lastSeen = new HashMap<>();
	private volatile boolean die;

	private void checkForKeyUp(String last) {
		Long timeSeen = System.currentTimeMillis();
		for(String key : new HashSet<String>(lastSeen.keySet())){			
			Long diff = timeSeen - lastSeen.get(key);
			if(diff > repeatThreshold){
				KeyEvent ku = new KeyUpEvent(key);
				fire(ku);
				lastSeen.remove(key);
			}		
		}		
		lastSeen.put(last,timeSeen);
	}

	private void fire(KeyEvent k){
		for(EventListener<KeyEvent> listener : listeners){
            listener.handleEvent(k);
		}
	}
	
	public static class Builder{
		private InputStream in;
		private List<String> stops = new ArrayList<>();
		private List<KeyboardEventListener> listeners = new ArrayList<>();
		private int threshold = 100;
		
		public Builder withListener(KeyboardEventListener listener){
			listeners.add(listener);
			return this;
		}
		
		public Builder withStopCharacter(Character stopCharacter){
			stops.add(stopCharacter.toString());
			return this;
		}
		
		public Builder withStopSequence(String stopSequence){
			stops.add(stopSequence);
			return this;
		}
		
		public Builder withInputStream(InputStream in){
			this.in = in;
			return this;
		}
		
		public KeyboardEventDriver build(){
			KeyboardEventDriver instance = new KeyboardEventDriver();
			instance.in = this.in;
			instance.stops = new ArrayList<>(stops);
			instance.listeners = new ArrayList<>(listeners);
			instance.repeatThreshold = threshold;
			return instance;
		}

		public Builder withRepeatThreshold(int threshold) {
			this.threshold  = threshold;
			return this;
		}
	}

	public void die() {
		die = true;	
	}

}
