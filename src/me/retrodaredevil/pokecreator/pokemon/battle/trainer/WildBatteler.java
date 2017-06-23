package me.retrodaredevil.pokecreator.pokemon.battle.trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack;
import me.retrodaredevil.pokecreator.pokemon.attack.AttackList;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.AttackTurn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.util.Pending;
import me.retrodaredevil.pokecreator.util.Pending.SetPending;
import me.retrodaredevil.pokecreator.util.Randomizer;

/**
 * @author retro
 *
 */
public class WildBatteler implements Batteler{

	private Battle battle;
	
	private final List<Pokemon> active;
	
	public WildBatteler(Battle battle, Pokemon mon){
		this.battle = battle;
		this.active = Arrays.asList(mon);
	}
	
	private List<Attack> banned = new ArrayList<>();
	
	@Override
	public Pending<Turn> getTurn(String canDo, BattlePokemon mon, boolean canStruggle) {
		if(canDo == null){
			banned.clear();
		}
		SetPending<Turn> r = new SetPending<>();
		if(canStruggle){
			r.setAnswer(new AttackTurn(AttackList.STRUGGLE, mon, battle.getBBatteler(this)));
			return r;
		}
		List<Attack> attacks = mon.getPokemon().getAttacks();
		
		Attack set = null;
		while(set == null || banned.contains(set)){
			set = attacks.get(Randomizer.getRandom().nextInt(attacks.size()));
		}
		r.setAnswer(new AttackTurn(set, mon, battle.getBBatteler(this)));
		
		return r;
	}
	@Override
	public void tell(TurnOutcome doTurn, Battle battle) {
	}

	@Override
	public Battle getBattle() {
		return battle;
	}

	@Override
	public List<Pokemon> getActive() {
		return active;
	}

	@Override
	public boolean setBattle(Battle b) {
		this.battle = b;
		return true;
	}
	
}
