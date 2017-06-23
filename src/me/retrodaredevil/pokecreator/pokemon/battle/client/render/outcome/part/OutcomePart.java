package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.ImageEdit;

/**
 * @author retro
 *
 */
public abstract class OutcomePart implements Screen, ImageEdit {

	protected final Battle battle;
	
	public OutcomePart(Battle battle){
		this.battle = battle;
	}
	
	

	
	/**
	 * return null when done, return another OutcomePart to switch to that, return this to keep.
	 */
	@Override
	public abstract Screen getNext();
	
	/**
	 * fire this to stop the current object. May not be implemented in most classes extending OutcomePart
	 */
	@Override
	public void stop() {
	}

	/**
	 * used to initialize things like adding text to animation. Not to be used in a server side operation.
	 */
	@Override
	public abstract void init(GameContainer container) throws SlickException;
	
	/**
	 * used to update things. Note to be used in a server side operation.
	 */
	@Override
	public abstract void update(GameContainer container, int delta) throws SlickException;

	@Override
	public String toString() {
		return "OutcomePart{data:'" + super.toString() + "'}";
	}

}
