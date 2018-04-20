package whorten.termgames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import whorten.termgames.events.EventBus;
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
import whorten.termgames.reflection.ClassFinder;
import whorten.termgames.render.GameBorder;
import whorten.termgames.render.OutputStreamRenderer;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.SoundPlayer;
import whorten.termgames.sounds.events.MidiStartEvent;
import whorten.termgames.sounds.events.MidiStopEvent;
import whorten.termgames.sounds.events.PlaySoundEvent;
import whorten.termgames.utils.CircularList;
import whorten.termgames.utils.StringUtils;

/**
 * Hello world!
 *
 */
public class GameConsole {
	private static GameConsole instance = new GameConsole();
	private KeyboardEventDriver ked;
	private static ExecutorService pool = Executors.newCachedThreadPool();
	private Renderer renderer = new OutputStreamRenderer(System.out, 80, 24);
	private final EventBus eventBus = new EventBus();
	private final SoundPlayer soundPlayer = getSoundPlayer();
	private ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	private Map<String, byte[]> cachedBytes = new HashMap<>();
	private Map<String, Game> games = new TreeMap<>(); 
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		initEventSystem();
		
		instance.runInThreadPool(() -> {
			try { instance.ked.listen();} 	
			catch (IOException e) {}
		});	

		instance.loadGames();			
		instance.renderMenu();
		instance.renderGameSelector(0);
		
		Thread.sleep(5000);
		
//     
//		for(int i = 0; i < 5; i++){
//			instance.renderer.clearScreen();
//			new SnakeGame().plugIn(instance);
//			instance.soundPlayer.close();
//		}
		
		Glyph bodyGlyph =  new Glyph.Builder("◆")
	            .withForegroundColor(FgColor.LIGHT_YELLOW)
	            .withBackgroundColor(BgColor.GREEN)
	            .build();
		
		for(int row = 1; row <= 24; row++){
			for(int col = 1; col <= 80; col++){
				instance.renderer.drawAt(row,col,bodyGlyph);
			}
		}
		
