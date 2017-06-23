package me.retrodaredevil.pokecreator.pokemon.attack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.DamageUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.FaintUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.HitFlash;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.pokemon.type.Type;

/**
 * @author retro
 *
 */
public class RecoilAttack extends Attack {


	private final double recoilMult;
	
	protected RecoilAttack(int power, int percentAcc, Type type, String name, AttackType moveType, int startingPP, Contest contest, int timesHit, double recoilMult) {
		super(power, percentAcc, type, name, moveType, startingPP, contest, timesHit);
		this.recoilMult = recoilMult;
	}
	@Override
	protected List<TurnOutcome> onAttack(BattlePokemon attacker, BattlePokemon defender, AttackOutcome outcome, Battle battle, int hitNumber, int maxHits) {
		int damage = outcome.getDamage();
		int recoil = (int) (damage * recoilMult);
		
		if(damage > 0 && recoil < 1){
			recoil = 1;
		}
		
		
		
		List<TurnOutcome> r = new ArrayList<>();
		
		if(recoil > 0){
			attacker.getPokemon().damage(recoil);
			r.add(new RecoilOutcome(attacker, attacker.getPokemon().isFainted(), attacker.getPokemon().getHP(), recoil, damage));
		}
		
		
		return r;
	}
	
	
	public static class RecoilOutcome implements TurnOutcome {

		private BattlePokemon user;
	
		private boolean faint;
		private int hp;
		
		
		private RecoilOutcome(BattlePokemon user, boolean faint, int hp, int recoil, int attackDamage){
			this.user = user;
			this.faint = faint;
			this.hp = hp;
			
			
		}
		
		
		@Override
		public List<OutcomePart> getOutcome(Battle battle) {
			List<OutcomePart> r = new ArrayList<>();
			
			
			r.add(new MessagePart(battle, Arrays.asList(user.getPokemon().getShortName() + " is damaged by the recoil.")));
			
			r.add(new HitFlash(battle, this.user)); 
			r.add(new DamageUpdate(battle, this.user, hp));
			
			if(this.isFainted()){
				r.add(new MessagePart(battle, Arrays.asList(user.getPokemon().getShortName() + " fainted!")));
				r.add(new FaintUpdate(battle, this.user));
			}
			return r;
		}
		
		public boolean isFainted(){
			return faint;
		}
		
	}

}
