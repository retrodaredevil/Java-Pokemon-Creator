package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.StatChange;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;

/**
 * @author retro
 *
 */
public class StatOutcome implements TurnOutcome{

	private BattlePokemon mon;
	private int levels;
	private boolean primary;
	private StatType type;
	
	/**
	 * 
	 * @param mon
	 * @param levels
	 * @param primary true if it's a move like leer, make false if it may not happen like with water pulse this should be false so getOutcome will return an empty list
	 */
	public StatOutcome(BattlePokemon mon, int levels, boolean primary, StatType type, boolean rise){
		this.mon = mon;
		this.levels = levels;
		this.primary = primary;
		this.type = type;
	}
	
	
	@Override
	public List<OutcomePart> getOutcome(Battle battle) {
		List<OutcomePart> r = new ArrayList<>();
		if(levels == 0){
			if(!primary){
				return r;
			}
			r.add(new MessagePart(battle, Arrays.asList("But nothing happened!")));
			return r;
		}
		r.add(new StatChange(battle, mon, levels > 0, type));
		
		String name = this.mon.getPokemon().getShortName();
		
		String stat = this.type.getName().toLowerCase();
		
		String message;
		switch(levels){
		case 1: 
			message = name + "'s " + stat + " rose!";
			break;
		case 2: 
			message = name + "'s " + stat + " sharply rose";
			break;
		case 3: 
			message = name + "'s " + stat + " rose drastically!";
			break;
		case -1:
			message = name + "'s " + stat + " fell!";
			break;
		case -2:
			message = name + "'s " + stat + " harshly fell!";
		case -3:
			message = name + "'s " + stat + " severely fell!";
			
		default: message = "I guess nothing happened.";
			break;
		
		}
		
		r.add(new MessagePart(battle, Arrays.asList(message)));
		
		return r;
	}

}
