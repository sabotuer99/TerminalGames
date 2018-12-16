package whorten.termgames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.animation.AnimationController;
import whorten.termgames.animation.events.StartAnimationEvent;
import whorten.termgames.animation.events.StopAllAnimationEvent;
import whorten.termgames.animation.events.StopAnimationEvent;
import whorten.termgames.events.EventBus;
import whorten.termgames.events.EventListener;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.events.keyboard.KeyEvent;
import whorten.termgames.events.keyboard.KeyEventType;
import whorten.termgames.events.keyboard.KeyUpEvent;
import whorten.termgames.events.keyboard.KeyboardEventDriver;
import whorten.termgames.games.Game;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.loader.GlyphLoader;
import whorten.termgames.reflection.ClassFinder;
import whorten.termgames.render.OutputStreamRenderer;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.SoundPlayer;
import whorten.termgames.sounds.events.MidiStartEvent;
import whorten.termgames.sounds.events.MidiStopEvent;
import whorten.termgames.sounds.events.PlaySoundEvent;
import whorten.termgames.sounds.events.ToggleMusicEvent;
import whorten.termgames.sounds.events.ToggleSoundEvent;
import whorten.termgames.utils.CircularList;
import whorten.termgames.utils.Keys;
import whorten.termgames.utils.StringUtils;

public class GameConsole {
	private final static Logger logger = LogManager.getLogger(GameConsole.class);
	private static final String BOOP_SOUND = "sounds/boop.wav";
	private static final String SELECT_SOUND = "sounds/three_boop.wav";
	private static final GameConsole instance = new GameConsole();
	private KeyboardEventDriver ked;
	private static ExecutorService pool = Executors.newCachedThreadPool();
	private volatile boolean stillPlaying = true;
	private final Renderer renderer = new OutputStreamRenderer(System.out, 80, 24);
	private final EventBus eventBus = new EventBus();
	private final SoundPlayer soundPlayer = getSoundPlayer();
	private final AnimationController animator = new AnimationController(this);
	private final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	private final Map<String, byte[]> cachedBytes = new HashMap<>();
	private final List<String> gameNames = new CircularList<>();
	private final Map<String, Game> games = new TreeMap<>();
	private volatile boolean gameIsRunning;
	private Game currentGame = null;
	private int gameIndex = 0;
	private static final String MAIN_MENU_BACKGROUND_FILE = 
			"gspecs/mainmenu_background.gstxt";
	private EventListener<PlaySoundEvent> pseEventListener = null;	
	private EventListener<MidiStartEvent> midiStartEventListener = null;	
	private EventListener<MidiStopEvent> midiStopEventListener = null;	
	private EventListener<KeyDownEvent> keyDownEventListener = null;
	private EventListener<ToggleSoundEvent> toggleSoundEventListener;
	private EventListener<ToggleMusicEvent> toggleMusicEventListener;
	private EventListener<StartAnimationEvent> startAnimationEventListener;
	private EventListener<StopAnimationEvent> stopAnimationEventListener;
	private EventListener<StopAllAnimationEvent> stopAllAnimationEventListener;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		setGlobalUncaughtExceptionHandler();	
		List<String> argList = Arrays.asList(args);
		logger.info("Running with args: ", Arrays.toString(args));
		instance.initEventSystem(argList.contains("--windows"));
		
		instance.runInThreadPool(() -> {
			try { instance.ked.listen();} 	
			catch (IOException e) {
				instance.stillPlaying = false;
				throw new RuntimeException(e.getMessage());
			}
		});	

		instance.loadGames();				
		instance.renderMenu();
		instance.renderGameSelector(instance.gameIndex);
		
		if(instance.games.size() == 0){
			instance.stillPlaying = false;
			instance.pause(5000);
		} else {
			instance.currentGame = instance.games.get(
					instance.gameNames.get(instance.gameIndex));
		}
			
		while(instance.stillPlaying){
			instance.pause(100);
			if(instance.gameIsRunning){
				instance.eventBus.unsubscribe(KeyDownEvent.class, instance.keyDownEventListener);
				instance.currentGame.plugIn(instance);
				instance.renderMenu();
				instance.renderGameSelector(instance.gameIndex);
				instance.gameIsRunning = false;
				instance.eventBus.subscribe(KeyDownEvent.class, instance.keyDownEventListener);
				instance.soundPlayer.setMidiEnabled(true);
				instance.soundPlayer.setSoundEnabled(true);
			}
		}
		
