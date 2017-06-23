package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public class FaintUpdate extends OutcomePart{

	
	private double percent = 0;
	
	public FaintUpdate(Battle battle, BattlePokemon update) {
		super(battle);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isStartingToFaint(){
		return this.percent > 0;
	}
	public boolean isTotallyFainted(){
		return this.percent >= 100;
	}
	public double getPercentFainted(){
		return this.percent;
	}
	public void setPercentFainted(double percent){
		this.percent = percent;
	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Screen getNext() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {

		return "FaintUpdate{}";
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
