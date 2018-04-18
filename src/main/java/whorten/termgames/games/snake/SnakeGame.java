package whorten.termgames.games.snake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import whorten.termgames.GameConsole;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyboardEventListener;
import whorten.termgames.games.Game;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Keys;
import whorten.termgames.utils.SoundPlayer;

public class SnakeGame extends Game {

	private volatile boolean running = true;
	private Direction direction = Direction.DOWN;
	private Snake snake;
	private Renderer renderer;
	private int maxcol;
	private int maxrow;
	private SoundPlayer soundPlayer = getSoundPlayer();
	private Glyph gfGlyph = new Glyph.Builder("O")
            .withForegroundColor(255, 0, 0)
            .isBold(true)
            .build();
	private Glyph badGlyph = new Glyph.Builder("#")
			.withForegroundColor(FgColor.WHITE)
			.withBackgroundColor(BgColor.CYAN)
			.build();

	@Override
	public void plugIn(GameConsole console) {

		KeyboardEventListener listener = (KeyEvent k) -> {handleKeyEvent(k);};
		console.getKeyboardEventDriver().subscribe(listener);
		this.renderer = console.getRenderer();
		GameBorder gb = new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
							.withFgColor(FgColor.LIGHT_GREEN)
							.withBgColor(0, 0, 255)
							.withDefaultLayout()
							.build();
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString title = new GlyphString.Builder("SNAKE!")
									.withBgColor(BgColor.LIGHT_CYAN)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawAt(4, 67, title);
		
		maxrow = renderer.getCanvasHeight();
		maxcol = renderer.getCanvasWidth() - 21;
		snake = new Snake();
		
		run();
		console.getKeyboardEventDriver().unsubscribe(listener);
	    soundPlayer.close();
	}

	private void handleKeyEvent(KeyEvent ke) {
		if(ke.getKeyEventType() == KeyEventType.UP) return;
		
		//System.out.println("Key down event!");
		
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
		Coord goodFruit = getRandomCoord();
		Coord badFruit = getRandomCoord();
		drawGoodFruit(goodFruit);
		drawBadFruit(badFruit);
		
		Set<Coord> badApples = new HashSet<>();
		badApples.add(badFruit);
		
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
							goodFruit = getRandomCoord();
							while(snake.getOccupiedSet().contains(goodFruit) ||
									badApples.contains(goodFruit)){
								goodFruit = getRandomCoord();
							}
							
							badFruit = getRandomCoord();
							while(snake.getOccupiedSet().contains(badFruit) || 
									badFruit.equals(goodFruit)){
								badFruit = getRandomCoord();
							}
							badApples.add(badFruit);
							drawGoodFruit(goodFruit);
							drawBadFruit(badFruit);
						}
						
						//if snake eats a bad apple, he dies
						if(badApples.contains(snake.getHead())){
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



	private void drawBadFruit(Coord badFruit) {
		renderer.drawAt(badFruit.getRow(), badFruit.getCol(), badGlyph );
	}

	private void drawGoodFruit(Coord goodFruit) {
		renderer.drawAt(goodFruit.getRow(), goodFruit.getCol(), gfGlyph );
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
	
	private SoundPlayer getSoundPlayer() {
		Sequencer sequencer = null;
		try{
			sequencer = MidiSystem.getSequencer();
		} catch(Exception ex){
			// gulp
		}

		return new SoundPlayer.Builder().withSequencer(sequencer).build();
	}

	private void playNom(){
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream nom = classLoader.getResourceAsStream("sounds/nom.wav");
		soundPlayer.play(nom);
	}
	
	private void playBleh() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream bleh = classLoader.getResourceAsStream("sounds/bleh.wav");
		soundPlayer.play(bleh);
	}
	
	private void playOof() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream oof = classLoader.getResourceAsStream("sounds/oof.wav");
		soundPlayer.play(oof);
	}
	
	private void playMusic(){
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream midiFile = classLoader.getResourceAsStream("midi/Undertale_-_Sans.mid");
		soundPlayer.playMidi(midiFile, true);
	}
}
