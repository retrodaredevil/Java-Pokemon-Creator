package me.retrodaredevil.pokecreator.pokemon.attack;

import java.util.ArrayList;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.pokemon.type.Type;


/**
 * 
 * This class makes it so the user will raise one of it's stat when this is called
 * overrides the {@link #attack(BattlePokemon, List, List, Battle)
 */
/**
 * @author retro
 *
 */
public class StatusSelfAttack extends Attack{

	private StatType stat;
	private int amount;
	
	
	/**
	 * 
	 * @param stat the stat you want to raise
	 * @param amount the amount you want to raise or lower (if 1 it will raise it by 1 if -2 it will lower it by 2)
	 */
	protected StatusSelfAttack(int power, int percentAcc, Type type, String name, AttackType moveType, int startingPP, Contest contest, int timesHit, StatType stat, int amount) {
		super(power, percentAcc, type, name, moveType, startingPP, contest, timesHit);
		
		this.stat = stat;
		this.amount = amount;
		
		
	}
	
	@Override
	public List<TurnOutcome> attack(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle) {
		
		List<TurnOutcome> r = new ArrayList<>();
		r.addAll(adjustStat(user, stat, amount, battle));
		
		
		return r;
	}

}
