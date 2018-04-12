package whorten.termgames;

import java.io.IOException;

import whorten.termgames.utils.Keys;
import whorten.termgames.utils.TerminalNavigator;

/**
 * Hello world!
 *
 */
public class App 
{
	private static TerminalNavigator nav = new TerminalNavigator(System.out);
	
    public static void main( String[] args ) throws IOException
    {
    	reset();
        int c = 0xFF;
        while(c != 'Q' && c != 'q'){
        	
        	if ( System.in.available() != 0 ) {
                c = System.in.read();
                
            	switch(c){
            	case 0x1B:
            		c = System.in.read();
            		if(c == '['){
            			c = System.in.read();
            		}
            		switch(c){
            		case 'B':
	            	case Keys.DOWN_ARROW_BYTE:
	            		nav.cursorDown();
	            		System.out.print(' ');
	            		nav.cursorBack();
	            		break;
	            	case 'A':
	            	case Keys.UP_ARROW_BYTE:
	            		nav.cursorUp();
	            		System.out.print(' ');
	            		nav.cursorBack();
	            		break;
	            	case 'C':
	            	case Keys.RIGHT_ARROW_BYTE:
	            		nav.cursorForward();
	            		System.out.print(' ');
	            		nav.cursorBack();
	            		break;
	            	case 'D':
	            	case Keys.LEFT_ARROW_BYTE:
	            		nav.cursorBack();
	            		System.out.print(' ');
	            		nav.cursorBack();
	            		break;
            		}
            		break;
            	case 'k':
            		nav.cursorDown();
            		System.out.print(' ');
            		nav.cursorBack();
            		break;
            	case 'i':
            		nav.cursorUp();
            		System.out.print(' ');
            		nav.cursorBack();
            		break;
            	case 'l':
            		nav.cursorForward();
            		System.out.print(' ');
            		nav.cursorBack();
            		break;
            	case 'j':
            		nav.cursorBack();
            		System.out.print(' ');
            		nav.cursorBack();
            		break;
            	case 's':
            		reset();
            		break;
            	default:
            		System.out.print((char)c);
            		System.out.print('X');
            		break;
            	}
            }
        	
        }
        System.out.println("PEACE!");
    }

	private static void reset(){
		StringBuilder sb = new StringBuilder();
		
		for(int row = 0; row < 23; row++){
			for(int col = 0; col < 79; col++){
				sb.append('#');
			}
			sb.append("\n\r");
		}
		
		nav.positionCursor(1, 1);
		System.out.print(sb);
		nav.positionCursor(1, 1);
	}
}
