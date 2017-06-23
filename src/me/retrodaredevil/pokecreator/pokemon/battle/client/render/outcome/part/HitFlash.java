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
public class HitFlash extends OutcomePart{

	private BattlePokemon mon;
	
	
	
	public HitFlash(Battle battle, BattlePokemon mon) {
		super(battle);
		this.mon = mon;/////////////////////////////////////////////////////////////////
		
	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
	}

	@Override
	public Screen getNext() {
		
		return null;
	}
	@Override
	public String toString() {
		
		return "HitFlash{}";
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
