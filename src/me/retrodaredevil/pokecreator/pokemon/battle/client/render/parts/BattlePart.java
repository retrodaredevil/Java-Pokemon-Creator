package me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;

/**
 * The object for a part of the battle screen like the text on screen
 *
 */
/**
 * @author retro
 *
 */
public abstract class BattlePart implements Screen {

	protected BattleAnimation animation = null;
	
	protected BattlePart(BattleAnimation an){
		this.animation = an;
	}
	
	
	public BattleAnimation getBattleAnimation(){
		return animation;
	}
	
	
	public abstract boolean isDone();
	
	
	
	
}
