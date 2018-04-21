package whorten.termgames.games.snake;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventBus;
import whorten.termgames.events.EventListener;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.snake.events.EatFruitEvent;
import whorten.termgames.games.snake.events.HeadMoveEvent;
import whorten.termgames.games.snake.events.TailMoveEvent;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.MidiStartEvent;
import whorten.termgames.sounds.events.MidiStopEvent;
import whorten.termgames.sounds.events.PlaySoundEvent;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Keys;

public class SnakeGame extends Game {

	private final static Logger logger = LogManager.getLogger(SnakeGame.class);
	private static final String SLITHER_SOUND = "sounds/slither2.wav";
	private static final String NOM_SOUND = "sounds/nom.wav";
	private static final String BLEH_SOUND = "sounds/bleh.wav";
	private static final String OOF_SOUND = "sounds/oof.wav";
	private static final String MUSIC_MIDI = "midi/Undertale_-_Sans.mid";
	private volatile boolean running = true;
	private Direction direction = Direction.DOWN;
	private Snake snake;
	private Renderer renderer;
	private int maxcol;
	private int maxrow;
	private Glyph gfGlyph = defaultGoodFruitGlyph();
	private Glyph badGlyph = defaultBadFruitGlyph();
	private GameBorder gb;
	private Map<Coord, Fruit> fruits = new HashMap<>();
	private EventBus eventBus;
	private Glyph bodyGlyph =  defaultBodyGlyph();
	private Glyph headUpGlyph = headSegment("^"); //"▲")
	private Glyph headDownGlyph = headSegment("v"); //"▼")
	private Glyph headRightGlyph = headSegment(">"); //"▶")
	private Glyph headLeftGlyph = headSegment("<"); //"◀")
	EventListener<KeyDownEvent> keyListener;
	EventListener<HeadMoveEvent> headListener;
	EventListener<TailMoveEvent> tailListener;
	EventListener<EatFruitEvent> fruitListener;
	
	@Override
	public void plugIn(GameConsole console) {
		eventBus = console.getEventBus();
		renderer = console.getRenderer();
		renderer.turnOffCursor();
		maxrow = renderer.getCanvasHeight();
		maxcol = renderer.getCanvasWidth() - 21;		
		renderBoard();				
		snake = new Snake(eventBus);
		initializeListeners();
		run();
		removeListeners();
	}
	
	@Override
	public String getDisplayName() {
		return "SNAKE!!!";
	}

	private void removeListeners() {
		eventBus.unsubscribe(KeyDownEvent.class, keyListener);	  
		eventBus.unsubscribe(HeadMoveEvent.class, headListener);
		eventBus.unsubscribe(TailMoveEvent.class, tailListener);
		eventBus.unsubscribe(EatFruitEvent.class, fruitListener);
	}

	private void initializeListeners() {
		keyListener = (KeyDownEvent k) -> {handleKeyDownEvent(k);};
		headListener = (HeadMoveEvent h) -> {handleHeadMoveEvent(h);};
		tailListener = (TailMoveEvent t) -> {handleTailMoveEvent(t);};
		fruitListener = (EatFruitEvent h) -> {handleEatFruitEvent(h);};	
		eventBus.subscribe(KeyDownEvent.class, keyListener);
		eventBus.subscribe(HeadMoveEvent.class, headListener);
		eventBus.subscribe(TailMoveEvent.class, tailListener);
		eventBus.subscribe(EatFruitEvent.class, fruitListener);
	}

	private void renderBoard() {
		renderer.clearScreen();
		gb = defaultGameBorder(renderer);
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString title = new GlyphString.Builder("SNAKE!!!")
									.withBgColor(BgColor.LIGHT_CYAN)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawAt(4, 66, title);
		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
		updateScore(1);
		//                                               ###################
		GlyphString instr1 = menuBuilder.withBaseString("Use the arrow keys ").build();
		GlyphString instr2 = menuBuilder.withBaseString("to change direction").build();
		GlyphString instr3 = menuBuilder.withBaseString("   Eat red apples  ").build();
		GlyphString instr4 = menuBuilder.withBaseString("to grow longer!    ").build();
		GlyphString instr5 = menuBuilder.withBaseString("   But avoid the   ").build();
		GlyphString instr6 = menuBuilder.withBaseString("cans of poison!    ").build();
		renderer.drawAt(8, 61, instr1);
		renderer.drawAt(9, 61, instr2);
		renderer.drawAt(11, 61, instr3);
		renderer.drawAt(11, 61, gfGlyph);
		renderer.drawAt(12, 61, instr4);
		renderer.drawAt(14, 61, instr5);
		renderer.drawAt(14, 61, badGlyph);
		renderer.drawAt(15, 61, instr6);
	}

	private void updateScore(int score) {
		String scoreStr = Integer.toString(score);
		GlyphString scoreGlyph = new GlyphString.Builder(scoreStr)
				.withFgColor(FgColor.LIGHT_YELLOW)
				.build();
		renderer.drawAt(6, 70, scoreGlyph);
	}

