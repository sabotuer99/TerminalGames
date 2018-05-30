package whorten.termgames.games.tableflipper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.GameConsole;
import whorten.termgames.animation.events.StopAllAnimationEvent;
import whorten.termgames.entity.Entity;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.games.tableflipper.board.TableFlipperBoard;
import whorten.termgames.games.tableflipper.events.EntityChangeEvent;
import whorten.termgames.games.tableflipper.events.PlayerMoveEvent;
import whorten.termgames.games.tableflipper.events.TableFlipEvent;
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
		run();
		removeLocalListeners();
	}
	
	private void initializeListeners() {
		logger.debug("Initializing TableFlipper listeners.");
		addListener(KeyDownEvent.class, this::handleKeyDownEvent);
		addListener(EntityChangeEvent.class, this::handleEntityChangeEvent);
		addListener(TableFlipEvent.class, this::handleTableFlipEvent);
		addListener(PlayerMoveEvent.class, this::handlePlayerMoveEvent);
	}

	private void run() {
		playMusic(MUSIC_FILE);
		tfr.drawEntity(board.getPlayer());
		for(Entity npc : board.getNPCs()){
			tfr.drawEntity(npc);
		}
		while (running) {				
			console.pause(40);
			board.tick(console.getTimeMillis());	
		}
		stopMusic();
		eventBus.fire(new StopAllAnimationEvent());
	}

	@Override
	public void resetGameState(GameConsole console){
		super.resetGameState(console);		
		tfr = new TableFlipperRenderer(console);
		board = new TableFlipperBoard.Builder(eventBus).build();
		board.addRandomNpc();
		board.addRandomNpc();
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
	
	private void handleEntityChangeEvent(EntityChangeEvent eme){
		//erase from entity
		//draw to entity
		logger.info(String.format("Changing entity from %s to %s", eme.getFrom(), eme.getTo()));
		tfr.clearEntity(eme.getFrom());
		tfr.drawEntity(eme.getTo());
	}
	
	private void handleTableFlipEvent(TableFlipEvent tfe){
		
	}
	
	private void handlePlayerMoveEvent(PlayerMoveEvent pme){
		playSound(FOOTSTEP_SOUND);
	}
}
