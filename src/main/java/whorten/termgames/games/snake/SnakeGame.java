package whorten.termgames.games.snake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventListener;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Keys;

public class SnakeGame extends Game {

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
	private GameBorder gb = defaultGameBorder();
	private Fruit goodFruit;
	private Set<Fruit> badFruits;

	@Override
	public void plugIn(GameConsole console) {

		EventListener<KeyDownEvent> listener = (KeyDownEvent k) -> {handleKeyDownEvent(k);};
		console.getEventBus().subscribe(KeyDownEvent.class, listener);
		this.renderer = console.getRenderer();
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString title = new GlyphString.Builder("SNAKE!")
									.withBgColor(BgColor.LIGHT_CYAN)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawAt(4, 67, title);
		
		maxrow = renderer.getCanvasHeight();
		maxcol = renderer.getCanvasWidth() - 21;
		snake = new Snake(console.getEventBus());
		
		run();
		console.getEventBus().unsubscribe(KeyDownEvent.class, listener);
	    soundPlayer.close();
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
		spawnGoodFruit();
		spawnBadFruit();
		
		try {
			while (running && snake.isAlive()) {
				
				Thread.sleep(70);
				if(isLegalMove(direction, snake)){	
					playSlither();
					snake.move(direction);

					//check if snake ate an apple
					if(snake.getHead().equals(goodFruit)){
						snake.eat();
						playNom();
						spawnGoodFruit();
						spawnBadFruit();							
					}
					
					//if snake eats a bad apple, he dies
					if(isBadFruitLocation(snake.getHead())){
						playBleh();
						Thread.sleep(700);
						snake.kill();
					}
					
				} else { // not legal move i.e. hit a wall
					playOof();
					Thread.sleep(700);
					snake.kill();
				}
			}
		} catch (InterruptedException e) {
			
			throw new RuntimeException();
		}	
		soundPlayer.stopMidi();

	}

	private boolean isBadFruitLocation(Coord head) {
		for(Fruit fruit : badFruits){
			if(head.equals(fruit.getCoord())){
				return true;
			}
		}
		return false;
	}

	private void spawnBadFruit() {
		if(goodFruit == null){
			spawnGoodFruit();
		}
		Coord nextCoord = getRandomCoord();
		Coord goodFriutLoc = goodFruit.getCoord();
		while(snake.getOccupiedSet().contains(nextCoord) || nextCoord.equals(goodFriutLoc)){
			nextCoord = getRandomCoord();
		}
		Fruit badFruit = new Fruit(nextCoord, badGlyph);
		badFruits.add(badFruit);
		drawFruit(badFruit);
	}

	private void spawnGoodFruit() {
		Coord nextCoord = getRandomCoord();
		while(snake.getOccupiedSet().contains(nextCoord) || isBadFruitLocation(nextCoord)){
			nextCoord = getRandomCoord();
		}
		goodFruit = new Fruit(nextCoord, gfGlyph);
		drawFruit(goodFruit);
	}



	private void drawFruit(Fruit fruit) {
		Coord loc = fruit.getCoord();
		Glyph glyph = fruit.getGlyph();
		renderer.drawAt(loc.getRow(), loc.getCol(), glyph );
	}

	public boolean isLegalMove(Direction direction, Snake snake){
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
		return new Coord(new Random().nextInt(renderer.getCanvasWidth() - 23) + 2,
                         new Random().nextInt(renderer.getCanvasHeight() - 2) + 2);
	}

	// the "slither" effect gets played every tick, so it's
	// worth the trouble of avoiding reloading it from disk
	// every time
	private byte[] cached = null;
	private void playSlither(){
		try{
			if(cached == null){
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				InputStream slitherSound = classLoader.getResourceAsStream("sounds/slither2.wav");
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(); 
				int nextByte = slitherSound.read(); 
				while(nextByte != -1){ 
					buffer.write(nextByte); 
					nextByte = slitherSound.read(); 
				} 
				cached = buffer.toByteArray();
			}
			
			soundPlayer.play(new ByteArrayInputStream(cached));
		} catch (Exception ex){}
	}
	


	private void playNom(){
	}
	
	private void playBleh() {	
	}
	
	private void playOof() {
	}
	
	private void playMusic(){
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
	
	private GameBorder defaultGameBorder() {
		return new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
							.withFgColor(FgColor.LIGHT_GREEN)
							.withBgColor(0, 0, 255)
							.withDefaultLayout()
							.build();
	}
}
