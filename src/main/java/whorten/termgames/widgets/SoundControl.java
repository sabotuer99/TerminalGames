package whorten.termgames.widgets;

import whorten.termgames.GameConsole;
import whorten.termgames.events.EventBus;
import whorten.termgames.events.keyboard.KeyDownEvent;
import whorten.termgames.games.Game;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.Renderer;
import whorten.termgames.sounds.events.ToggleSoundEvent;

public class SoundControl {

	private GameConsole console;
	private Renderer renderer;
	private Coord location;
	private EventBus eventBus;

	public SoundControl(Game game, Coord location){
		this.console = game.getConsole();		
		this.location = location;
		this.renderer = console.getRenderer();
		this.eventBus = console.getEventBus();
		game.addListener(KeyDownEvent.class, this::handleKeyDownEvent);
	}
	
	public void update() {
		GlyphString sound_off = new GlyphString.Builder("<X ")
				.withFgColor(255,0,0).build();
		GlyphString sound_on = new GlyphString.Builder("<((") 
				.withFgColor(0,255,0).build();
		
		renderer.drawAt(location.getRow(), location.getCol(), 
				console.isSoundOn() ? sound_on : sound_off);
	}
	
	private void handleKeyDownEvent(KeyDownEvent ke) {
		switch (ke.getKey()) {
		case "s":
		case "S":
			eventBus.fire(new ToggleSoundEvent());
			update();
			break;
		default:
			break;
		}
	}
}
