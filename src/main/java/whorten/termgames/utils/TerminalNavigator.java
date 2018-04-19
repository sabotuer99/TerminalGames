package whorten.termgames.utils;

import java.io.PrintStream;

public class TerminalNavigator {
	
	private final String CSI = "\u001B[";
	private PrintStream out;

	public TerminalNavigator(PrintStream out){
		this.out = out;
	}
	
	public void positionCursor(int row, int col){
		String command = String.format("%s%d;%dH", CSI, row, col);
		out.print(command);
		out.flush();
	}
	
	public void cursorUp(){
		String command = String.format("%sA", CSI);
		out.print(command);
		out.flush();
	}
	
	public void cursorDown(){
		String command = String.format("%sB", CSI);
		out.print(command);
		out.flush();
	}
	
	public void cursorForward(){
		String command = String.format("%sC", CSI);
		out.print(command);
		out.flush();
	}
	
	public void cursorBack(){
		String command = String.format("%sD", CSI);
		out.print(command);
		out.flush();
	}
	
	public void cursorHide(){
		String command = String.format("%s?25l", CSI);
		out.print(command);
		out.flush();
	}

	public void cursorShow() {
		String command = String.format("%s?25h", CSI);
		out.print(command);
		out.flush();
	}
}
