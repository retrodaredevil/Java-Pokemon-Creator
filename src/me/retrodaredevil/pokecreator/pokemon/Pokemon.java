package me.retrodaredevil.pokecreator.pokemon;


import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.attack.Attack;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackOutcome;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackType;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.type.Type;
import me.retrodaredevil.pokecreator.pokemon.type.TypeSet;
import me.retrodaredevil.pokecreator.trainer.Trainer;
import me.retrodaredevil.pokecreator.util.Randomizer;

/**
 * @author retro
 *
 */
public class Pokemon {

	protected PType type;
	protected Stat ivs; // TODO implement evs
	
	protected Trainer owner;
	
	private int maxHP;
	private int hp;
	private int level;
	
	private List<Attack> moves;
	
	private String name;
	
	public Pokemon(String nickName, PType type, Stat ivs, Trainer owner, int hp, int maxHP, int level, List<Attack> moves){
		this.name = nickName;
		
		this.type = type;
		this.ivs = ivs;
		this.owner = owner;
		
		this.level = level;
		this.moves = moves;
		
		if(maxHP <= 0){
			maxHP = this.getStat(StatType.HP);
			hp = maxHP;
		}
		this.hp = hp;
		this.maxHP = maxHP;
		
		if(type == null){
			throw new NullPointerException("type is null");
		}
		if(ivs == null){
			throw new NullPointerException("ivs is null");
		}
	}
	
	public int getStat(StatType type){
		Stat ivs = this.getIVs();
		int base = this.getPokemonType().getBaseStat().getValue(type);
		int iv = ivs.getValue(type);
		System.out.println("base: " + base + " iv: " + iv + " level: " + this.getLevel());
		
		return Stat.getValue(base, iv, this.getLevel(), type == StatType.HP);
	}
	
	
	public boolean isActive(){
		return owner != null && owner.getActive().contains(this);
	}
	
	public String getNickName(){
		return name;
	}
	
	public Stat getIVs(){
		return ivs;
	}
	public PType getPokemonType(){
		return type;
	}
	
	
	public int getLevel() {
		return level;
	}
	public int getHP(){
		return hp;
	}
	public int getMaxHP(){
		return maxHP;
	}
	
	public List<Attack> getAttacks() {
		return this.moves;
	}
	
	public boolean hasOwner(){
		return this.getOwner() != null;
	}
	public Trainer getOwner(){
		return owner;
	}
	
	
	/**
	 * This method should be used when getting the multiplier everytime because we can implement abilities into the game easily.
	 * @param type the type of the attack hitting the pokemon
	 * @return the multiplier for the damage
	 */
	public double getMultiplier(Type type){
		double r = 1;
		
		for(Type t : this.getPokemonType().getTypeSet().getTypes()){
			if(type.doesNoDamage(t)){
				r *=0;
				return r;
			} else if(type.isSuperEffective(t)){
				r*=2;
			} else if(type.isNotEffective(t)){
				r/=2;
			}
		}
		
		return r;
	}
	
