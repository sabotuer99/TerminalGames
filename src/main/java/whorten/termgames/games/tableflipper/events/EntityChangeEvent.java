package whorten.termgames.games.tableflipper.events;

import whorten.termgames.entity.Entity;
import whorten.termgames.events.Event;

public class EntityChangeEvent implements Event {

	private Entity from;
	private Entity to;

	public EntityChangeEvent(Entity from, Entity to) {
		this.from = from;
		this.to = to;
	}
	
	public Entity getFrom(){
		return from;
	}
	
	public Entity getTo(){
		return to;
	}

}
