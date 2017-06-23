package me.retrodaredevil.pokecreator.pokemon.battle;

/**
 * @author retro
 *
 */
public class VStat {

	private static final int max = 6;
	
	private int attack = 0;
	private int defense = 0;
	
	private int spattack = 0;
	private int spdefense = 0;
	
	private int speed = 0;
	
	private int accuracy = 0;
	private int evasivness = 0; // 
	
	
	
	public VStat(){
		// 
	}
	
	/**
	 * 
	 * @param type the stat type
	 * @param amount the amount to increase
	 * @return the amount it rose (usually a number from -3 to 3. This will include 0)
	 */
	public int increase(StatType type, int amount){
		
		int current = this.getStage(type);
		switch(type){
		case ATTACK: attack = this.check(attack + amount); break;
		case DEFENSE: defense = this.check(defense + amount); break;
		case SPATTACK: spattack = this.check(spattack + amount); break;
		case SPDEFENSE: spdefense = this.check(spdefense + amount); break;
		case SPEED: speed = this.check(speed + amount); break;
		case ACCURACY: accuracy = this.check(accuracy + amount); break;
		case EVASIVNESS: evasivness = this.check(evasivness + amount); break;
		case HP:
			System.out.println("Tried increasing the hp stat. Error.");
			return 0;
		default:
			break;
		}
		
		int stage = this.getStage(type);
		System.out.println("raised: " + type.getName() + " by: " + amount);
		return stage - current;
	}
	
	
	private int check(int value){
		int neg = max * -1;
		if(value > max){
			return max;
		} else if(value < neg){
			return neg;
		}
		
		
		return value;
	}
	
	public double getMultiplier(StatType type, int evasStage){ // thanks http://bulbapedia.bulbagarden.net/wiki/Statistic
		int stage = this.getStage(type);
		
		
		
		
		boolean acc = type == StatType.ACCURACY || type == StatType.EVASIVNESS;
		
		if(type == StatType.ACCURACY){
			stage -= evasStage;
		}
		stage = this.check(stage); // so they are capped at -6 and 6	
		if(stage == 0){
			return 1;
		}
		
		if(acc){
			switch(stage){ // this is gen 3 and 4 only
			case -6: return 33/100.0;
			case -5: return 36/100.0;
			case -4: return 43/100.0;
			case -3: return 1/2.0;
			case -2: return 60/100.0;
			case -1: return 75/100.0;
			case 0: return 1.0;
			case 1: return 133/100.0;
			case 2: return 166/100.0;
			case 3: return 2.0;
			case 4: return 250/100.0;
			case 5: return 266/100.0;
			case 6: return 3.0;
			}
			return 1;
		}
		
		switch(stage){ // this is gen3+
		case -6: return 2/8.0;
		case -5: return 2/7.0;
		case -4: return 2/6.0;
		case -3: return 2/5.0;
		case -2: return 2/4.0;
		case -1: return 2/3.0;
		case 0: return 1.0;
		case 1: return 3/2.0;
		case 2: return 4/2.0;
		case 3: return 5/2.0;
		case 4: return 6/2.0;
		case 5: return 7/2.0;
		case 6: return 8/2.0;
		}
		
		return 1.0;
		
	}
	
	
	
	public int getStage(StatType type){
		
		switch(type){
		case ATTACK: return attack;
		case DEFENSE: return defense;
		case SPATTACK: return spattack;
		case SPDEFENSE: return spdefense;
		case SPEED: return speed;
		case ACCURACY: return accuracy;
		case EVASIVNESS: return evasivness;
		default:
			break;
		}
		//System.out.println("Problem in VStat. Did not find the correct variable to return in getStage. This should never happen but if this is the only time, it's not that big of a deal.");
		
		throw new IllegalArgumentException("StatType: " + type.getName() + " is not supported while getting a stat value in the VStat class.");
		
		
	}
	
	
	public static enum StatType {
		ATTACK("attack"),
		DEFENSE("defense"),
		SPATTACK("sp. attack"),
		SPDEFENSE("sp. defense"),
		SPEED("speed"),
		ACCURACY("accuracy"),
		EVASIVNESS("evasivness"),
		HP("hp");
		;
		
		private String name;
		
		private StatType (String name){
			this.name= name;
		}
		
		
		public String getName(){
			return name;
		}
		
		
	}
	
}
