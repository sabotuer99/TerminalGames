package whorten.termgames.games.snake.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.snake.Fruit;

public class EatFruitEvent implements Event{
	
	private Fruit fruit;

	public EatFruitEvent(Fruit fruit){
		this.fruit = fruit;
	}
	
	public Fruit getFruit(){
		return fruit;
	}
}

