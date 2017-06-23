package me.retrodaredevil.pokecreator.pokemon;

import java.util.ArrayList;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.util.Randomizer;


/**
 * @author retro
 *
 */
public class Stat {

	public static enum OutOf {
		IV(31), BASE(255), NONE(0)
		;
		
		private int of;
		
		private OutOf(int of){
			this.of = of;
		}
		public int getOutOf(){
			return of;
		}
	}
	
	private final int hp;
	private final int attack;
	private final int defense;
	
	private final int spattack;
	private final int spdefense;
	
	private final int speed;
	
	private final int of;
	
	
	public Stat(int hp, int attack, int defense, int spattack, int spdefense, int speed, int of){
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spattack = spattack;
		this.spdefense = spdefense;
		this.speed = speed;
		
		this.of = of;
	}
	public Stat(int hp, int attack, int defense, int spattack, int spdefense, int speed, OutOf of){
		this(hp, attack, defense, spattack, spdefense, speed, of.getOutOf());
	}
	
	public Stat(long hp, long attack, long defense, long spattack, long spdefense, long speed, OutOf base) {
		this((int)hp, (int) attack, (int) defense, (int) spattack, (int) spdefense, (int) speed, base);
	}
	@Override
	public String toString() {
		return "Stat{hp:" + hp +",attack:" + attack + ",defense:" + defense + ",spattack:" + spattack + ",spdefense:" + spdefense + ",speed:" + speed + ",outof:" + of + "}";
	}
	
	
	
	public int getHP(){
		return hp;
	}
	
	public int getAttack(){
		return attack;
	}
	public int getDefense(){
		return defense;
	}
	public int getSPAttack(){
		return spattack;
	}
	public int getSPDefense(){
		return spdefense;
	}
	public int getSpeed(){
		return speed;
	}
	
	public int getValue(StatType type){
		
		switch(type){
		case ATTACK: return this.getAttack();
		case DEFENSE: return this.getDefense();
		case SPATTACK: return this.getSPAttack();
		case SPDEFENSE: return this.getSPDefense();
		case SPEED: return this.getSpeed();
		case HP: return this.getHP();
		default:
			break;
		
		}
		throw new IllegalArgumentException("StatType: " + type.getName() + " is not supported while getting a stat value in the Stat class.");
		
	}
	
	public int getOutOf(){
		return this.of;
	}
	
	public static int getValue(int base, int iv, int level, boolean hp){
		int ev = 0;
		return (int) (((((base + iv) * 2 + ev) * level) / 100.0) + 5 + level + (hp ? 5 : 0));
	}
	
	
	
	/**
	 * uses the outOf variable to determine how good the current stats are. (If all variables are outOf this will return 100)
	 * @return
	 */
	public double getPercent(){
		int of = this.of;
		if(of == 0){
			return 100;
		}
		int outOf = of * 6;
		int i = hp + attack + defense + spattack + spdefense + speed;
		return i / outOf;
	}
	
	
	
	public static class StatJudge {
		public static enum Goodness {
			REALLYBAD, BAD, OK, GOOD, AMAZING, PERFECT, BEOND;
		}
//		public static enum StatType{
//			HP, ATTCK, DEFENSE, SPATTACK, SPDEFENSE, SPEED;
//		}
		
		
		private Stat stat;
		
		public StatJudge(Stat stat){
			this.stat = stat;
		}
		public Goodness getGoodness(){
			double percent = stat.getPercent();
			if(percent > 100){
				return Goodness.BEOND;
			} else if(percent == 100){
				return Goodness.PERFECT;
			} else if(percent > 85){
				return Goodness.AMAZING;
			} else if(percent > 75){
				return Goodness.GOOD;
			} else if(percent > 50){
				return Goodness.OK;
			} else if(percent > 25){
				return Goodness.BAD;
			}
			return Goodness.REALLYBAD;
		}
		
		/**
		 * 
		 * @return returns the best stats (if a person has an attack of 100, a defense of 100 and all other stats are 10, it would return ATTACK and DEFENSE in the list)
		 */
		public List<StatType> bestStats(){
			int hp = stat.getHP();
			int attack = stat.getAttack();
			int defense = stat.getDefense();
			int spattack = stat.getSPAttack();
			int spdefense = stat.getSPDefense();
			int speed = stat.getSpeed();
			
			StatType[] stats = StatType.values();
			int[] values = {hp, attack, defense, spattack, spdefense, speed};
			
			List<StatType> best = new ArrayList<>();
			int current = 0;
			
			for(int i = 0; i < values.length && i < stats.length; i++){
				StatType type = stats[i];
				int value = values[i];
				if(value > current){
					best.clear();
				}
				if(value >= current){
					best.add(type);
					current = value;
				}
			}
			return best;
		}
		
		
		
	}



	public static Stat random(OutOf of) {
		int outOf = of.getOutOf();
		return new Stat(getRandom(outOf), getRandom(outOf), getRandom(outOf), getRandom(outOf), getRandom(outOf), getRandom(outOf), of);
	}
	private static int getRandom(int outOf){
		return Randomizer.getRandom().nextInt(outOf) + 1;
	}
	
	
}