		instance.ked.die();
		pool.shutdown();
	}

	private void loadGames() {
		ClassFinder classFinder = new ClassFinder(Game.class);
		Set<String> gameClassNames = classFinder.getClasses();
		games.remove("whorten.termgames.games.Game");
		
		for(String gameClassName : gameClassNames){
			try {
				Game game = (Game) Class.forName(gameClassName).newInstance();
				games.put(game.getDisplayName(), game);
			} catch (Exception e) {
				//gulp
			}
		}
	}

	private void renderGameSelector(int index) {
		int offset = (renderer.getCanvasWidth() - 30) / 2;
		List<String> displayNames = new CircularList<>();
		for(String name : games.keySet()){
			displayNames.add(name);
		}
		
		String[] names = new String[7];
		for(int i = 0; i < 7; i++){
			names[i] = displayNames.get(i - 3);
		}
		
		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
		GlyphString[] nameGS = new GlyphString[7];
		nameGS[0] = menuBuilder.withBaseString(pad(names[0],30)).withFgColor(64, 64, 64).build();
  		nameGS[1] = menuBuilder.withBaseString(pad(names[1],30)).withFgColor(128,128,128).build();
  		nameGS[2] = menuBuilder.withBaseString(pad(names[2],30)).withFgColor(192,192,192).build();
  		nameGS[3] = menuBuilder.withBaseString(pad(names[3],30)).withFgColor(FgColor.WHITE).isBold(true).withBgColor(0,0,64).build();
  		nameGS[4] = menuBuilder.withBaseString(pad(names[4],30)).withFgColor(192,192,192).isBold(false).clearBgColor().build();		                                                            
  		nameGS[5] = menuBuilder.withBaseString(pad(names[5],30)).withFgColor(128,128,128).build();
  		nameGS[6] = menuBuilder.withBaseString(pad(names[6],30)).withFgColor(64, 64, 64).build();
  		for(int i = 0; i < 7; i ++){
  			renderer.drawAt(13 + i, offset, nameGS[i]);
  		}
	}

	private String pad(String string, int width) {
		if(string.length() > 30){
			return string.substring(0, 27) + "...";
		}		
		int left = (30 - string.length()) / 2;
		int right = 30 - string.length() - left;
		return StringUtils.repeat(" ", left) + string + StringUtils.repeat(" ", right);
	}

	private void renderMenu() {

		renderer.turnOffCursor();
		renderer.clearScreen();
		GameBorder gb = defaultGameBorder(renderer);
		renderer.drawGlyphCollection(gb.getGlyphCoords());
		
		GlyphString.Builder menuBuilder = new GlyphString.Builder(" ");
  		String title1 = " _____                      ____                           ";
  		String title2 = "|_   _|__ _ __ _ __ ___    / ___| __ _ _ __ ___   ___  ___ ";
  		String title3 = "  | |/ _ \\ '__| '_ ` _ \\  | |  _ / _` | '_ ` _ \\ / _ \\/ __|";
  		String title4 = "  | |  __/ |  | | | | | | | |_| | (_| | | | | | |  __/\\__ \\";
  		String title5 = "  |_|\\___|_|  |_| |_| |_|  \\____|\\__,_|_| |_| |_|\\___||___/";
  		GlyphString gsTitle1 = menuBuilder.withBaseString(title1).withFgColor(0,0,255).build();
  		GlyphString gsTitle2 = menuBuilder.withBaseString(title2).withFgColor(0,64,255).build();
  		GlyphString gsTitle3 = menuBuilder.withBaseString(title3).withFgColor(0,128,255).build();
  		GlyphString gsTitle4 = menuBuilder.withBaseString(title4).withFgColor(0,192,255).build();
  		GlyphString gsTitle5 = menuBuilder.withBaseString(title5).withFgColor(0,255,255).build();		                                                            
  		renderer.drawAt(2, 11, gsTitle1);
  		renderer.drawAt(3, 11, gsTitle2);
  		renderer.drawAt(4, 11, gsTitle3);
  		renderer.drawAt(5, 11, gsTitle4);
  		renderer.drawAt(6, 11, gsTitle5);
	}

	private GameBorder defaultGameBorder(Renderer renderer2) {
		return new GameBorder.Builder(renderer.getCanvasHeight(), renderer.getCanvasWidth())
				.withFgColor(100,100,255)
				.withBgColor(100, 0, 255)
				.withNoSidebar()
				.build();
	}

	private static void initEventSystem() {
		instance.ked = new KeyboardEventDriver.Builder()
				.withInputStream(System.in)
				.withListener((KeyEvent ke) -> instance.handleKeyEvent(ke))
				.build();
		
		instance.eventBus.subscribe(PlaySoundEvent.class, 
				(PlaySoundEvent pse) -> {instance.handlePlaySoundEvent(pse);});
		
		instance.eventBus.subscribe(MidiStartEvent.class, 
				(MidiStartEvent mse) -> {instance.handleMidiStartEvent(mse);});
		
		instance.eventBus.subscribe(MidiStopEvent.class, 
				(MidiStopEvent mse) -> {instance.handleMidiStopEvent(mse);});
	}
	
	private void handleMidiStopEvent(MidiStopEvent mse) {
		instance.soundPlayer.stopMidi();
	}

	private void handleMidiStartEvent(MidiStartEvent mse) {
		String path = mse.getPath();
		byte[] cached = getFileBytes(path);					
		instance.soundPlayer.playMidi(new ByteArrayInputStream(cached), true);
	}

	private void handlePlaySoundEvent(PlaySoundEvent pse) {		
			String path = pse.getPath();
			byte[] cached = getFileBytes(path);					
			instance.soundPlayer.play(new ByteArrayInputStream(cached));
	}

	private byte[] getFileBytes(String path){
		try{
			byte[] cached = cachedBytes.get(path);
			if(cached == null){
				InputStream sound = classLoader.getResourceAsStream(path);
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(); 
				int nextByte = sound.read(); 
				while(nextByte != -1){ 
					buffer.write(nextByte); 
					nextByte = sound.read(); 
				} 
				cached = buffer.toByteArray();
				cachedBytes.put(path, cached);
			}
			return cached;
		} catch(Exception ex){
			//gulp
		}
		return null;
	}

	public EventBus getEventBus(){
		return this.eventBus;
	}
	
	public void runInThreadPool(Runnable runnable){
		pool.execute(runnable);
	}
	
	
	private void handleKeyEvent(KeyEvent ke) {
		if(ke.getKeyEventType() == KeyEventType.UP){
			eventBus.fire((KeyUpEvent) ke);
		}
		if(ke.getKeyEventType() == KeyEventType.DOWN){
			eventBus.fire((KeyDownEvent) ke);
		}
	}

	public Renderer getRenderer() {
		return renderer;
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
}
