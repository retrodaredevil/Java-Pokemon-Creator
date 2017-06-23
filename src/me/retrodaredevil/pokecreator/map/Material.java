package me.retrodaredevil.pokecreator.map;

import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.map.ObjectLayer.Section;
import me.retrodaredevil.pokecreator.map.ObjectLayer.Spawn;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.util.Randomizer;

/**
 * @author retro
 *
 */
public enum Material {
	NORMAL("normal", false, false, false),
	GRASS("spawn", false, true, true, true),
	WATER("water", false, false, true, true), 
	CAVE("cave", false, false, true, true),
	BARRIER("barrier", true, false, false), 
	PUSH("push", false, false, false), // not special
	SIGN("sign", false, false, true)
;
	private String name;
	
	private boolean alwaysBlocked;
	private boolean alwaysUnblocked;
	
	private boolean isSpecial;
	
	private boolean spawn = false;
	
	private List<MovementType> moves;
	
	private Material(String name, boolean alwaysBlocked, boolean alwaysUnblocked, boolean isSpecial, MovementType... allowedTransportation){
		this.name = name;
		
		this.alwaysBlocked = alwaysBlocked;
		this.alwaysUnblocked = alwaysUnblocked;
		
		this.isSpecial = isSpecial;
		
		this.moves = Arrays.asList(allowedTransportation);
		
		
	}
	private Material(String name, boolean alwaysBlock, boolean alwaysUnblocked, boolean isSpecial, boolean spawn, MovementType... allowedTransportation){
		this(name, alwaysBlock, alwaysUnblocked, isSpecial, allowedTransportation);
		this.spawn = true;
		
		
	}
	public boolean isSpawn(){
		return this.spawn;
	}
	public Pokemon getSpawn(Block block){
		if(!this.spawn){
			return null;
		}
		
		
		List<Spawn> spawns = null;
		
		for(Section s : block.getLayer().getObjectLayer().getSections()){
			if(s.contains(block)){
				spawns = s.getSpawns();
				break;
			}
		}
		if(spawns == null){
			return null;
		}
		
	
		for(Spawn spawn : spawns){
			double x = 0;
			
			switch(spawn.getRate()){
			case 0: 
				x = 10.0;
				break;
			case 1:
				x= 8.5;
				break;
			case 2:
				x = 6.75;
				break;
			case 3:
				x = 3.33;
				break;
			case 4:
				x = 1.25;
				break;
			default:
				x = 0.001;
				break;
			}
			
			double rate = 0;
			if(this == Material.GRASS){ // grass
				rate = x / 187.5;
			} else{ // else if(this == Material.WATER || this == Material.CAVE) {
				rate = 1. / (187.5 / x);
			}
			if(Randomizer.getTrue(rate)){
				return spawn.createPokemon();
			}
		}
		
		return null;
	}
	
	public boolean isAlwaysBlocked(){
		return alwaysBlocked;
	}
	public boolean isAlwaysUnblocked(){
		return this.alwaysUnblocked;
	}
	public boolean isSpecial(){
		return isSpecial;
	}
	
	public String getName(){
		return name;
	}
	public List<MovementType> getAllowedTransportation(){
		return this.moves;
	}
	
	
	

	public static Material getMaterial(String scustom) {
		for(Material m : values()){
			if(m.toString().equalsIgnoreCase(scustom)){
				return m;
			}
		}
		return null;
	}
	
}
