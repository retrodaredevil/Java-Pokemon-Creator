package me.retrodaredevil.pokecreator.pokemon.attack.learn;

import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.attack.Attack;

/**
 * @author retro
 *
 */
public class LearnSet {

	private List<AttackLearn> learn;
	private List<Attack> all;
	
	
	public LearnSet(List<AttackLearn> learn, List<Attack> possible){
		this.learn = learn;
		this.all = possible;
	}
	
	
	public List<AttackLearn> getLearn(){
		return learn;
	}
	public List<Attack> getPossible(){
		return all;
	}
	
	
	
	public static class AttackLearn {
		
		private int level;
		private Attack attack;
		
		
		public AttackLearn(int level, Attack attack){
			this.level = level;
			this.attack = attack;
		}
		public int getLevelLearned(){
			return level;
		}
		public Attack getAttack(){
			return attack;
		}
		
	}
	
}
