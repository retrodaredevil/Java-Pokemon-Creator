package me.retrodaredevil.pokecreator.pokemon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.retrodaredevil.pokecreator.pokemon.Stat.OutOf;
import me.retrodaredevil.pokecreator.pokemon.animation.PSprite;
import me.retrodaredevil.pokecreator.pokemon.type.Type;
import me.retrodaredevil.pokecreator.pokemon.type.TypeSet;
import me.retrodaredevil.pokecreator.util.file.JSONFile;


/**
 * @author retro
 *
 */
public class PType {

	public static final int maxNumber = 151;
	
	private static List<PType> allTypes = new ArrayList<>();
	
	private int number;
	private String name;
	private Stat base;
	
	
	/**
	 * this is out of 255
	 */
	private int catchRate;
	
	private TypeSet types;
	private Evolution evo;
	
	private PSprite sprite = null;
	
	
	/**
	 * 
	 * anything with a rate is out of 100 unless said otherwise
	 * 
	 */
	private PType(int number, String name, Stat baseStat, int catchRate, TypeSet types, Evolution evolution){
		allTypes.add(this);
		
		this.number = number;
		this.name = name;
		this.base = baseStat;
		this.catchRate = catchRate;
		this.types = types;
		this.evo = evolution;
		
		
	}
	public TypeSet getTypeSet(){
		return types;
	}
	
	public int getNumber(){
		return number;
	}
	public String getName(){
		return name;
	}
	public Stat getBaseStat(){
		return base;
	}
	public Evolution getEvolution(){
		return evo;
	}
	
	@Override
	public String toString() {
		return "PType{number:" + this.getNumber() + ",name:" + this.getName() + ",types:" + this.getTypeSet().getTypes().toString() + ",basestat:" + this.getBaseStat().toString() + "}";
	}
	
	
	/**
	 * 
	 * @return returns the base capture rate for this pokemon (A number between 1 and 255)
	 */
	public int getCatchRate(){
		return catchRate;
	}
	
	public static enum GrowthRate {
		ERRATIC{
			@Override
			public int getXPNeeded(int n) {
				if(n <= 50){
					return (int) ((Math.pow(n, 3) *(100 - n))/50);
				} else if(n >= 50 && n <= 68){
					return (int) ((Math.pow(n, 3) *(150 - n))/100);
				} else if(n >= 68 && n <= 98){
					return (int) ((Math.pow(n, 3) *((1911 - (10 * n)) / 3))/500);
				} //else if(n >= 98 && n <= 100){
				
				return (int) ((Math.pow(n, 3) *(160 - n))/100);
				
				
			}
		}, FAST{
			@Override
			public int getXPNeeded(int n) {
				
				return (int) ((4 * Math.pow(n, 3)) / 5);
			}
			
		}, MEDIUM_FAST{
			@Override
			public int getXPNeeded(int n) {
				return (int) Math.pow(n, 3);
			}
			
		}, MEDIUM_SLOW{
			@Override
			public int getXPNeeded(int n) {
				return (int) (((6 / 5) * Math.pow(n, 3)) - (15 * Math.pow(n, 2)) + (100 * n) - 140);
			}
			
		}, SLOW{
			@Override
			public int getXPNeeded(int n) {
				return (int) ((5 * Math.pow(n, 3)) / 4);
			}
			
		}, FLUCTUATING{
			@Override
			public int getXPNeeded(int n) {
				
				if(n <= 15){
					return (int) (Math.pow(n, 3) * ((((n + 1) / 3) + 24)/ 50));
				} else if(n >= 15 && n <= 36){
					return (int) (Math.pow(n, 3) * (((n + 14) + 14)/ 50));
				} // else if(n >= 36 && n <= 100){
				return (int) (Math.pow(n, 3) * (((n / 2) + 32)/ 50));
			}
			
		};
		
		public int getXPNeeded(int level){
			return 1;
		}
		
	}
	public PSprite getSprite(boolean shiny){
		if(sprite == null){
			sprite = new PSprite(this, false);
		}
		return this.sprite;
	}

	public static PType getType(int i) {

		for(PType t : values()){
			if(t.getNumber() == i){
				return t;
			}
		}
		if(i > 0 && i <=maxNumber){
			System.out.println("Should have gotten a type from the number: " + i + ".");
		}
		
		return null;
	}
	public static PType getType(String s) {
		s = s.replaceAll(" ", "");
		s = s.replaceAll("_", "");
		for(PType type : values()){
			String name = type.getName();
			name = name.replaceAll(" ", "");
			name = name.replaceAll("_", "");
			if(name.equalsIgnoreCase(s)){
				return type;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param stats the json file that contains all the stats for pokemon
	 * @param useOdd true if you want to add "odd" pokemon like mega or alolan types (This isn't implemented yet so, it's recommended as false)
	 */
	@SuppressWarnings("unchecked")
	public static void init(File stats, boolean useOdd){
		JSONFile jsonFile = new JSONFile(stats);
		
		for(Object ob : jsonFile.getObject().keySet()){
			String key = (String) ob;
			Map<String, Object> map = (Map<String, Object>) jsonFile.getObject().get(key);
			String odd = (String) map.get("odd");
			if(odd != null && !useOdd){
				continue;
			}
			long number = (long) map.get("number");
			if(number > maxNumber){
				continue;
			}
			
			Stat stat = new Stat((long)map.get("hp"),(long) map.get("attack"), (long)map.get("defense"),(long) map.get("spattack"), (long)map.get("spdefense"), (long) map.get("speed"), OutOf.BASE);
			
			String name = (String) map.get("name");
			
			ArrayList<String> stringTypes = (ArrayList<String>) map.get("types");
			ArrayList<Type> types = new ArrayList<>();
			
			for(String type : stringTypes){
				types.add(Type.fromString(type));
			}
			new PType((int)number, name, stat, 45, new TypeSet(types.get(0), (types.size() > 1 ? types.get(1) : null)), null);
			// automatically gets added to allTypes
			
			
			
			
		}
		
		
	}
	
	public static List<PType> values(){
		return allTypes;
	}
	public String getUniqueName() {
		return this.getName();
	}

}
