package whorten.termgames.games.tableflipper;

import whorten.termgames.GameConsole;
import whorten.termgames.games.Game;
import whorten.termgames.games.tableflipper.renderer.TableFlipperRenderer;

public class TableFlipper extends Game {

	private TableFlipperRenderer tfr;
	
	@Override
	public void plugIn(GameConsole console) {
		resetGameState(console);
		renderBoard();
		initializeListeners();
		run();
		removeLocalListeners();
	}
	
	private void initializeListeners() {
		// TODO Auto-generated method stub
		
	}

	private void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetGameState(GameConsole console){
		super.resetGameState(console);		
		tfr = new TableFlipperRenderer(console);
	}
	
	public void renderBoard(){
		tfr.drawBoard();		
	}

	@Override
	public String getDisplayName() {
		return "┳━┳ Table Flipper ┳━┳";
	}

}
