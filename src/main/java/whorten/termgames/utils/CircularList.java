package whorten.termgames.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CircularList<E> implements List<E> {

	private ArrayList<E> backingList = new ArrayList<>();
	
	private int mod(int index){
		if(index < 0){
			return size() + (index % size());
		} else {
			return index % size();
		}
	}
	
	@Override
	public int size() {
		return backingList.size();
	}

	@Override
	public boolean isEmpty() {
		return backingList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return backingList.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return backingList.iterator();
	}

	@Override
	public Object[] toArray() {
		return backingList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return backingList.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return backingList.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return backingList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return backingList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return backingList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return backingList.addAll(mod(index), c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return backingList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return backingList.retainAll(c);
	}

	@Override
	public void clear() {
		backingList.clear();
	}

	@Override
	public E get(int index) {
		return backingList.get(mod(index));
	}

	@Override
	public E set(int index, E element) {
		return backingList.set(mod(index), element);
	}

	@Override
	public void add(int index, E element) {
		backingList.set(mod(index), element);		
	}

	@Override
	public E remove(int index) {
		return backingList.remove(mod(index));
	}

	@Override
	public int indexOf(Object o) {
		return backingList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return backingList.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return backingList.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		int endIndex = index + this.size();
		return subList(index, endIndex).listIterator();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		List<E> subList = new ArrayList<>();
		for(int i = fromIndex; i < toIndex; i++){
			subList.add(get(i));
		}
		return subList;
	}

}
