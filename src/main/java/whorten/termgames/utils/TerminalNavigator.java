package whorten.termgames.utils;

import java.io.PrintStream;

public class TerminalNavigator {
	
	private final String CSI = "\u001B[";
	private PrintStream out;

	public TerminalNavigator(PrintStream out){
		this.out = out;
	}
	
	public void positionCursor(int x, int y){
		String command = String.format("%s%d;%dH", CSI,x,y);
		out.print(command);
	}
	
	public void cursorUp(){
		String command = String.format("%sA", CSI);
		out.print(command);
	}
	
	public void cursorDown(){
		String command = String.format("%sB", CSI);
		out.print(command);
	}
	
	public void cursorForward(){
		String command = String.format("%sC", CSI);
		out.print(command);
	}
	
	public void cursorBack(){
		String command = String.format("%sD", CSI);
		out.print(command);
	}
}
