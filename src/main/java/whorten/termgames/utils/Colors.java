package whorten.termgames.utils;

public class Colors {

	public static final String RESET_ALL = "\u001B[0m";

	public static String foreground(int r, int g, int b){
		int offset = getOffset(r,g,b);
		return String.format("\u001B[38;5;%sm", offset);
	}
	
	public static String background(int r, int g, int b){
		int offset = getOffset(r,g,b);
		return String.format("\u001B[48;5;%sm", offset);
	}
	
	private static int getOffset(int r, int g, int b){		
		
		//Get color offset
		int interval = 5100;
		int rOff = (r * 100 + 2550) / interval;
		int gOff = (g * 100 + 2550) / interval;
		int bOff = (b * 100 + 2550) / interval;
		
		int offset = rOff * 36 + gOff * 6 + bOff + 16;
		
	    if(offset == 16){
	    	int val = Math.max(Math.max(r,g), b);
			offset = 232 + (val + 3)/5;
		} 
		
		return offset;
	}
}
