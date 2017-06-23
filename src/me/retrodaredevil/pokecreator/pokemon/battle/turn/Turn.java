package me.retrodaredevil.pokecreator.pokemon.battle.turn;

import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BBatteler;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;

/**
 * @author retro
 *
 */
public interface Turn {

	/**
	 * 
	 * @param user the pokemon using this move (or the pokemon being healed or the pokemon that's running away)
	 * @param team all the pokemon on the user's team (this includes the user)
	 * @param enemy all the enemies of the pokemon
	 * @param battle the battle
	 * @return returns the turn outcome
	 */
	public TurnOutcome doTurn(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle);
	
	public int getPriority();
	
	/**
	 * 
	 * @return return the user of the turn. This should not be null but using an item may return a pokemon that's not in battle
	 */
	public BattlePokemon getUser();
	
	public BBatteler getBBattleer();
	
	public static interface TurnOutcome {
		public List<OutcomePart> getOutcome(Battle battle);
	}

	public String getName();
	
	
}
