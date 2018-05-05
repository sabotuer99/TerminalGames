package whorten.termgames.games.quadtris;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.animation.events.StartAnimationEvent;
import whorten.termgames.animation.events.StopAllAnimationEvent;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.quadtris.board.QuadtrisBoard;
import whorten.termgames.games.quadtris.events.FullRowsEvent;
import whorten.termgames.games.quadtris.events.RedrawPieceEvent;
import whorten.termgames.games.quadtris.events.RedrawPieceEvent.PieceTransformType;
import whorten.termgames.games.quadtris.events.SpawnPieceEvent;
import whorten.termgames.games.quadtris.events.ToggleThemeEvent;
import whorten.termgames.games.quadtris.piece.PieceFactory;
import whorten.termgames.games.quadtris.well.WellRenderer;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.PlaySoundEvent;
import whorten.termgames.sounds.events.ToggleMusicEvent;
import whorten.termgames.sounds.events.ToggleSoundEvent;
import whorten.termgames.utils.Keys;

public class Quadtris extends Game {

	private final static String THEME_A = "midi/Quadtris_ThemeA.mid";
	private final static String THEME_B = "midi/Quadtris_ThemeB.mid";	
	private static final String BEEP_SOUND = "sounds/beep.wav";
	private static final String BOOP_SOUND = "sounds/boop.wav";
	private static final String THUNK_SOUND = "sounds/thunk.wav";
	private static final String TRIBEEP_SOUND = "sounds/three_boop.wav";
	private String currentTheme = THEME_A;
	private final static Logger logger = LogManager.getLogger(Quadtris.class);
	private WellRenderer wellRenderer;
	private GameBorder gb;
	private Coord wellOrigin;
	private int level = 0;
	private int score;
	private int lines;
	private Map<String, Glyph> minis = PieceFactory.getNameMiniMap();
	private QuadtrisBoard quadtrisBoard;
	
	@Override
	public void plugIn(GameConsole console) {
		resetGameState(console);
		renderBoard();				
		initializeListeners();
		logger.debug(String.format("Keyboard event driver listening? %b.", 
				console.isKeyboardEventDriverListening()));
		run();
		removeLocalListeners();
	}
	
	@Override
	protected void resetGameState(GameConsole console) {
		super.resetGameState(console);
		this.quadtrisBoard = new QuadtrisBoard.Builder(eventBus).build();
		this.wellOrigin = new Coord(7,3);
		this.wellRenderer = new WellRenderer.Builder(renderer)
				.withOriginOffset(wellOrigin).build();
		this.score = 0;
				
	}

	@Override
	public String getDisplayName() {
		return "Quadtris";
	}	

	private void run() {
		
		playMusic(currentTheme);
		wellRenderer.drawPiece(quadtrisBoard.getCurrentPiece());
		wellRenderer.previewPiece(quadtrisBoard.getNextPiece());
		updateStats();
		long lastTick = console.getTimeMillis();
		while (running && quadtrisBoard.isAlive()) {	
			
			console.pause(10);
			if(console.getTimeMillis() > lastTick + currentInterval())
			{
				lastTick = console.getTimeMillis();	
				quadtrisBoard.tick();
			}		
		}
		stopMusic();
		eventBus.fire(new StopAllAnimationEvent());
	}

	private int currentInterval() {
		return 300 - 10 * Math.min(level , 10);
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

	private void initializeListeners() {
		logger.debug("Initializing SnakeGame listeners.");
		addListener(KeyDownEvent.class, (KeyDownEvent k) -> {handleKeyDownEvent(k);});
		addListener(ToggleThemeEvent.class, (ToggleThemeEvent tte) -> {handleToggleThemeEvent(tte);});
		addListener(FullRowsEvent.class, (FullRowsEvent fre) -> {handleFullRowsEvent(fre);});
		addListener(RedrawPieceEvent.class, (RedrawPieceEvent rpe) -> {handleRedrawPieceEvent(rpe);});
		addListener(SpawnPieceEvent.class, (SpawnPieceEvent spe) -> {handleSpawnPieceEvent(spe);});
	}
	
	
	private void handleSpawnPieceEvent(SpawnPieceEvent spe) {
		wellRenderer.previewPiece(quadtrisBoard.getNextPiece());
		//wellRenderer.drawWellCells(quadtrisBoard.getWell());
		wellRenderer.drawPiece(quadtrisBoard.getCurrentPiece());
		updateStats();
	}

	private void handleRedrawPieceEvent(RedrawPieceEvent rpe) {		
		wellRenderer.clearPiece(rpe.getOldPiece());
		wellRenderer.drawPiece(rpe.getNewPiece());
		String sound = getSoundEffectForTransform(rpe.getTransform());
		eventBus.fire(new PlaySoundEvent(sound));
	}

	private String getSoundEffectForTransform(PieceTransformType transform) {
		String sound = "";
		switch(transform){
		case DROP:
			sound = THUNK_SOUND;
			break;
		case ROTATION:
			sound = BOOP_SOUND;
			break;
		case TRANSLATION:
			sound = BEEP_SOUND;
			break;
		default:
			break;
		}
		return sound;
	}

	private void handleFullRowsEvent(FullRowsEvent fre) {
		List<Integer> rows = fre.getRows();
		eventBus.fire(new PlaySoundEvent(TRIBEEP_SOUND));
		eventBus.fire(new StartAnimationEvent(wellRenderer.createLineFlashAnimation(rows)));
		console.pause(250);
		wellRenderer.drawWellCells(quadtrisBoard.getWell());
		score += rows.size() * rows.size() * 100;
		lines += rows.size();
		level = lines / 10;
		updateScore(score);
	}

	private void handleKeyDownEvent(KeyDownEvent ke) {
		logger.debug(String.format("KeyDown handler called: %s", ke.getKey()));
		switch (ke.getKey()) {
		case "Q":
		case "q":
			running = false;
			break;
		case Keys.DOWN_ARROW:
			// DROP PIECE
			quadtrisBoard.dropPiece();
			break;
		case Keys.RIGHT_ARROW:
			// MOVE PIECE RIGHT
			quadtrisBoard.movePieceRight();
			break;
		case Keys.LEFT_ARROW:
			// MOVE PIECE LEFT
			quadtrisBoard.movePieceLeft();
			break;
		case "z":
		case "Z":
			// ROTATE CCW
			quadtrisBoard.rotatePieceCounterClockwise();
			break;
		case "x":
		case "X":
			// ROTATE CW
			quadtrisBoard.rotatePieceClockwise();
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
	
	private void updateStats(){
		GlyphString.Builder baseBuilder = new GlyphString.Builder("Lines : " + Integer.toString(lines))
				.withFgColor(FgColor.LIGHT_YELLOW);		
		renderer.drawAt(12, 32, baseBuilder.build());
		
		baseBuilder.withBaseString("Level : " + Integer.toString(level))
				.withFgColor(FgColor.LIGHT_CYAN);
		
		renderer.drawAt(13, 32, baseBuilder.build());

		int index = 0;
		for(String name : minis.keySet()){
			Integer count = quadtrisBoard.getCount(name);
			GlyphString statLine = baseBuilder.withBaseString(" - " + count.toString())
					    .withFgColor(FgColor.LIGHT_YELLOW)
						.build()
						.prepend(minis.get(name));
			renderer.drawAt(15 + index, 32, statLine);
			index++;
		}
		
		
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
