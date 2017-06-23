package me.retrodaredevil.pokecreator.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * @author retro
 *
 * @param <T> 
 * 
 * This class makes it simple to iterate over and remove objects from multiple lists in a simple for loop
 */
public class MultiListIterator<T> implements Iterator<T>{

	private List<Iterator<T>> its = new ArrayList<>();
	private int current = 0;
	
	public MultiListIterator(List<List<T>> lists){
		for(List<T> l : lists){
			its.add(l.listIterator());
		}
		
		
	}
	
	/**
	 * 
	 * @return returns the current ListIterator. This will return the last one even if it is done with it. If there is no last listit, then it will return null
	 */
	protected Iterator<T> getCurrent(){
		if(its.size() == 0){
			return null;
		}
		
		if(current >= its.size()){
			return its.get(its.size() - 1);
		}
		
		
		return its.get(current);
	}
	/*
	@Override
	public void add(T e) {
		
		
		ListIterator<T> current = this.getCurrent();
		if (current == null){
			throw new OperationNotSupportedException("There is no current ListIterator to add to and there are no provided list to add to.");
		}
		
		
	}
	*/
	
	@Override
	public boolean hasNext() {
		Iterator<T> current = this.getCurrent();
		if(current == null){
			return false;
		}
		if(!current.hasNext()){
			this.current++;
			current = this.getCurrent();
		}
		
		return current.hasNext();
		
	}
	/*
	@Override
	public boolean hasPrevious() {
		ListIterator<T> current = this.getCurrent();
		if(current == null){
			return false;
		}
		return current.hasPrevious();
	}
	*/
	
	
	@Override
	public T next() {
		Iterator<T> current = this.getCurrent();
		
		if(current == null){
			throw new NoSuchElementException("There were no provided lists");
		}
		
		if(!current.hasNext()){
			this.current++;
			current = this.getCurrent();
		}
		
		
		return current.next();
		
	}
/*
	@Override
	public int nextIndex() {
		ListIterator<T> current = this.getCurrent();
		
		return current.nextIndex();
	}

	@Override
	public T previous() {
		ListIterator<T> current = this.getCurrent();
		
		if(current == null){
			throw new NoSuchElementException("There are no provided lists.");
		}
		if(current.hasPrevious()){
			return current.previous();
		}
		for(int i = this.current; i >= 0; i--){
			if(i > this.its.size()){
				i--;
			}
			if(i >= this.its.size()){
				throw new NoSuchElementException("Error while getting previous");
			}
			current = this.its.get(i);
			if(current.hasPrevious()){
				return current.previous();
			}
		}
		
		throw new NoSuchElementException("None of the lists had a previous value.");
	}

	@Override
	public int previousIndex() {
		ListIterator<T> current = this.getCurrent();
		
		if(current == null){
			return -1;
		}
		if(current.hasPrevious()){
			return current.previousIndex();
		}
		for(int i = this.current; i >= 0; i--){
			if(i > this.its.size()){
				i--;
			}
			if(i >= this.its.size()){
				return -1;
			}
			current = this.its.get(i);
			if(current.hasPrevious()){
				return current.previousIndex();
			}
		}
		
		return -1;
	}
	*/
	
	@Override
	public void remove() {
		Iterator<T> current = this.getCurrent();
		
		if(current == null){
			throw new IllegalStateException("There is nothing to remove.");
		}
		
		current.remove();
	}

	/*
	@Override
	public void set(T e) {
		ListIterator<T> current = this.getCurrent();
		
		if(current == null){
			throw new IllegalStateException("There is nothing to remove.");
		}
		
		current.set(e);
		
	}
	*/
}
