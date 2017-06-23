package me.retrodaredevil.pokecreator.util;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 * @param <T>
 */
public class ScreenList<T extends Screen> implements Screen{
	
	
	private T current;
	private List<T> waiting = new ArrayList<>();
	
	
	/**
	 * 
	 * @return returns all the values including the current message in the '0' spot on the list. Adds the waiting values to the end of the list.
	 */
	public List<T> getAllValues(){
		ArrayList<T> r = new ArrayList<>();
		if(current != null){
			r.add(this.current);
		}
		if(waiting.size() > 0){
			r.addAll(this.waiting);
		}
		return r;
		
	}
	
	public void add(T add){
		if(add == null){
			return;
		}
		waiting.add(add);
			
		
	}
	public T getCurrent(){
		return current;
	}
	public boolean hasCurrent(){
		return this.getCurrent() != null;
	}

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		if(current != null){ // mess with waiting in update
			current.render(c, g, currentPack);
		}
	}


	@Override
	public void init(GameContainer container) throws SlickException {
	}


	@SuppressWarnings("unchecked")
	@Override
	public void update(GameContainer container, int delta) throws SlickException {

		//System.out.println("updating...");
		
		
		
		
		if(current != null){ // here we will replace current with it's getNext() or set it to null
			Screen next = current.getNext(); // this could be null
			if(next != null){
				if(current != next){
					current = (T) next;
					current.init(container);
				}
				//System.out.println("replaced message");
			} else{
				boolean keep = current.keepUnlessWaiting();
				if(!keep || (keep && waiting.size() > 0)){
					//System.out.println("Set current to null. keepUnlessWaiting: " + keep + " current's class: " + current.getClass().getSimpleName());
					current = null;
				}
			}
			
		}
		if(current != null){ // here we will check if current wants to be replaced only if there is something to replace it with and if so, replace it.
			if(current.keepUnlessWaiting()){
				if(waiting.size() > 0){
					current = waiting.get(0);
					current.init(container);
					waiting.remove(0);
					
				}
			}
		}
		
		if(current != null){ // here we will either update current or if it is null, set it to the next Screen that's waiting in line in the waiting variable
			
			this.current.update(container, delta);
			
			
		} else {
			//System.out.println("message is null");
			if(waiting.size() > 0){
				current = waiting.get(0);
				waiting.remove(0);
				//System.out.println("added message");
				if(current == null){
					System.err.println("Somehow message is null after replacing it from the waiting list. class: " + this.getClass().getName());
					return;
				}
				current.init(container);
			} 
		}
	}


	@Override
	public void stop() {
	}


	@Override
	public Screen getNext() {
		return this;
	}

	public void finishCurrent() {
		if(this.current != null){
			current.stop();
		}
	}
}
