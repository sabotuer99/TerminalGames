package whorten.termgames.games.tableflipper.events;

import whorten.termgames.entity.Entity;
import whorten.termgames.events.Event;

public class EntitySpawnEvent implements Event {

	private Entity entity;

	public EntitySpawnEvent(Entity entity){
		this.entity = entity;
	}
	
	public Entity getEntity(){
		return entity;
	}
}
