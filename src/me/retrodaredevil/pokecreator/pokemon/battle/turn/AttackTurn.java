package me.retrodaredevil.pokecreator.pokemon.battle.turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.attack.Attack;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackOutcome;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BBatteler;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;

/**
 * @author retro
 *
 */
public class AttackTurn implements Turn{

	
	private Attack attack;
	private BattlePokemon mon;
	private BBatteler b;
	
	public AttackTurn(Attack attack, BattlePokemon battlePokemon, BBatteler b){
		this.attack = attack;
		this.mon = battlePokemon;
		this.b = b;
	}
	@Override
	public BBatteler getBBattleer() {
		return b;
	}
	
	
	@Override
	public TurnOutcome doTurn(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle) {
		List<TurnOutcome> a = attack.attack(user, team, enemy, battle);
		boolean b = user.sayUsed();
		return new AttackOutcomes(a, user, attack.getName().toLowerCase(), b);
	}
	
	
	@Override
	public String getName() {
		return attack.getName();
	}



	@Override
	public BattlePokemon getUser() {
		return mon;
	}

	@Override
	public int getPriority() {
		return attack.getPriority();
	}

	public static class AttackOutcomes implements TurnOutcome {

		private List<TurnOutcome> list;
		private BattlePokemon user;
		private String name;
		private boolean sayUsed;
		
		public AttackOutcomes(List<TurnOutcome> list, BattlePokemon user, String attackName, boolean sayUsed){
			this.list = list;
			this.user = user;
			this.name = attackName;
			this.sayUsed = sayUsed;
		}
		
		


		@Override
		public List<OutcomePart> getOutcome(Battle battle) {
			List<OutcomePart> r = new ArrayList<>();
			if(sayUsed){
				r.add(new MessagePart(battle, Arrays.asList(this.user.getPokemon().getShortName() + " used " + name)));
			}

			for(TurnOutcome a : list){
				r.addAll(a.getOutcome(battle));
			}
			
			
			return r;
		}
		
	}
	
	
}