		fancyClose();
		System.exit(0);
	}

	public void pause(long pauseMillis) {
		try{
			Thread.sleep(pauseMillis);
		} catch (Exception ex){
			// gulp
		}
	}

	private static void setGlobalUncaughtExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {		
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.fatal(sw.toString());
				logger.fatal("Uncaught Exception, exiting...");
				System.exit(1);
			}
		});
	}

	private static void fancyClose() {
		Glyph bodyGlyph = new Glyph.Builder("◆").withForegroundColor(FgColor.LIGHT_YELLOW)
				.withBackgroundColor(BgColor.GREEN).build();

		for (int row = 1; row <= 24; row++) {
			for (int col = 1; col <= 80; col++) {
				instance.renderer.drawAt(row, col, bodyGlyph);
			}
		}
	}

	private void loadGames() {
		ClassFinder classFinder = new ClassFinder(Game.class);
		Set<String> gameClassNames = classFinder.getClasses();
		games.remove("whorten.termgames.games.Game");

		for (String gameClassName : gameClassNames) {
			try {
				Game game = (Game) Class.forName(gameClassName).newInstance();
				games.put(game.getDisplayName(), game);
			} catch (Exception e) {
				// gulp
			}
		}
		
		for (String name : games.keySet()) {
			gameNames.add(name);
		}
	}

	private void renderGameSelector(int index) {
		int offset = (renderer.getCanvasWidth() - 30) / 2;
		if(games.size() == 0){
			GlyphString error = new GlyphString.Builder(pad("ERROR: no games loaded!", 30))
					.withFgColor(255,0,0).isBold(true).build();
			renderer.drawAt(13, offset, error);
			return;
		}
				
		String[] names = new String[7];
		for (int i = 0; i < 7; i++) {
			names[i] = gameNames.get(index + i - 3);
		}

		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
		GlyphString[] nameGS = new GlyphString[7];
		nameGS[0] = menuBuilder.withBaseString(pad(names[0], 30)).withFgColor(64, 64, 64).build();
		nameGS[1] = menuBuilder.withBaseString(pad(names[1], 30)).withFgColor(128, 128, 128).build();
		nameGS[2] = menuBuilder.withBaseString(pad(names[2], 30)).withFgColor(192, 192, 192).build();
		nameGS[3] = menuBuilder.withBaseString(pad(names[3], 30)).withFgColor(FgColor.WHITE).isBold(true)
				.withBgColor(0, 0, 64).build();
		nameGS[4] = menuBuilder.withBaseString(pad(names[4], 30)).withFgColor(192, 192, 192).isBold(false)
				.clearBgColor().build();
		nameGS[5] = menuBuilder.withBaseString(pad(names[5], 30)).withFgColor(128, 128, 128).build();
		nameGS[6] = menuBuilder.withBaseString(pad(names[6], 30)).withFgColor(64, 64, 64).build();
		for (int i = 0; i < 7; i++) {
			renderer.drawAt(13 + i, offset, nameGS[i]);
		}
	}

	private String pad(String string, int width) {
		if (string.length() > 30) {
			return string.substring(0, 27) + "...";
		}
		int left = (30 - string.length()) / 2;
		int right = 30 - string.length() - left;
		return StringUtils.repeat(" ", left) + string + StringUtils.repeat(" ", right);
	}

	private void renderMenu() {

		renderer.turnOffCursor();
		renderer.clearScreen();
		renderer.drawGlyphStringCollection(loadFromFile(MAIN_MENU_BACKGROUND_FILE));
	}

	private void initEventSystem(boolean isWindows) {
		if (isWindows){
			System.out.println("No Windows Support yet, sorry =(");
			throw new RuntimeException();
		} else {
			ked = new KeyboardEventDriver.Builder().withInputStream(System.in)
					.withListener((KeyEvent ke) -> handleKeyEvent(ke)).build();
		}
		
		pseEventListener = (PlaySoundEvent pse) -> {handlePlaySoundEvent(pse);};		
		midiStartEventListener = (MidiStartEvent mse) -> {handleMidiStartEvent(mse);};		
		midiStopEventListener = (MidiStopEvent mse) -> {handleMidiStopEvent(mse);};		
		keyDownEventListener = (KeyDownEvent kde) -> {handleKeyDownEvent(kde);};	
		toggleSoundEventListener = (ToggleSoundEvent tse) -> {handleToggleSoundEvent(tse);};
		toggleMusicEventListener = (ToggleMusicEvent tme) -> {handleToggleMusicEvent(tme);};
		startAnimationEventListener = (StartAnimationEvent sae) -> {handleStartAnimationEvent(sae);};
		stopAnimationEventListener = (StopAnimationEvent sae) -> {handleStopAnimationEvent(sae);};
		stopAllAnimationEventListener = (StopAllAnimationEvent saae) -> {handleStopAllAnimationEvent(saae);};
		eventBus.subscribe(PlaySoundEvent.class, pseEventListener);
		eventBus.subscribe(MidiStartEvent.class, midiStartEventListener);
		eventBus.subscribe(MidiStopEvent.class, midiStopEventListener);
		eventBus.subscribe(KeyDownEvent.class, keyDownEventListener);
		eventBus.subscribe(ToggleSoundEvent.class, toggleSoundEventListener);
		eventBus.subscribe(ToggleMusicEvent.class, toggleMusicEventListener);
		eventBus.subscribe(StartAnimationEvent.class, startAnimationEventListener);
		eventBus.subscribe(StopAnimationEvent.class, stopAnimationEventListener);
		eventBus.subscribe(StopAllAnimationEvent.class, stopAllAnimationEventListener);
	}

	private void handleStopAllAnimationEvent(StopAllAnimationEvent saae) {
		animator.stopAllAnimations();
	}

	private void handleStopAnimationEvent(StopAnimationEvent sae) {
		animator.stopAnimation(sae.getAnimation());
	}

	private void handleStartAnimationEvent(StartAnimationEvent sae) {
		animator.playAnimation(sae.getAnimation());
	}

	private void handleToggleSoundEvent(ToggleSoundEvent tse){
		soundPlayer.toggleSound();
	}
	
	private void handleToggleMusicEvent(ToggleMusicEvent tme){
		soundPlayer.toggleMidi();
	}
	
	private void handleKeyDownEvent(KeyDownEvent kde) {
		String key = kde.getKey();
		if (!gameIsRunning) {
			switch (key) {
			case Keys.UP_ARROW:
				eventBus.fire(new PlaySoundEvent(BOOP_SOUND));
				gameIndex--;
				currentGame = games.get(gameNames.get(gameIndex));
				renderGameSelector(gameIndex);				
				break;
			case Keys.DOWN_ARROW:
				eventBus.fire(new PlaySoundEvent(BOOP_SOUND));
				gameIndex++;
				currentGame = games.get(gameNames.get(gameIndex));
				renderGameSelector(gameIndex);
				break;
			case Keys.ENTER:
				eventBus.fire(new PlaySoundEvent(SELECT_SOUND));
				pause(792);
				gameIsRunning = true;
				break;
			case "Q":
			case "q":
				stillPlaying = false;
				break;
			default:
				break;
			}
		}
	}

	private void handleMidiStopEvent(MidiStopEvent mse) {
		instance.soundPlayer.stopMidi();
	}

	private void handleMidiStartEvent(MidiStartEvent mse) {
		String path = mse.getPath();
		byte[] cached = getFileBytes(path);
		if(cached != null){
			instance.soundPlayer.playMidi(new ByteArrayInputStream(cached), true);
		}
	}

	private void handlePlaySoundEvent(PlaySoundEvent pse) {
		String path = pse.getPath();
		byte[] cached = getFileBytes(path);
	    logger.debug(String.format("Playing sound %s", path));
		if(cached != null){
			instance.soundPlayer.play(new ByteArrayInputStream(cached));
		}
		
	}

	private byte[] getFileBytes(String path) {
		try {
			byte[] cached = cachedBytes.get(path);
			if (cached == null) {
				InputStream sound = classLoader.getResourceAsStream(path);

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nextByte = sound.read();
				while (nextByte != -1) {
					buffer.write(nextByte);
					nextByte = sound.read();
				}
				cached = buffer.toByteArray();
				cachedBytes.put(path, cached);
			}
			return cached;
		} catch (Exception ex) {
			// gulp
		}
		return null;
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	public void runInThreadPool(Runnable runnable) {
		pool.execute(runnable);
	}

	private void handleKeyEvent(KeyEvent ke) {
		if (ke.getKeyEventType() == KeyEventType.UP) {
			eventBus.fire((KeyUpEvent) ke);
		}
		if (ke.getKeyEventType() == KeyEventType.DOWN) {
			eventBus.fire((KeyDownEvent) ke);
		}
	}

	public Renderer getRenderer() {
		return renderer;
	}
	
	public boolean isKeyboardEventDriverListening(){
		return ked.isListening();
	}

	private SoundPlayer getSoundPlayer() {
		Sequencer sequencer = null;
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
		}

		return new SoundPlayer.Builder().withSequencer(sequencer).build();
	}

	public boolean isMusicOn() {
		return soundPlayer.isMidiEnabled();
	}

	public boolean isSoundOn() {
		return soundPlayer.isSoundEnabled();
	}
	
	public long getTimeMillis(){
		return System.currentTimeMillis();
	}
	
	public Set<GlyphStringCoord> loadFromFile(String fileName){
		GlyphLoader gl = new GlyphLoader();
		InputStream file = classLoader.getResourceAsStream(fileName);
		return gl.parse(file);
	}
	
}