	private void handleEatFruitEvent(EatFruitEvent h) {
		Fruit fruit = h.getFruit();
		//check if snake ate an apple
		if(fruit.isGood()){
			fruits.remove(h.getFruit().getCoord());		
			playSound(NOM_SOUND);
			spawnFruit(true);
			spawnFruit(false);
			updateScore(h.getNewSize());
		} else {
			//if snake eats a bad apple, he dies
			playSound(BLEH_SOUND);
			try{ 
				Thread.sleep(700);
			} catch (Exception ex) {
				//gulp
			}
			snake.kill();
		}		
	}

	private void handleTailMoveEvent(TailMoveEvent t) {
		Coord from = t.getFrom();
		renderer.clear(from.getRow(), from.getCol());
	}

	private void handleHeadMoveEvent(HeadMoveEvent h) {
		Direction dir = h.getDirection();
		Coord from = h.getFrom();
		Coord to = h.getTo();
		
		Glyph head = null;
		switch(dir){
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
		
		renderer.drawAt(to.getRow(), to.getCol(), head);
		renderer.drawAt(from.getRow(), from.getCol(), bodyGlyph);		
	}

	private void handleKeyDownEvent(KeyDownEvent ke) {
		
		switch (ke.getKey()) {
		case "Q":
		case "q":
			running = false;
			break;
		case "K":
		case Keys.DOWN_ARROW:
			direction = Direction.DOWN;
			break;
		case "I":
		case Keys.UP_ARROW:
			direction = Direction.UP;
			break;
		case "L":
		case Keys.RIGHT_ARROW:
			direction = Direction.RIGHT;
			break;
		case "J":
		case Keys.LEFT_ARROW:
			direction = Direction.LEFT;
			break;
		default:
			break;
		}
	}

	private void run() {
		
		playMusic();
		spawnFruit(true);
		spawnFruit(false);
		
		try {
			while (running && snake.isAlive()) {
				
				Thread.sleep(70);
				if(isLegalMove(direction, snake)){	
					playSound(SLITHER_SOUND);
					snake.move(direction);

					//check if snake ate an apple
					Fruit fruit = fruits.get(snake.getHead());
					if(fruit != null){
						snake.eat(fruit);
					}
					
				} else { // not legal move i.e. hit a wall
					playSound(OOF_SOUND);
					Thread.sleep(700);
					snake.kill();
				}
			}
		} catch (InterruptedException e) {			
			throw new RuntimeException();
		}	
		stopMusic();
	}

	private void spawnFruit(boolean isGood){
		Glyph glyph = isGood ? gfGlyph : badGlyph;
		Coord nextCoord = getRandomCoord();
		while(snake.getOccupiedSet().contains(nextCoord) ||
				fruits.keySet().contains(nextCoord)){
			nextCoord = getRandomCoord();
		}
		Fruit fruit = new Fruit(nextCoord, glyph, isGood);
		fruits.put(nextCoord, fruit);
		drawFruit(fruit);
	}

	private void drawFruit(Fruit fruit) {
		Coord loc = fruit.getCoord();
		Glyph glyph = fruit.getGlyph();
		renderer.drawAt(loc.getRow(), loc.getCol(), glyph );
	}

	private boolean isLegalMove(Direction direction, Snake snake){
		Coord head = snake.getHead();
		Coord next = new Coord(head.getCol() + direction.getDx(),
	                           head.getRow() + direction.getDy());
		
		// is the snake going out of bounds?
		boolean oob = next.getRow() > 1 && next.getCol() > 1 && 
			   next.getRow() < maxrow && next.getCol() < maxcol;
		
		// is the snake double back on itself?
		boolean selfcollision = snake.getOccupiedSet().contains(next);
		
		return oob && !selfcollision;
	}
	
	private Coord getRandomCoord() {
		return Coord.getRandomCoord(2, 2, maxcol, maxrow);
	}

	private void playSound(String sound){
		eventBus.fire(new PlaySoundEvent(sound));
	}
	
	private void playMusic(){
		eventBus.fire(new MidiStartEvent(MUSIC_MIDI));
	}
	
	private void stopMusic() {
		eventBus.fire(new MidiStopEvent());
	}
	
	private Glyph defaultBadFruitGlyph() {
		return new Glyph.Builder("#")
				.withForegroundColor(FgColor.WHITE)
				.withBackgroundColor(BgColor.CYAN)
				.build();
	}

	private Glyph defaultGoodFruitGlyph() {
		return new Glyph.Builder("O")
		        .withForegroundColor(255, 0, 0)
		        .isBold(true)
		        .build();
	}
	
	private GameBorder defaultGameBorder(Renderer renderer) {
		return new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
							.withFgColor(FgColor.LIGHT_GREEN)
							.withBgColor(0, 0, 255)
							.withDefaultLayout()
							.build();
	}

	private Glyph headSegment(String base) {
		return new Glyph.Builder(base)
				.withForegroundColor(FgColor.GREEN)
				.build();
	}
	
	private Glyph defaultBodyGlyph() {
		return new Glyph.Builder("X")
		        .withForegroundColor(FgColor.LIGHT_YELLOW)
		        .withBackgroundColor(BgColor.GREEN)
		        .build();
	}

}
