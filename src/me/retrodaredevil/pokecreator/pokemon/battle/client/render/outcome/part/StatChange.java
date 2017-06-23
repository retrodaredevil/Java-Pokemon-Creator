package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public class StatChange extends OutcomePart{

	
	private boolean raise;
	private BattlePokemon mon;
	private StatType type;
	
	
	public StatChange(Battle battle, BattlePokemon mon, boolean raise, StatType type) {
		super(battle);
		this.mon = mon;
		this.raise = raise;
		this.type = type;
		
		
	}
	

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
	}

	@Override
	public Screen getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
	}

}
