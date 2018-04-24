package whorten.termgames.games.quadtris;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventListener;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.quadtris.events.ToggleThemeEvent;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.ToggleMusicEvent;
import whorten.termgames.sounds.events.ToggleSoundEvent;
import whorten.termgames.utils.BoxDrawingGenerator;
import whorten.termgames.utils.Keys;
import whorten.termgames.utils.StringUtils;

public class Quadtris extends Game {

	private final static String THEME_A = "midi/Quadtris_ThemeA.mid";
	private final static String THEME_B = "midi/Quadtris_ThemeB.mid";	
	private String currentTheme = THEME_A;
	private final static Logger logger = LogManager.getLogger(Quadtris.class);
	EventListener<KeyDownEvent> keyListener;
	EventListener<ToggleThemeEvent> themeListener;
	private GameBorder gb;
	private int speed;
	
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
	public String getDisplayName() {
		return "Quadtris";
	}	

	private void renderBoard() {
		logger.debug("Rendering Quadtris board.");
		renderer.clearScreen();
		gb = defaultGameBorder(renderer);
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString title = new GlyphString.Builder("-={ QuadTris }=-")
									.withBgColor(BgColor.LIGHT_MAGENTA)
									.withFgColor(FgColor.BLACK)
									.build();
		renderer.drawAt(4, 62, title);
		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
		updateScore(1);
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
		updateSound();
		updateTheme();
		
		//Draw inner play area
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 20; i++){
			sb.append("##")
			  .append(StringUtils.repeat(" ", 20))
			  .append("##")
			  .append("\n");
		}
		sb.append(StringUtils.repeat("#", 24)).append("\n");
		//sb.append(StringUtils.repeat("#", 24));
		//String[] lines = new BoxDrawingGenerator().transform(sb.toString().split("\n"));
		String[] lines = sb.toString().split("\n");
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			for(int j = 0; j < lines[0].length(); j++){
				String cell = Character.toString(line.charAt(j));
				if(!" ".equals(cell)){
					Glyph glyph = new Glyph.Builder("╳")
							.isBold(true)
							.withForegroundColor(0,125,255)
							.withBackgroundColor(0,0,255)
							.build();
					renderer.drawAt(3+i, 5+j, glyph);
				}
			}
		}
	}
	


	private void run() {
		
		playMusic(currentTheme);
		
		try {
			while (running) {
				
				Thread.sleep(150 - 10*speed);
			}
		} catch (InterruptedException e) {			
			throw new RuntimeException();
		}	
		stopMusic();
	}

	
	private void removeListeners() {
		logger.debug("Removing SnakeGame listeners.");
		eventBus.unsubscribe(KeyDownEvent.class, keyListener);	
		eventBus.unsubscribe(ToggleThemeEvent.class, themeListener);
	}

	private void initializeListeners() {
		logger.debug("Initializing SnakeGame listeners.");
		keyListener = (KeyDownEvent k) -> {handleKeyDownEvent(k);};
		themeListener = (ToggleThemeEvent tte) -> {handleToggleThemeEvent(tte);};
		eventBus.subscribe(KeyDownEvent.class, keyListener);
		eventBus.subscribe(ToggleThemeEvent.class, themeListener);
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
		// TODO Auto-generated method stub
		
	}

	private void moveRight() {
		// TODO Auto-generated method stub
		
	}

	private void drop() {
		// TODO Auto-generated method stub
		
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
