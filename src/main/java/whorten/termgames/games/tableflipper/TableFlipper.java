package whorten.termgames.games.tableflipper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.animation.events.StopAllAnimationEvent;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.tableflipper.board.TableFlipperBoard;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.games.tableflipper.events.EntitySpawnEvent;
import whorten.termgames.games.tableflipper.events.PlayerMoveEvent;
import whorten.termgames.games.tableflipper.events.TableFlipEvent;
import whorten.termgames.games.tableflipper.events.TableUnflipEvent;
import whorten.termgames.games.tableflipper.renderer.TableFlipperRenderer;
import whorten.termgames.geometry.Coord;
import whorten.termgames.utils.Keys;
import whorten.termgames.widgets.MusicControl;
import whorten.termgames.widgets.SoundControl;


public class TableFlipper extends Game {

	private static final Logger logger = LogManager.getLogger(TableFlipper.class);
	private static final String MUSIC_FILE = "midi/Table_Flipper_Theme.mid";
	private static final String FOOTSTEP_SOUND = "sounds/footstep.wav";
	private static final String AH_SOUND = "sounds/girl-ah.wav";
	private TableFlipperBoard board;
	private TableFlipperRenderer tfr;
	private MusicControl musicControl;
	private SoundControl soundControl;
	
	@Override
	public void plugIn(GameConsole console) {
		resetGameState(console);
		renderBoard();
		initializeListeners();
		addEntities();
		run();
		removeLocalListeners();
	}
	
	private void addEntities() {
		eventBus.fire(new EntitySpawnEvent(board.getPlayer()));
		board.addRandomNpc();
		board.addRandomNpc();
		board.addRandomNpc();
		board.addRandomNpc();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
		board.addRandomTable();
	}

	private void initializeListeners() {
		logger.debug("Initializing TableFlipper listeners.");
		addListener(KeyDownEvent.class, this::handleKeyDownEvent);
		addListener(EntityChangeEvent.class, this::handleEntityChangeEvent);
		addListener(TableFlipEvent.class, this::handleTableFlipEvent);
		addListener(TableUnflipEvent.class, this::handleTableUnflipEvent);
		addListener(PlayerMoveEvent.class, this::handlePlayerMoveEvent);
		addListener(EntitySpawnEvent.class, this::handleEntitySpawnEvent);
	}

	private void run() {
		playMusic(MUSIC_FILE);
		while (running) {				
			console.runInThreadPool(new Runnable(){
				@Override
				public void run() {
					board.tick(console.getTimeMillis());						
				}
			});
			console.pause(80);
		}
		stopMusic();
		eventBus.fire(new StopAllAnimationEvent());
	}

	@Override
	public void resetGameState(GameConsole console){
		super.resetGameState(console);		
		tfr = new TableFlipperRenderer(console);
		board = new TableFlipperBoard.Builder(eventBus).build();
		musicControl = new MusicControl(this, new Coord(74, 19));
		soundControl = new SoundControl(this, new Coord(74, 20));
	}
	
	public void renderBoard(){
		tfr.drawBoard();	
		soundControl.update();
		musicControl.update();
	}

	@Override
	public String getDisplayName() {
		return "┳━┳ Table Flipper ┳━┳";
	}
	
	private void handleKeyDownEvent(KeyDownEvent ke) {
		logger.debug(String.format("KeyDown handler called: %s", ke.getKey()));
		switch (ke.getKey()) {
		case "Q":
		case "q":
			running = false;
			break;
		case "Z":
		case "z":
			board.flip(console.getTimeMillis());
			playSound(AH_SOUND);
			break;
		case Keys.DOWN_ARROW:
			board.movePlayerDown(console.getTimeMillis());
			break;
		case Keys.UP_ARROW:
			board.movePlayerUp(console.getTimeMillis());
			break;
		case Keys.RIGHT_ARROW:
			board.movePlayerRight(console.getTimeMillis());
			break;
		case Keys.LEFT_ARROW:
			board.movePlayerLeft(console.getTimeMillis());
			break;
		}
	}
	
	private synchronized void handleEntityChangeEvent(EntityChangeEvent eme){
		//erase from entity
		//draw to entity
		//logger.info(String.format("Changing entity from %s to %s", eme.getFrom(), eme.getTo()));
		tfr.moveEntity(eme.getFrom(), eme.getTo());
	
	}
	
	private synchronized void handleEntitySpawnEvent(EntitySpawnEvent esw){
		tfr.drawEntity(esw.getEntity());
	}
	
	private synchronized void handleTableFlipEvent(TableFlipEvent tfe){
		tfr.moveEntity(tfe.getUnflipped(), tfe.getFlipped());
	}
	
	private synchronized void handleTableUnflipEvent(TableUnflipEvent tuf){
		board.unflipTable(tuf.getFlipped(), tuf.getUnflipped());
		tfr.moveEntity(tuf.getFlipped(), tuf.getUnflipped());		
	}
	
	private void handlePlayerMoveEvent(PlayerMoveEvent pme){
		playSound(FOOTSTEP_SOUND);
	}
}
