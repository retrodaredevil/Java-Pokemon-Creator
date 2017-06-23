package me.retrodaredevil.pokecreator.pokemon.attack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackOutcome;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.DamageUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.FaintUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.HitFlash;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.pokemon.type.Type;
import me.retrodaredevil.pokecreator.util.Randomizer;


/**
 * @author retro
 *
 */
public  class Attack {
	
	private final String name;
	protected final int percentAcc;
	protected final int power;
	
	private final int pp;
	private final int maxPP;
	
	protected final Type type;
	protected final AttackType moveType;
	
	protected final Contest contest;
	
	private boolean highCriticalHit;
	private final int timesHit;
	
	private final int priority = 0;
	
	private final AimType aimType = AimType.ONE_ENEMY;
	
	
	
	
	protected Attack(int power, int percentAcc, Type type, String name, AttackType moveType, int startingPP, Contest contest, int timesHit){
		this.power = power;
		this.percentAcc = percentAcc;
		this.name = name;
		this.type = type;
		this.moveType = moveType;
		
		this.pp = startingPP;
		this.maxPP = (int) (pp * 1.6);
		
		this.contest = contest;
		this.timesHit = timesHit;
	}
	
	public int getStartingPP(){
		return pp;
	}
	
	public int getMaxPP(){
		return maxPP;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean hasHighCriticalHit(){
		return this.highCriticalHit;
	}
	
	/**
	 * 
	 * @return the maximum amount of times this move can hit another pokemon
	 */
	public int getTimesHit(){
		return timesHit;
	}
	
	/**
	 * Is called right after the user attacked a pokemon and the returned value is added after the outcome is added. Will return null unless overriden
	 * Called after the damage method is called (if it should be called)
	 * @param attacker the attack (the one who's doing this move)
	 * @param defender the pokemon that's receiving this move
	 * @param outcome
	 * @param battle
	 */
	protected List<TurnOutcome> onAttack(BattlePokemon attacker, BattlePokemon defender, AttackOutcome outcome, Battle battle, int hitNumber, int maxHits){
		return null;
	}

	/**
	 * fires onAttack
	 * @param attacker
	 * @param defender
	 */
	public List<TurnOutcome> attack(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle){
		user.setSayUsed(true);
		
		List<BattlePokemon> targets = this.aimType.getAim(user, team, enemy);
		int size = targets.size();
		
		List<TurnOutcome> r = new ArrayList<>();
		
		
		List<TurnOutcome> before = this.beforeAttacks(user, team, enemy, targets);
		if(before != null){
			r.addAll(before);
		}
		for(BattlePokemon p : targets){
			r.addAll(this.hit(user, p, size, battle));
		}
		List<TurnOutcome> after = this.afterAttacks(user, team, enemy, targets, r);
		if(after != null){
			r.addAll(after);
		}
		
		
		return r;
	
	}
	/**
	 * 
	 * called before a move has happened
	 */
	protected List<TurnOutcome> beforeAttacks(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, List<BattlePokemon> targets){
		return null;
	}
	/**
	 * 
	 * Is called once after a move is done
	 */
	protected List<TurnOutcome> afterAttacks(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, List<BattlePokemon> targets, List<TurnOutcome> currentOutcomes){
		return null;
	}
	
	protected List<TurnOutcome> hit(BattlePokemon user, BattlePokemon d, int targetSize, Battle battle){
		
		List<TurnOutcome> all = new ArrayList<>();
		
		double random = Randomizer.getRandomDouble(0, 100);
		if(this.getPower() > 0){
			List<TurnOutcome> attack = this.attack(user, d, targetSize, battle, random);
			all.addAll(attack);
			
			
		}
		
		return all;
	}
	
	
	
	protected final List<TurnOutcome> attack(BattlePokemon user, BattlePokemon d, int targetSize, Battle battle, double random){
		Pokemon attacker = user.getPokemon();
		Pokemon defender = d.getPokemon();
		
		if(!doesHit(user, d, random)){
			return new ArrayList<>(Arrays.asList(getMissedOutcome(user, d, this)));
		}
		
		int amount = 1;
		
		
		if(this.timesHit > 1){ // calculating how many times the move will hit if it is a multi strike move
			int rand = (int) (Math.random() * 8);
			if(rand < 3){
				amount = 2;
			} else if(rand < 6){
				amount = 3;
			} else if(rand < 7){
				amount = 4;
			} else if(rand < 8){
				amount = 5;
			}
		}

		
		
		List<TurnOutcome> outcomes = new ArrayList<>();
		for(int i = 0; i < amount; i++){
			int hitNumber = -1;
			if(amount > 1){
				hitNumber = i + 1;
			}
			AttackOutcome outcome = defender.damage(user, d, this.getName(), this.getPower(), this.getType(),  attacker.getPokemonType().getTypeSet(), this.getAttackType(), percentAcc, highCriticalHit, hitNumber, amount);
			
			outcomes.add(outcome);
			List<TurnOutcome> more = this.onAttack(user, d, outcome, battle, hitNumber, amount);
			if(more != null){
				outcomes.addAll(more);
			}
			if(outcome.noDamage || outcome.failed || outcome.isFainted()){
				break;
			}
			if(i + 1 < amount){
				outcome.superEffective = false; // so we only display this at the end it.
				outcome.notVeryEffective = false;
			}
			
		}
		

		return outcomes;
	}
	
	protected final List<TurnOutcome> adjustStat(BattlePokemon mon, StatType type, int levels, Battle battle){
		List<TurnOutcome> r = new ArrayList<>();
		r.addAll(mon.adjustStat(type, levels, battle, this.getAttackType() == AttackType.STATUS));
		
		
		
		return r;
	}
	
	
	public int getPower() {
		return this.power;
	}
	
	public AttackType getAttackType() {
		return this.moveType;
	}
	public Type getType(){
		return this.type;
	}
	public int getPriority() {
		return this.priority;
	}
	
	/**
	 * Usually will return false and is only called if the pokemon has already used this move and if it owns this move (with a possible exception to struggle (update if struggle is called))
	 * Also returns false unless overriden
	 * @param mon the pokemon to test
	 * @return true if the pokemon is forced to use this move (for a move like solarbeam or hyper beam it would force you to use this move twice in a row)
	 * 
	 */
	public boolean forceUse(BattlePokemon mon){
		return false;
	}
	/**
	 * 
	 * @param user the pokemon using the move 
	 * @param hit the pokemon being hit
	 * @param randomValue a double from 0 to 100 not including 100
	 * @return
	 */
	protected final boolean doesHit(BattlePokemon user, BattlePokemon hit, double randomValue){
		
		double acc = user.getAccuracyMultiplier(hit);
		
		
		return this.percentAcc * acc > randomValue;
	}
	
	
	public static class AttackOutcome implements TurnOutcome{
		
		
		private int damage;
		
		private int timesHit;
		private int maxHits;
		
		private boolean failed;
		private boolean critical;
		private boolean noDamage;
		private boolean notVeryEffective;
		private boolean superEffective;
		//private boolean fainted;/////////////////////////
		
		private int hp;
		
		
		private BattlePokemon hit;
		
		
		/**
		 * @param name this parameter is not being used but may be used in the future. It is the name of the attack.
		 * @param timesHit the amount of times the move hit. If the move is not a multi strike move, this should be -1
		 */
		public AttackOutcome(BattlePokemon user, BattlePokemon hit, String name, boolean failed, boolean critical, boolean noDamage, boolean notVeryEffective, boolean superEffective, int damage, int timesHit, int maxHits, int hp){ // TODO implement the effects applied to the mon
			
			
			this.hit = hit;
			
			
			this.failed = failed;			
			this.critical = critical;
			this.noDamage = noDamage;
			this.notVeryEffective = notVeryEffective;
			this.superEffective = superEffective;
			this.damage = damage;
			this.timesHit = timesHit;
			this.maxHits = maxHits;
			
			this.hp = hp;
			

		}
		public boolean isFainted(){
			return hp <= 0;
		}
		public int getHP(){
			return hp;
		}
		
		
		@Override
		public List<OutcomePart> getOutcome(Battle battle){ // TODo make it so in a double battle, "it's super effective!" is "it's super effective against <pokemon>!"
			List<OutcomePart> r = new ArrayList<>();
			
			
			if(noDamage){
				r.add(new MessagePart(battle, Arrays.asList("It doesn't affect this pokemon.")));
				return r;
			} 
			
			if(failed){
				r.add(new MessagePart(battle, Arrays.asList("The attack missed!")));
				return r;
			} else{
				r.add(new HitFlash(battle, this.hit)); 
				r.add(new DamageUpdate(battle, this.hit, hp));
			}
			
			
			{
				List<String> damage = new ArrayList<>();
				if(critical && !noDamage){
					damage.add("A critical hit!");
				}
				if(notVeryEffective){
					damage.add("It's not very effective.");
				} else if(superEffective){
					damage.add("It's super effective!");
				}

				if(timesHit > 0 && (this.timesHit == this.maxHits || this.isFainted())){
					damage.add("Hit " + timesHit + " time" + (timesHit > 1 ? "s" : "") + "!");
				}

				if(damage.size() > 0){
					r.add(new MessagePart(battle, damage));
				}
			}
			
		
			if(this.isFainted()){
				r.add(new MessagePart(battle, Arrays.asList(hit.getPokemon().getShortName() + " fainted!")));
				r.add(new FaintUpdate(battle, this.hit));
			}
			
			
			return r;
		}
		
		public int getDamage(){
			return damage;
		}
		public boolean wasCritical(){
			return critical;
		}
		public boolean missed() {
			return failed;
		}
		
		
	}
	
	

	
	protected static AttackOutcome getMissedOutcome(BattlePokemon user, BattlePokemon hit, Attack attack){
		int hitNumber = -1;
		int maxHits = attack.getTimesHit();
		if(maxHits > 1){
			hitNumber = 1;
		}
		return new AttackOutcome(user, hit, attack.getName(), true, false, false, false, false, 0, hitNumber, maxHits, hit.getPokemon().getHP());
	}

	public static enum AimType {
		ALL_BUT_USER(){
			@Override
			public List<BattlePokemon> getAim(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy) {
				List<BattlePokemon> r = new ArrayList<>();
				r.addAll(team);
				r.remove(user);
				r.addAll(enemy);
				
				return r;
			}
		},
		
		@Deprecated
		ALL(),
		ONE_ENEMY(){
			@Override
			public List<BattlePokemon> getAim(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy) {
				BattlePokemon aim = user.getAim();
				if(aim == null){
					return Arrays.asList(enemy.get(0));
				}
				return Arrays.asList(aim);
			}
			
		},
		BOTH_ENEMIES(){
			@Override
			public List<BattlePokemon> getAim(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy) {
				return new ArrayList<>(enemy);
			}
			
		},
		
		@Deprecated
		ONE_ENEMY_BOTH_TEAM(),
		
		@Deprecated
		ONE_ENEMY_ONE_TEAM();
		
		public List<BattlePokemon> getAim(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy){
			List<BattlePokemon> r = new ArrayList<>();
			BattlePokemon aim = user.getAim();
			
			if(enemy.contains(aim)){
				r.add(aim);
				return r;
			}
			r.add(enemy.get(0));
			return r;
			
			
		}
		
	}

	public static enum AttackType{
		PHYSICAL,SPECIAL,STATUS
	}
	public static enum Contest {
		COOL, BEAUTIFUL, CUTE, CLEVER, TOUGH
	}
	///////
}
