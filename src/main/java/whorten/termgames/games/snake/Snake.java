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
	private int maxrow;
	private int maxcol;
	
	public Snake(int maxrow, int maxcol){
		coords.add(new Coord(2,2));
		this.maxrow = maxrow;
		this.maxcol = maxcol;
	}
	
	public boolean isLegalMove(Direction direction){
		Coord first = coords.peekFirst();
		Coord next = new Coord(first.col + direction.getDx(),
	                           first.row + direction.getDy());
		
		return next.row > 1 && next.col > 1 && 
			   next.row < maxrow && next.col < maxcol;
	}
	
	private String bodyGlyph =  new Glyph.Builder("X")
            .withForegroundColor(FgColor.LIGHT_YELLOW)
            .withBackgroundColor(BgColor.GREEN)
            .build()
            .toString();
	
	private String headUpGlyph = headSegment("^"); //"▲")
	private String headDownGlyph = headSegment("v"); //"▼")
	private String headRightGlyph = headSegment(">"); //"▶")
	private String headLeftGlyph = headSegment("<"); //"◀")
	
	private String headSegment(String base) {
		return new Glyph.Builder(base)
				.withForegroundColor(FgColor.GREEN)
				.build()
				.toString();
	}
	
	private String erase = " ";
	
	public void eat(){
		length++;
	}
	
	public void move(Direction direction){

		Coord first = coords.peekFirst();
		Coord last = coords.peekLast();
		Coord next = new Coord(first.col + direction.getDx(),
	                           first.row + direction.getDy());
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
		
		
		drawAt(next.col, next.row, head);
		drawAt(first.col, first.row, bodyGlyph);
		
		if(length < coords.size()){			
			drawAt(last.col, last.row, erase);
			coords.removeLast();
		}
		
		drawAt(10,1,Integer.toString(coords.size()));
	}
	
	public class Coord {
		
		Coord(int col, int row){
			this.col = col;
			this.row = row;
		}
		
		int col = 0; //col
		int row = 0; //row
	}
	
	private void drawAt(int x, int y, String payload){
		nav.positionCursor(y, x);
		System.out.print(payload);
	}
	
}
