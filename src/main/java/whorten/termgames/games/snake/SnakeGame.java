package whorten.termgames.games.snake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
		
		GlyphString title = new GlyphString.Builder(4, 67, "SNAKE!")
									.withBgColor(BgColor.LIGHT_CYAN)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawGlyphCollection(title.getGlyphCoords());
		
		maxrow = renderer.getCanvasHeight();
		maxcol = renderer.getCanvasWidth() - 21;
		snake = new Snake(maxrow, maxcol);
		
		run();
		console.getKeyboardEventDriver().unsubscribe(listener);
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
		
		Coord goodFruit = getRandomCoord();
		Coord badFruit = getRandomCoord();
		Glyph gfGlyph = new Glyph.Builder("O")
				              .withForegroundColor(FgColor.RED)
				              .build();
		Glyph badGlyph = new Glyph.Builder("X")
	              .withForegroundColor(FgColor.BLACK)
	              .withBackgroundColor(BgColor.LIGHT_CYAN)
	              .build();
		renderer.drawAt(goodFruit.getRow(), goodFruit.getCol(), gfGlyph );
		renderer.drawAt(badFruit.getRow(), badFruit.getCol(), badGlyph );
		
		Set<Coord> badApples = new HashSet<>();
		badApples.add(badFruit);
		
		try {
			while (running && snake.isAlive()) {
				
					Thread.sleep(70);
					if(snake.isLegalMove(direction)){	
						playSlither();
						snake.move(direction);

						//check if snake ate an apple
						if(snake.getHead().equals(goodFruit)){
							snake.eat();
							playNom();
							goodFruit = getRandomCoord();
							while(snake.getOccupiedSet().contains(goodFruit)){
								goodFruit = getRandomCoord();
							}
							
							badFruit = getRandomCoord();
							while(snake.getOccupiedSet().contains(badFruit)){
								badFruit = getRandomCoord();
							}
							badApples.add(badFruit);
							renderer.drawAt(goodFruit.getRow(), goodFruit.getCol(), gfGlyph );
							renderer.drawAt(badFruit.getRow(), badFruit.getCol(), badGlyph );
						}
						
						//if snake eats a bad apple, he dies
						if(badApples.contains(snake.getHead())){
							snake.kill();
						}
						
					} else {
						snake.kill();
					}
			}
		} catch (InterruptedException e) {
			
			throw new RuntimeException();
		}			

	}

	private Coord getRandomCoord() {
		return new Coord(new Random().nextInt(renderer.getCanvasWidth() - 23) + 2,
                         new Random().nextInt(renderer.getCanvasHeight() - 2) + 2);
	}

	
	private byte[] cached = null;
	private SoundPlayer sp = new SoundPlayer();
	private void playSlither(){
		try{
			if(cached == null){
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				InputStream slitherSound = classLoader.getResourceAsStream("sounds/slither.wav");
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(); 
				int nextByte = slitherSound.read(); 
				while(nextByte != -1){ 
					buffer.write(nextByte); 
					nextByte = slitherSound.read(); 
				} 
				cached = buffer.toByteArray();
			}
			
			sp.play(new ByteArrayInputStream(cached));
		} catch (Exception ex){}
	}
	
	private void playNom(){
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream nom = classLoader.getResourceAsStream("sounds/nom.wav");
		sp.play(nom);
	}
}
