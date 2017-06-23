package me.retrodaredevil.pokecreator.trainer.controller;

import org.newdawn.slick.GameContainer;

import me.retrodaredevil.pokecreator.trainer.Trainer;

/**
 * @author retro
 *
 */
public abstract class Controller  {

	protected Trainer trainer;
	
	
	public Controller(Trainer trainer){
		this.trainer = trainer;
	}
	
	
	
	public abstract Direction run(GameContainer container, int delta);
	
}
