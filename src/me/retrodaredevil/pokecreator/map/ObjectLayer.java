package me.retrodaredevil.pokecreator.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.tiled.TiledMap;

import me.retrodaredevil.pokecreator.pokemon.PType;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.Stat;
import me.retrodaredevil.pokecreator.pokemon.Stat.OutOf;
import me.retrodaredevil.pokecreator.pokemon.attack.AttackList;
import me.retrodaredevil.pokecreator.resources.SoundType;
//import me.retrodaredevil.pokecreator.pokemon.PType;
import me.retrodaredevil.pokecreator.util.BlockHitbox;
import me.retrodaredevil.pokecreator.util.Parser;
import me.retrodaredevil.pokecreator.util.Randomizer;

/**
 * @author retro
 *
 */
public class ObjectLayer {

	private Layer layer;
	private TiledMap map;
	
	private List<Section> sections = new ArrayList<>();
	
	public ObjectLayer (Layer layer, TiledMap map){
		this.layer = layer;
		this.map = map;
		
		for(int i = 0; i < map.getObjectGroupCount(); i++){
			
			
			
			for(int j = 0; j < map.getObjectCount(i); j++){
				final int groupid = i;
				final int objectid = j;
				
				
				String forLayer = map.getObjectProperty(groupid, objectid, "layer", null);
				
				if(forLayer == null || forLayer.equalsIgnoreCase(layer.getName())){
					Section s = new Section(this, groupid, objectid);
					sections.add(s);
				}
			}
		}
		
	}


	public List<Section> getSections() {
		return this.sections;
	}
	
	
//	public String getObjectLayerName(){
//		return layer.getName() + ":o";
//	}
	

	
	public static class Section {
		
		
		private final ObjectLayer layer;
		private final BlockHitbox box;
		
		private String sign = null;
		private String route = null;
		
		private SoundType music;
		
		private List<Spawn> spawns = new ArrayList<>();
		
		//private List<Spawn> spawns= null;
		
		private Section(ObjectLayer layer, int groupid, int objectid){
			this.layer = layer;
			
			TiledMap map = layer.map;
			
			final int ppb = map.getTileHeight();
			
			final int x = map.getObjectX(groupid, objectid) / ppb;
			final int y = map.getObjectY(groupid, objectid) / ppb;
			
			final int h = (int) Math.ceil(map.getObjectHeight(groupid, objectid) / ppb);
			final int w = (int) Math.ceil(map.getObjectWidth(groupid, objectid) / ppb);
			
			box = new BlockHitbox(layer.layer.getBlock(x, y), layer.layer.getBlock(x + w, y + h));
			
			System.out.println("x: " + x + " y: " + y + " h: " + h + " w: " + w);
			
			sign = map.getObjectProperty(groupid, objectid, "text", null);
			route = map.getObjectProperty(groupid, objectid, "route", null);
			
			
			
			music = SoundType.getSoundTypeFromShortName(map.getObjectProperty(groupid, objectid, "music", null));
			
			String spawns = map.getObjectProperty(groupid, objectid, "spawns", null);
			if(spawns != null){
				String[] split = spawns.split(",");
				
				for(String s : split){
					String[] sp = s.split(":");
					PType t = null;
					int level = 1;
					int rate = 100;
					
					if(sp.length > 0){ // first one is the type
						Parser p = new Parser(sp[0]);
						if(p.isNumber()){
							t = PType.getType(p.getInt());
						} else {
							t = PType.getType(sp[0]);
						}
					} 
					if(sp.length > 1){ // second one is the level
						Parser p = new Parser(sp[1]);
						if(p.isNumber()){
							level = p.getInt();
						}
					}
					if(sp.length > 2){ // third one is the rate
						Parser p = new Parser(sp[2]);
						if(p.isNumber()){
							rate = p.getInt();
						}
						
					}
					if(t == null){
						System.out.println("A spawn was null. Setting to a pidgey.");
						t = PType.getType(16);
					}
					Spawn spawn = new Spawn(t, level, rate);
					this.spawns.add(spawn);
					System.out.println(spawn.toString());
				}
				
			}
			
			System.out.println("Created " + this.toString());
		} 
		public boolean hasSign(){
			return sign != null;
		}
		//		public boolean hasSpawns(){
		//			return spawns != null;
		//		}

		public boolean contains(Block b){
			return b.getLayer() == this.getLayer().layer && this.box.contains(b);
		}
		
		public ObjectLayer getLayer(){
			return layer;
		}
		public String getSignText() {
			return this.sign;
		}
		public String getRoute() {
			return this.route;
		}
		public SoundType getMusic(){
			return music;
		}
		
		@Override
		public String toString() {
			return "Section:{layer:" +this.layer.layer.getName() + ",route:" + this.route + ",music:" + this.music + ",sign:" + this.sign +"}";
		}
		
		public List<Spawn> getSpawns(){
			return this.spawns;
		}
		

	}


		

		
		
	
	public static class Spawn {
		
		private PType type;
		private int level;
		private int rate;
		
		
		private Spawn(PType type, int level, int rate){
			this.type = type;
			this.level = level;
			this.rate = rate;
			if(rate == 0){
				rate = 1;
			}
		}
//		/**
//		 * 
//		 * @return the pokemon to spawn or null if the rate wasn't successful (calculates chance to spawn and if it can, it will spawn)
//		 */
//		public Pokemon spawn(){
//			if(Randomizer.getTrue(rate)){
//				return new Pokemon(null, type, Stat.random(OutOf.IV), null, -1, -1, level, Arrays.asList(AttackList.TACKLE, AttackList.CUT, AttackList.PSYCHIC));
//			}
//			
//			return null;
//		}
		public int getRate(){
			return rate;
		}
		public Pokemon createPokemon(){
			return new Pokemon(null, type, Stat.random(OutOf.IV), null, -1, -1, level, Arrays.asList(AttackList.TACKLE));
		}
		@Override
		public String toString() {
			return "Spawn{type:" + type.getName() + ",level:" + level + ",rate:" + rate + "}";
		}
	}
	
}
