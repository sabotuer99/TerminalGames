package whorten.termgames.games.snake;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import whorten.termgames.events.Event;
import whorten.termgames.events.EventBus;
import whorten.termgames.games.snake.events.EatFruitEvent;
import whorten.termgames.games.snake.events.HeadMoveEvent;
import whorten.termgames.games.snake.events.TailMoveEvent;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.TerminalNavigator;


public class Snake {

	private TerminalNavigator nav = new TerminalNavigator(System.out);
	private LinkedList<Coord> coords = new LinkedList<>();
	private Set<Coord> coordSet = new HashSet<>();
	private int length = 1;
	private boolean alive = true;
	
	private EventBus eventBus = null;
	
	public Snake(){
		coords.add(new Coord(2,2));
	}
	
	public Snake(EventBus eventBus){
		coords.add(new Coord(2,2));
		this.eventBus = eventBus;
	}

	public void kill(){
		this.alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public Coord getHead(){
		return coords.peekFirst();
	}
	
	public Set<Coord> getOccupiedSet(){
		return coordSet;
	}
	
	public void setEventBus(EventBus eventBus){
		this.eventBus = eventBus;
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
	
	public void eat(Fruit fruit){
		length++;
		eventBus.fire(new EatFruitEvent(fruit));
	}
	
	public void move(Direction direction){

		Coord first = coords.peekFirst();
		Coord last = coords.peekLast();
		Coord next = new Coord(first.getCol() + direction.getDx(),
	                           first.getRow() + direction.getDy());
		coords.addFirst(next);
		coordSet.add(next);
	
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
		
		
		drawAt(next.getCol(), next.getRow(), head);
		drawAt(first.getCol(), first.getRow(), bodyGlyph);
		
		eventBus.fire(new HeadMoveEvent(first, next));
		
		if(length < coords.size()){			
			drawAt(last.getCol(), last.getRow(), erase);
			coords.removeLast();
			coordSet.remove(last);
			Coord nextLast = coords.peekLast();
			eventBus.fire(new TailMoveEvent(last, nextLast));
		}
		
		drawAt(70,6,Integer.toString(coords.size()));
	}
	
	private void drawAt(int x, int y, String payload){
		nav.positionCursor(y, x);
		System.out.print(payload);
	}
}
