package whorten.termgames.events.keyboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import whorten.termgames.events.EventDriver;
import whorten.termgames.utils.Keys;

public class KeyboardEventDriver implements EventDriver<KeyboardEventListener>{	
	private InputStream in;
	private List<Character> stopCharacters;

	public KeyboardEventDriver(InputStream in, List<Character> stopCharacters){
		this.in = in;
		this.stopCharacters = new ArrayList<Character>(stopCharacters);
	}
	
	@Override
	public void subscribe(KeyboardEventListener listener) {
				
	}
	
	public void listen() throws IOException{
		
        Character last = null;
        while(!stopCharacters.contains(last)){           	
        	if ( in.available() != 0 ) {
        		int c = 0xFF;
                c = in.read();
                
                if(c ==  0x1B){
                	// escape character requires special handling
               	
                	c = in.read();
            		if(c == '['){
            			c = in.read();
            		}
            		switch(c){
            		case 'B':
	            	case Keys.DOWN_ARROW_BYTE:

	            		break;
	            	case 'A':
	            	case Keys.UP_ARROW_BYTE:

	            		break;
	            	case 'C':
	            	case Keys.RIGHT_ARROW_BYTE:

	            		break;
	            	case 'D':
	            	case Keys.LEFT_ARROW_BYTE:

	            		break;
            		}
                } else {
                	// every other character
                	
                }
            	
            }
        }
	}
	
}
