package whorten.termgames.games.snake;

import java.util.LinkedList;

import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.TerminalNavigator;


public class Snake {

	private TerminalNavigator nav = new TerminalNavigator(System.out);
	private LinkedList<Coord> coords = new LinkedList<>();
	private int length = 1;
	
	public Snake(){
		coords.add(new Coord(1,1));
	}
	
	private String bodyGlyph =  new Glyph.Builder("X")
            .withForegroundColor(FgColor.LIGHT_YELLOW)
            .withBackgroundColor(BgColor.GREEN)
            .build()
            .toString();
	
	private String headUpGlyph = new Glyph.Builder("▲")
			.withForegroundColor(FgColor.GREEN)
			.build()
			.toString();
	
	private String headDownGlyph = new Glyph.Builder("▼")
			.withForegroundColor(FgColor.GREEN)
			.build()
			.toString();
	
	private String headLeftGlyph = new Glyph.Builder("◀")
			.withForegroundColor(FgColor.GREEN)
			.build()
			.toString();
	
	private String headRightGlyph = new Glyph.Builder("▶")
			.withForegroundColor(FgColor.GREEN)
			.build()
			.toString();
	
	private String erase = " ";
	
	public void eat(){
		length++;
	}
	
	public void move(Direction direction){

		Coord first = coords.peekFirst();
		Coord last = coords.peekLast();
		Coord next = new Coord(first.x + direction.getDx(),
	                           first.y + direction.getDy());
		coords.addFirst(next);
	
		String head = "";
		switch(direction){
		case DOWN:
			head = headDownGlyph;
			break;
		case UP:
			head = headUpGlyph;
			break;
		case LEFT:
			head = headLeftGlyph;
			break;
		case RIGHT:
			head = headRightGlyph;
			break;
		}
		
		
		drawAt(next.x, next.y, head);
		drawAt(first.x, first.y, bodyGlyph);
		
		if(length < coords.size()){			
			drawAt(last.x, last.y, erase);
			coords.removeLast();
		}
		
		drawAt(10,1,Integer.toString(coords.size()));
	}
	
	public class Coord {
		
		Coord(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		int x = 0; //col
		int y = 0; //row
	}
	
	private void drawAt(int x, int y, String payload){
		nav.positionCursor(y, x);
		System.out.print(payload);
	}
	
}