	/**
	 * Please read all the parameters to see how this method works.
	 * 
	 * @param user the pokemon that's attacking
	 * @param hit the battlepokemon that's recieving the hit. It should return this Pokemon when you call {@link BattlePokemon#getPokemon()}
	 * 
	 * @param hitNumber the current hitNumber. If there is only going to be one hit, this should be -1
	 * @param maxHits the total number of hits the attack will be called. this should be a number from 1 to 5 if it's a multi strike move it should not be one
	 * 
	 * @param damage1 amount of damage to apply
	 * @param moveType the type of the move being used - used to adjust the damage multiplier
	 * @param attack the attack stat of the attacking pokemon
	 * @param spattack the spattack stat of the attacking pokemon
	 * @param level the level of the attacking pokemon
	 * @param pokemonType the type of the attacking pokemon
	 * @param attackType the attack type (PYSCHICAL, SPECIAL, STATUS) Probably should never be status. Update this if it could be
	 * @param percentAcc the accuracy of the attack
	 */ //int damage1, Type moveType, int attack, int spattack,  int level, TypeSet pokemonType, AttackType attackType, int percentAcc, int baseSpeed, boolean highCriticalHit, int hitNumber
	public AttackOutcome damage(BattlePokemon user, BattlePokemon hit, String name, int power, Type moveType, TypeSet attackerType, AttackType attackType, int percentAcc, boolean highCriticalHit, int hitNumber, int maxHits){ // TODO implement returning if attack worked
		// used http://bulbapedia.bulbagarden.net/wiki/Damage 
		// Implement Weather, Abilities
		
		
//		if(hitNumber <= 1 && !Randomizer.getTrue(percentAcc * user.getAccuracyMultiplier())){
//			return new AttackOutcome(user, hit, name, true, false, false, false, false, 0, hitNumber, maxHits, this.getHP());
//		}
		
		int mult = 1;
		if(highCriticalHit){
			mult = 8;
		}
		boolean critical = Randomizer.getRandom().nextInt(256) < (this.getPokemonType().getBaseStat().getSpeed() / 2) * mult; // in gen 1
		//new AttackOutcome(failed, critical, noDamage, notVeryEffective, superEffective)
		boolean noDamage = false;
		boolean notVeryEffective = false;
		boolean superEffective = false;
		double modifier = 1;
		
		
		{// type effectiveness
			double m = this.getMultiplier(moveType);
			modifier *= m;
			if(m == 0){
				noDamage = true;
			} else if(m < 1){
				notVeryEffective = true;
			} else if(m > 1){
				superEffective = true;
			}
		}
		
		{ // critical
			if(critical){
				modifier *= 2; 
			}
		}
		{// same type attack bonus implementation
			if(attackerType.hasType(moveType)){
				modifier *= 1.5; // implement Adaptability ability
			}
		}

		modifier *= (Randomizer.getRandom().nextInt(26) + 85.0) / 100.0; // number between 85 and 100
		
		double thisdefense; // this makes it so we use or don't use sp values
		double aattack;
		if(attackType == AttackType.SPECIAL){
			thisdefense = hit.getStat(StatType.SPDEFENSE); // stat is the ivs of this class
			aattack = user.getStat(StatType.SPATTACK);
		} else {
			thisdefense = hit.getStat(StatType.DEFENSE);
			aattack = user.getStat(StatType.ATTACK);
		}
		int attackerLevel = user.getPokemon().getLevel();
		
		double damage = (	((2.0 * attackerLevel + 10) / 250.0) * (aattack / thisdefense) * power + 2	) * modifier; // hopefully someone can read this. // all gen's formula
		
		System.out.println("Defender: " + this.getShortName() + ", attackerLevel: " + attackerLevel + " power: " + power + " thisdefense: " + thisdefense + " aattack: " + aattack + " damage: " + damage);
		
		this.damage((int) Math.ceil(damage));
		return new AttackOutcome(user, hit,name, false, critical, noDamage, notVeryEffective, superEffective, (int) damage, hitNumber, maxHits, this.getHP());
	}
	
	public void damage(int amount){
		this.hp -= amount;
		if(hp <0){
			hp = 0;
		}
	}
	public void heal(int amount){
		hp += amount;
		if(hp > maxHP){
			hp = maxHP;
		}
	}
	
	public boolean isFainted(){
		return hp <= 0;
	}

	public boolean isShiny() {
		// TODO Auto-generated method stub
		return false;
	}



	/**
	 * 
	 * @return if this pokemon has a nick name returns that, if not returns the pokemon's type name in upper case
	 */
	public String getShortName() {
		String nick = this.getNickName();
		if(nick != null){
			return nick;
		}
		return this.getPokemonType().getName().toUpperCase();
	}

	public void fullyHeal() {
		this.hp = maxHP;
	}
	
}
