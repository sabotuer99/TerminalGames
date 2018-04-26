package whorten.termgames.games.quadtris;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.animation.events.StartAnimationEvent;
import whorten.termgames.animation.events.StopAllAnimationEvent;
import whorten.termgames.events.EventListener;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.quadtris.events.FullRowsEvent;
import whorten.termgames.games.quadtris.events.ToggleThemeEvent;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceFactory;
import whorten.termgames.games.quadtris.well.Well;
import whorten.termgames.games.quadtris.well.WellRenderer;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.ToggleMusicEvent;
import whorten.termgames.sounds.events.ToggleSoundEvent;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Keys;

public class Quadtris extends Game {

	private final static String THEME_A = "midi/Quadtris_ThemeA.mid";
	private final static String THEME_B = "midi/Quadtris_ThemeB.mid";	
	private String currentTheme = THEME_A;
	private final static Logger logger = LogManager.getLogger(Quadtris.class);
	private EventListener<KeyDownEvent> keyListener;
	private EventListener<ToggleThemeEvent> themeListener;
	private EventListener<FullRowsEvent> fullRowsListener;
	private GameBorder gb;
	private Coord wellOrigin;
	private int level = 0;
	private WellRenderer wellRenderer;
	private Well well;
	private Piece currentPiece;
	private Piece nextPiece;
	private Coord baseOrigin;

	
	@Override
	public void plugIn(GameConsole console) {
		resetGameState(console);
		renderBoard();				
		initializeListeners();
		logger.debug(String.format("Keyboard event driver listening? %b.", console.isKeyboardEventDriverListening()));
		run();
		removeListeners();
	}
	
	@Override
	protected void resetGameState(GameConsole console) {
		super.resetGameState(console);
		this.well = new Well(eventBus);
		this.wellOrigin = new Coord(7,3);
		this.wellRenderer = new WellRenderer.Builder(renderer)
				.withOriginOffset(wellOrigin).build();
				
	}

	@Override
	public String getDisplayName() {
		return "Quadtris";
	}	

	private void run() {
		
		playMusic(currentTheme);
		baseOrigin = new Coord(5,-2);
		currentPiece = PieceFactory.getRandomPiece(baseOrigin);
		nextPiece = PieceFactory.getRandomPiece(baseOrigin);
		wellRenderer.previewPiece(nextPiece);
		
		while (running) {			
			pause(150 - 10* Math.min(level , 10));	
			//check that current piece is not blocked
			if(!well.isOccupied(currentPiece)){
				Piece down = currentPiece.moveDown(1);
				if(!well.isOccupied(down) && !well.isTouchingBottom(currentPiece)){
					wellRenderer.clearPiece(currentPiece);
					currentPiece = down;
					wellRenderer.drawPiece(currentPiece);
				} else {
				    if(well.isLegal(currentPiece)){
				    	getNextPiece();
				    } else {
				    	//not legal, i.e. out the top
				    	running = false;
				    	pause(500);
				    }
				}
			} else {
				//piece overlaps, game over
				running = false;
		    	pause(500);
			}
						
		}
		stopMusic();
		eventBus.fire(new StopAllAnimationEvent());
	}
	
	private void pause(long millis) {
		try{
			Thread.sleep(millis);
		} catch(Exception ex){
			//gulp
		}
	}

	private void renderBoard() {
		logger.debug("Rendering Quadtris board.");
		renderer.clearScreen();
		gb = defaultGameBorder(renderer);
		renderer.drawGlyphCollection(gb.getGlyphCoords());		
		updateScore(0);
		drawMenu();
		updateSound();
		updateTheme();		
		wellRenderer.drawBlockWell();
	}

	private void drawMenu() {
		GlyphString title = new GlyphString.Builder("-={ QuadTris }=-")
				.isBold(true)
				.withBgColor(100,0,200)
				.withFgColor(255,255,200)
				.build();
		renderer.drawAt(4, 62, title);
		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
		//                                               ###################
		GlyphString instr1 = menuBuilder.withBaseString("[← →]  move piece  ").build();
		GlyphString instr2 = menuBuilder.withBaseString("[ ↓ ]  drop piece  ").build();
		GlyphString instr3 = menuBuilder.withBaseString("[ z ] counterclock ").build();
		GlyphString instr4 = menuBuilder.withBaseString("[ x ] clockwise    ").build();
		GlyphString instr5 = menuBuilder.withBaseString("Theme: [T]         ").build();
		GlyphString instr8 = menuBuilder.withBaseString("Music: [M]         ").build();
		GlyphString instr9 = menuBuilder.withBaseString("Sound: [S]         ").build();
		renderer.drawAt(8, 61, instr1);
		renderer.drawAt(9, 61, instr2);
		renderer.drawAt(11, 61, instr3);
		renderer.drawAt(12, 61, instr4);
		renderer.drawAt(17, 61, instr5);
		renderer.drawAt(19, 61, instr8);
		renderer.drawAt(20, 61, instr9);
	}

