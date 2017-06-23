package me.retrodaredevil.pokecreator.pokemon.battle.trainer;

import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.trainer.Trainer;
import me.retrodaredevil.pokecreator.util.Pending;

/**
 * 
 * This class is used for any object that needs to choose a move or be in a battle (Like a trainer or wild pokemon)
 *
 */
/**
 * @author retro
 *
 */
public interface Batteler {
	/**
	 * 
	 * @param canDo if the trainer chose a turn that they can't do, this string won't be null and will tell the trainer why they can't do this turn. Otherwise, this is null
	 * @param mon the current pokemon the Batteler is choosing the move for
	 * @return
	 */
	public Pending<Turn> getTurn(String canDo,BattlePokemon mon, boolean canStruggle);
	
	public Battle getBattle();
	
	public default boolean isInBattle(){
		return this.getBattle() != null;
	}
	/**
	 * 
	 * @return if this is a trainer. yeah update please probably needs to be changed when there are npc trainers
	 */
	public default boolean isTrainer(){
		return this instanceof Trainer;
	}
	
	/**
	 * 
	 * @param b the battle to set
	 * @return returns true if the battle is successfully set, false otherwise
	 */
	public boolean setBattle(Battle b);

	public List<Pokemon> getActive();
	public default boolean hasUseablePokemon(){
		
		for(Pokemon p : this.getActive()){
			if(p.isFainted()){
				return false;
			}
		}
		
		return true;
	}
	/**
	 * 
	 * @return returns an array of data where if the first int of the returned value is 0, that pokemon is not fainted, if it is one, it is fainted if second value is 0, not fainted etc
	 */
	public default int[] getPokemonData(){
		List<Pokemon> mon = this.getActive();
		int[] r = new int[mon.size()];
		
		for(int i = 0; i < mon.size(); i++){
			Pokemon p = mon.get(i);
			int put = 0;
			
			if(p.isFainted()){
				put = 1;
			} // TODO implement status effects as 2
			
			r[i] = put;
			
		}
		
		
		return r;
		
	}

	public void tell(TurnOutcome doTurn, Battle battle);
	
	
}
