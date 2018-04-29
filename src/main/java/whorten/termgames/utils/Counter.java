package whorten.termgames.utils;

import java.util.HashMap;
import java.util.Map;

public class Counter<E> {
	Map<E, Integer> counts = new HashMap<>();
	
	public void increment(E key){
		Integer count = safeGet(key);
		count++;
		counts.put(key, count);
	}	
	
	public void decrement(E key){
		Integer count = safeGet(key);
		count--;
		counts.put(key, count);
	}
	
	public Integer getCount(E key){
		return safeGet(key);
	}

	private Integer safeGet(E key) {
		Integer count = counts.get(key);
		if(count == null){
			count = 0;
		}
		return count;
	}

}