	private void removeListeners() {
		logger.debug("Removing SnakeGame listeners.");
		eventBus.unsubscribe(KeyDownEvent.class, keyListener);	
		eventBus.unsubscribe(ToggleThemeEvent.class, themeListener);
		eventBus.unsubscribe(FullRowsEvent.class, fullRowsListener);
	}

	private void initializeListeners() {
		logger.debug("Initializing SnakeGame listeners.");
		keyListener = (KeyDownEvent k) -> {handleKeyDownEvent(k);};
		themeListener = (ToggleThemeEvent tte) -> {handleToggleThemeEvent(tte);};
		fullRowsListener = (FullRowsEvent fre) -> {handleFullRowsEvent(fre);};
		eventBus.subscribe(KeyDownEvent.class, keyListener);
		eventBus.subscribe(ToggleThemeEvent.class, themeListener);
		eventBus.subscribe(FullRowsEvent.class, fullRowsListener);
	}
	
	
	private void handleFullRowsEvent(FullRowsEvent fre) {
		List<Integer> rows = fre.getRows();
		eventBus.fire(new StartAnimationEvent(wellRenderer.createLineFlashAnimation(rows)));
		pause(150);
		wellRenderer.drawWellCells(well);
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
			// DROP PIECE
			drop();
			break;
		case "L":
		case Keys.RIGHT_ARROW:
			// MOVE PIECE RIGHT
			moveRight();
			break;
		case "J":
		case Keys.LEFT_ARROW:
			// MOVE PIECE LEFT
			moveLeft();
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
		case "t":
		case "T":
			eventBus.fire(new ToggleThemeEvent());
			break;
		default:
			break;
		}
	}
	
	private void moveLeft() {
		Piece left = currentPiece.moveLeft(1);
		if(!well.isOccupied(left) && well.isInUprights(left)){
				wellRenderer.clearPiece(currentPiece);
				currentPiece = left;
				wellRenderer.drawPiece(currentPiece);
		}
	}

	private void moveRight() {
		Piece right = currentPiece.moveRight(1);
		if(!well.isOccupied(right) && well.isInUprights(right)){
				wellRenderer.clearPiece(currentPiece);
				currentPiece = right;
				wellRenderer.drawPiece(currentPiece);
		}
	}

	private void drop() {
		if(well.isLegal(currentPiece)){
			well.addPiece(currentPiece);
			wellRenderer.drawWellCells(well);
			getNextPiece();
		}	
	}

	private void getNextPiece() {
		currentPiece = nextPiece;
		wellRenderer.drawPiece(currentPiece);
		nextPiece = PieceFactory.getRandomPiece(baseOrigin);
		wellRenderer.previewPiece(nextPiece);
	}

	private void handleToggleThemeEvent(ToggleThemeEvent tte){
		if(THEME_A.equals(currentTheme)){
			currentTheme = THEME_B;
		} else {
			currentTheme = THEME_A;
		}
		stopMusic();
		playMusic(currentTheme);
		updateTheme();
	}
	
	private void updateTheme() {
		GlyphString.Builder theme = new GlyphString.Builder(" ");
		switch(currentTheme){
		case THEME_A:
			theme.withBaseString("Theme A").withFgColor(25,0,255);
			break;
		case THEME_B:
			theme.withBaseString("Theme B").withFgColor(255,25,0);
		}
		renderer.drawAt(17, 72, theme.build());
	}

	private void updateScore(int score) {
		logger.debug("Updating Quadtris score: " + Integer.toString(score));
		String scoreStr = Integer.toString(score);
		GlyphString scoreGlyph = new GlyphString.Builder(scoreStr)
				.withFgColor(FgColor.LIGHT_YELLOW)
				.build();
		renderer.drawAt(6, 70, scoreGlyph);
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
	
	private GameBorder defaultGameBorder(Renderer renderer) {
		return new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
							.withFgColor(FgColor.LIGHT_YELLOW)
							.withBgColor(200, 100, 0)
							.withDefaultLayout()
							.build();
	}

}
