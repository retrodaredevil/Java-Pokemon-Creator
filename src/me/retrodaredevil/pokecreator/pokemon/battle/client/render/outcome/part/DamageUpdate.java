package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.HPBarPart;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public class DamageUpdate extends OutcomePart{

	private final BattlePokemon update;
	
	private final HPBarPart hp;
	private float target;
	
	private final int targetHP;
	
	private boolean done = false;
	
	public DamageUpdate(Battle battle, BattlePokemon update, int target) {
		super(battle);
		
		
		this.update = update;
		this.targetHP = target;
		
		hp = battle.getBattleAnimation().getHPBar(update);
	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		
	}
	@Override
	public String toString() {
		return "DamageUpdate{}";
	}

	@Override
	public Screen getNext() {
		if(!done){
			return this;
		}
		return null;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		Pokemon mon = update.getPokemon();
		target = ((float) (targetHP * 100f) / mon.getMaxHP());
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {

		final int max = update.getPokemon().getMaxHP();
		
		
		float current = hp.getCurrentPercent();
		//System.out.println("first current: " + current);
		current -= 0.03 * delta;
		if(current < target){
			current = target;
			//System.out.println("Setting current to: " + target);
			done = true;
			hp.setHP(this.targetHP);
		}
		hp.setCurrentPecent(current);
		

		int amount = (int) ((float) max * (current / 100f));
		if(amount > max){
			amount = max;
			//System.out.println("rounding amount down to max hp");
		} else if(amount < update.getPokemon().getHP()){
			amount = update.getPokemon().getHP();
			//System.out.println("Rounding amount up to current hp");
		}
		//System.out.println("amount: " + amount + " percent: " + current + " targetHP: " + targetHP);
		hp.setHP(amount);

		
		//System.out.println("Current: hp:" + amount + " current: " + current);
	}

}
