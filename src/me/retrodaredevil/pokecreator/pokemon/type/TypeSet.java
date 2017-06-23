package me.retrodaredevil.pokecreator.pokemon.type;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;



/**
 * @author retro
 *
 */
public class TypeSet {

	private Type type1;
	private Type type2;
	
	private List<Type> types;
	
	public TypeSet(Type t1, @Nullable Type t2){
		this.type1 = t1;
		this.type2 = t2;
		if(t2 == null){
			types = Arrays.asList(type1);
		} else {
			types = Arrays.asList(type1, type2);
		}
	}
	public boolean hasTwoTypes(){
		return type2 != null;
	}
	public List<Type> getTypes(){
		return types;
	}
	public Type getType1(){
		return type1;
	}
	public Type getType2(){
		return type2;
	}
	public boolean hasType(Type moveType) {
		return types.contains(moveType);
	}
	
	/**
	 * @deprecated should use the Pokemon class's getMultiplier
	 * @param attackType
	 * @return
	 */
	@Deprecated
	public double getMultiplier(Type attackType){
		
		double r = 1;
		
		for(Type t : this.getTypes()){
			if(attackType.doesNoDamage(t)){
				r *=0;
				break;
			} else if(attackType.isNotEffective(t)){
				r /=2;
			} else if(attackType.isSuperEffective(t)){
				r*=2;
			}
		}
		
		return r;
		
	}
	
}
