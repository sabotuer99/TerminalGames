package whorten.termgames.widgets;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventBus;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.ToggleMusicEvent;

public class MusicControl {

	private GameConsole console;
	private Renderer renderer;
	private Coord location;
	private EventBus eventBus;

	public MusicControl(Game game, Coord location){
		this.console = game.getConsole();		
		this.location = location;
		this.renderer = console.getRenderer();
		this.eventBus = console.getEventBus();
		game.addListener(KeyDownEvent.class, this::handleKeyDownEvent);
	}
	
	public void update() {
		GlyphString music_off = new GlyphString.Builder("dXb")
				.withFgColor(255,0,0).build();
		GlyphString music_on = new GlyphString.Builder("d⎺b")
				.withFgColor(0,255,0).build();
		
		renderer.drawAt(location.getRow(), location.getCol(), 
				console.isMusicOn() ? music_on : music_off);	
	}
	
	private void handleKeyDownEvent(KeyDownEvent ke) {
		switch (ke.getKey()) {
		case "m":
		case "M":
			eventBus.fire(new ToggleMusicEvent());
			update();
			break;
		default:
			break;
		}
	}
}
