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
 * This class makes it so it lowers (or raises) one of target's stats
 *
 */
/**
 * @author retro
 *
 */
public class StatusAttack extends Attack{

	private StatType stat;
	private int amount;
	
	
	/**
	 * 
	 * @param stat the stat you want to lower
	 * @param amount the amount you want to raise or lower (if 1 it will raise it by 1 if -2 it will lower it by 2)
	 */
	protected StatusAttack(int power, int percentAcc, Type type, String name, AttackType moveType, int startingPP, Contest contest, int timesHit, StatType stat, int amount) {
		super(power, percentAcc, type, name, moveType, startingPP, contest, timesHit);
		
		this.stat = stat;
		this.amount = amount;
		
		
	}
	
	
	@Override
	protected List<TurnOutcome> hit(BattlePokemon user, BattlePokemon d, int targetSize, Battle battle) {
		List<TurnOutcome> r = new ArrayList<>();
		
		r.addAll(adjustStat(d, stat, amount, battle));
		//System.out.println("Adjusted " + d.getPokemon().getShortName() + "'s " + stat.getName() + " by " + amount);
		
		
		return r;
	}
	
	
	

}
