package whorten.termgames.games.snake.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.snake.Fruit;

public class EatFruitEvent implements Event{
	
	private Fruit fruit;
	private int newSize;

	public EatFruitEvent(Fruit fruit, int newSize){
		this.fruit = fruit;
		this.newSize = newSize;
	}
	
	public int getNewSize(){
		return newSize;
	}
	
	public Fruit getFruit(){
		return fruit;
	}
	
	@Override
	public String toString() {
		return String.format("EatFruitEvent: fruit: [%s], newSize: [%d]", fruit, newSize);
	}
}

