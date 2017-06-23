package me.retrodaredevil.pokecreator.pokemon;

import me.retrodaredevil.pokecreator.item.StoneItem;

/**
 * @author retro
 * An unfinished evolution class
 */
public enum Evolution {

	
	;
	
	
	
	
	
	
	
	
	
	
	public static class EvoPart {
		

		private Pokemon mon;
		private int level = 0;
		private StoneItem item;
		private boolean trade = false;
		
		
		private EvoPart(Pokemon mon){
			this.mon = mon;
		}
		private EvoPart(Pokemon mon, int evolve){
			this.level = evolve;
		}
		private EvoPart(Pokemon mon, StoneItem stone){
			this.mon = mon;
			this.item = stone;
		}
		
		private EvoPart(Pokemon mon, boolean trade){
			this.mon = mon;
			this.trade = trade;
		}
		
	}
	
	
}
