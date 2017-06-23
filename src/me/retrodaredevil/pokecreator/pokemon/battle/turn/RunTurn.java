package me.retrodaredevil.pokecreator.pokemon.battle.turn;

import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BBatteler;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.EndReason;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;

/**
 * @author retro
 *
 */
public class RunTurn implements Turn{

	
	private BattlePokemon out;
	private BBatteler b;
	private int tries;
	
	/**
	 * 
	 * @param runner the trainer that's trying to get away
	 * @param battle the current battle
	 * @param out the current pokemon that's out while using run
	 * @param tries the number of times a trainer has tried to run away
	 */
	public RunTurn(BBatteler runner, Battle battle, BattlePokemon out, int tries){
		this.out = out;
		this.b = runner;
		
		
		this.tries = tries;
		
	}
	
	@Override
	public BBatteler getBBattleer() {
		return b;
	}
	
	
	@Override
	public TurnOutcome doTurn(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle) {
		int a = user.getUnmodifiedStat(StatType.SPEED);
		int b = enemy.get(0).getUnmodifiedStat(StatType.SPEED);
		int c = this.tries;
		
		
		boolean gotAway = (256 * Math.random()) < (	((a * 128.0) / b) + (30.0 * c));
		
		if(gotAway){
			battle.setOver(EndReason.RUN);
		}
		
		return new RunOutcome(gotAway);
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public BattlePokemon getUser() {
		return out;
	}
	@Override
	public String getName() {
		return "run";
	}
	
	public static class RunOutcome implements TurnOutcome{ 
	
		private boolean gotAway;
		
		public RunOutcome(boolean gotAway){
			
			this.gotAway = gotAway;
		}

		@Override
		public List<OutcomePart> getOutcome(Battle battle) {
			
			
			if(this.gotAway){

				return Arrays.asList(new MessagePart(battle, Arrays.asList("You got away safely.")));
			}
			
			
			return Arrays.asList(new MessagePart(battle, Arrays.asList("You couldn't get away!")));
		}
//		public boolean gotAway(){
//			return gotAway;
//		}
		
		
		
		
	}

}
