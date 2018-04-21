package whorten.termgames.events.keyboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.events.EventDriver;
import whorten.termgames.events.EventListener;
import whorten.termgames.utils.Keys;

public class KeyboardEventDriver implements EventDriver<KeyboardEventListener>{	
	
	private final static Logger logger = LogManager.getLogger(KeyboardEventDriver.class);
	private InputStream in;
	private List<String> stops;
	private List<KeyboardEventListener> listeners = new ArrayList<>();
	private int repeatThreshold;	
	private volatile boolean isListening = false;
	
	private KeyboardEventDriver(){}
	
	@Override
	public void subscribe(KeyboardEventListener listener) {
		logger.debug("Adding subscriber to Keyboard Events.");
	    listeners.add(listener);
	}
	
	@Override
	public void unsubscribe(KeyboardEventListener listener) {
		logger.debug("Removing subscriber to Keyboard Events.");
		listeners.remove(listener);
	}
	
	public boolean isListening(){
		return isListening;
	}
	
	public void listen() throws IOException{
		isListening = true;
        String last = null;
        try {
	        while(!stops.contains(last) && !die){ 
	        	
					Thread.sleep(10);
				
	        	if ( in.available() != 0 ) {
	        		logger.debug("New input being processed...");
	                int c = in.read();
	                
	                if(c ==  0x1B){
	                	// escape character requires special handling              	
	                	c = in.read();
	                	int d = 0;
	            		if(c == '['){
	            			d = in.read();
	            		}
	            		switch(d){
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
		            	case 13:
		            		last = Keys.ENTER;
		            		break;
		            	default:
		            		last = String.valueOf((char)c) + String.valueOf((char)d);
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
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        isListening = false;
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
		logger.debug(String.format("Firing keyboard event: %s", k));
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
