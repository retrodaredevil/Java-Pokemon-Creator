package me.retrodaredevil.pokecreator.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import me.retrodaredevil.pokecreator.MainClass;
import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.PType;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.trainer.Trainer;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;
import me.retrodaredevil.pokecreator.util.file.JSONFile;

/**
 * @author retro
 * The map handler which handles a single map, the trainers in the map, and the battles
 * 
 */
/**
 * @author retro
 *
 */
public class MapHandler implements Screen{ // should be used for server and client. // the big class for managing and updating maps and players

	private PokeMap map = null;
	private List<Trainer> trainers = new ArrayList<>();
	private List<Battle> battles = new ArrayList<>();
	
//	private List<File> mapFiles = new ArrayList<>();
//	private JFileChooser chooser;
	
	private MainClass c;
	
	private JSONFile jFile;
	
	
	public MapHandler (MainClass c, Trainer... trainers){
		
		
		this.c = c;
		for(Trainer t : trainers){
			this.trainers.add(t);
		}

		
		
		
	}
	public PokeMap getMap(){
		return map;
	}
	
	
	public List<Trainer> getTrainers(){
		return trainers;
	}

	
	/** 
	 * Calls the {@link ClientTrainer#render(GameContainer, Graphics, ResourcePack)} method
	 * 
	 */ 
	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		ClientTrainer c = ClientTrainer.getClient();
		if(c == null){
			throw new NullPointerException("Called a render method while there is no client");
		}
		c.render(arg0, g, currentPack);
		//PType.getType(1).getSprite(false).getFront(currentPack);
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		File stats = new File("programdata/testmap/stats.json");
		System.out.println("abs: " + stats.getAbsolutePath());
		PType.init(stats, false);
		
		try {
			map = new PokeMap(new TiledMap("resources/default_maps/test.tmx"), this);
		} catch (SlickException e) {
			e.printStackTrace();
			return;
		}
		
		
		for(Trainer t : trainers){
			
			Block spawn = map.getSpawn();
			
			t.init(container); // this is where pokemon should be added
			t.spawn(spawn.getLayer(), spawn, Direction.DOWN);
			System.out.println("Spawning: " + t.getName());
		}
		for(PType type : PType.values()){
			System.out.println(type.toString());
		}
//		for(int i = 0; i < 151; i++){
//			PType type = PType.getType(i + 1);
//			System.out.println(type.toString());
//		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		for(Trainer t : trainers){
			t.update(container, delta);
			if(t.isInBattle()){
				Battle b = t.getBattle();
				
				if(!this.battles.contains(b)){
					this.battles.add(b);
				}
				
			}
		}
		Iterator<Battle> it = this.battles.iterator();
		while(it.hasNext()){
			Battle b = it.next();
			b.update(container, delta);
			if(b.isOver()){
				it.remove();
			}
		}
		
		
//		File mapFile = chooser.getSelectedFile();
//		if(!mapFiles.contains(mapFile)){
//			mapFiles.add(mapFile);
//		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Screen getNext() {
		return this;
	}
	
	
	
	
	
}
