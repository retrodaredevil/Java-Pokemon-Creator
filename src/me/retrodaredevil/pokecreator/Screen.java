package me.retrodaredevil.pokecreator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public interface Screen {
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException;

	public void init(GameContainer container) throws SlickException;
	public void update(GameContainer container, int delta) throws SlickException;
	
	public void stop();
	
	/**
	 * 
	 * @return the screen to be used next. Returns the current screen object if it should be kept the same. returns null if you should replace it with something else or leave it null
	 */
	public Screen getNext();
	
	
	/**
	 * 
	 * @return true if the you should wait to "throw away" this object until there is something to replace it with. If true, this object should be free to be replaced at any time
	 */
	public default boolean keepUnlessWaiting(){
		return false;
	}

	
	
}
