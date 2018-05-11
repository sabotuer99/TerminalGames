package whorten.termgames.games.snake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.snake.events.EatFruitEvent;
import whorten.termgames.games.snake.events.HeadMoveEvent;
import whorten.termgames.games.snake.events.TailMoveEvent;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
//import whorten.termgames.render.GameBorder;
//import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.ToggleMusicEvent;
import whorten.termgames.sounds.events.ToggleSoundEvent;
import whorten.termgames.utils.Direction;
import whorten.termgames.utils.Keys;

public class SnakeGame extends Game {

	private final static Logger logger = LogManager.getLogger(SnakeGame.class);
	private static final String SLITHER_SOUND = "sounds/slither2.wav";
	private static final String NOM_SOUND = "sounds/nom.wav";
	private static final String BLEH_SOUND = "sounds/bleh.wav";
	private static final String OOF_SOUND = "sounds/oof.wav";
	private static final String MUSIC_MIDI = "midi/Snake_Theme.mid";
	private static final String SNAKE_BACKGROUND_FILE = "whorten/termgames/games/snake/snake_background.gstxt";
	private Direction direction;
	private Snake snake;
	private Glyph gfGlyph = defaultGoodFruitGlyph();
	private Glyph badGlyph = defaultBadFruitGlyph();
	//private GameBorder gb;
	private Map<Coord, Fruit> fruits;
	private Glyph bodyGlyph =  defaultBodyGlyph();
	private Glyph headUpGlyph = headSegment("^"); //"▲")
	private Glyph headDownGlyph = headSegment("v"); //"▼")
	private Glyph headRightGlyph = headSegment(">"); //"▶")
	private Glyph headLeftGlyph = headSegment("<"); //"◀")
	private int speed = 5;
	
	
	@Override
	public void plugIn(GameConsole console) {
		
		resetGameState(console);	
		renderBoard();				
		snake = new Snake(eventBus);
		initializeListeners();
		logger.debug(String.format("Keyboard event driver listening? %b.", console.isKeyboardEventDriverListening()));
		run();
		removeListeners();
	}

	@Override
	protected void resetGameState(GameConsole console) {
		super.resetGameState(console);
		direction = Direction.DOWN;
		fruits = new HashMap<>();	
	}
	
	@Override
	public String getDisplayName() {
		return "SNAKE!!!";
	}

	private void removeListeners() {
		logger.debug("Removing SnakeGame listeners.");
		removeLocalListeners();
	}

	private void initializeListeners() {
		logger.debug("Initializing SnakeGame listeners.");
		addListener(KeyDownEvent.class, this::handleKeyDownEvent);
		addListener(HeadMoveEvent.class, this::handleHeadMoveEvent);
		addListener(TailMoveEvent.class, this::handleTailMoveEvent);
		addListener(EatFruitEvent.class, this::handleEatFruitEvent);	
	}

	private void renderBoard() {
		logger.debug("Rendering SnakeGame board.");
		renderer.clearScreen();
		Set<GlyphStringCoord> background = console.loadFromFile(SNAKE_BACKGROUND_FILE);
		renderer.drawGlyphStringCollection(background);

		updateScore(1);
		updateSpeed();
		updateSound();
	}
	
	private void updateSound() {
		GlyphString sound_off = new GlyphString.Builder("<X ")
				.withFgColor(255,0,0).build();
		GlyphString sound_on = new GlyphString.Builder("<((") 
				.withFgColor(0,255,0).build();
		GlyphString music_off = new GlyphString.Builder("dXb")
				.withFgColor(255,0,0).build();
		GlyphString music_on = new GlyphString.Builder("d⎺b")
				.withFgColor(0,255,0).build();
		
		renderer.drawAt(19, 74, console.isMusicOn() ? music_on : music_off);
		renderer.drawAt(20, 74, console.isSoundOn() ? sound_on : sound_off);	
	}

	private void updateScore(int score) {
		logger.debug("Updating SnakeGame score: " + Integer.toString(score));
		String scoreStr = Integer.toString(score);
		GlyphString scoreGlyph = new GlyphString.Builder(scoreStr)
				.withFgColor(FgColor.LIGHT_YELLOW)
				.build();
		renderer.drawAt(6, 70, scoreGlyph);
	}

	private void handleEatFruitEvent(EatFruitEvent h) {
		logger.debug(String.format("Eat fruit event handler called: ", h));
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
		logger.debug(String.format("Tale move handler called: %s", t));
		Coord from = t.getFrom();
		renderer.clear(from.getRow(), from.getCol());
	}

	private void handleHeadMoveEvent(HeadMoveEvent h) {
		logger.debug(String.format("Head move handler called: %s", h));
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
		logger.debug(String.format("KeyDown handler called: %s", ke.getKey()));
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
		case ">":
		case ".":
			speed = Math.min(speed + 1, 9);
			updateSpeed();
			break;
		case "<":
		case ",":
			speed = Math.max(speed - 1, 1);
			updateSpeed();
			break;
		case "m":
		case "M":
			eventBus.fire(new ToggleMusicEvent());
			updateSound();
			break;
		case "s":
		case "S":
			eventBus.fire(new ToggleSoundEvent());
			updateSound();
			break;
		default:
			break;
		}
	}

	private void updateSpeed() {
		logger.debug("Updating SnakeGame speed: " + Integer.toString(speed));
		String speedStr = Integer.toString(speed);
		GlyphString scoreGlyph = new GlyphString.Builder(speedStr)
				.withFgColor(FgColor.WHITE)
				.build();
		renderer.drawAt(17, 70, scoreGlyph);
	}

	private void run() {
		
		playMusic(MUSIC_MIDI);
		spawnFruit(true);
		spawnFruit(false);
		
		try {
			while (running && snake.isAlive()) {
				
				Thread.sleep(150 - 10*speed);
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
		logger.debug(String.format("Spawning %s fruit...", isGood ? "good" : "bad"));
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
		logger.debug(String.format("Drawing fruit: %s", fruit));
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
