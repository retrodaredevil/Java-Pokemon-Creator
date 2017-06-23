package me.retrodaredevil.pokecreator.pokemon.animation;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public class PBattleImages {

	private Image front;
	private Image back;
	
	private ResourcePack pack = null;
	
	private String number;
	
	/**
	 * 
	 * @param path the path to the image. ("textures/pokemon/front/" + path)
	 */
	public PBattleImages(String path){
		this.number = path;
	}
	
	
	public Image getFront(ResourcePack pack){
		this.update(pack);
		return front;
	}
	public Image getBack(ResourcePack pack){
		this.update(pack);
		return back;
	}
	
	private void update(ResourcePack pack){
		if(this.pack != pack){
			this.pack = pack;
		} else {
			return;
		}
		try {
			front = new Image(pack.getStream("textures/pokemon/front/" + number), "front" + number, false);
			back = new Image(pack.getStream("textures/pokemon/back/" + number), "back" + number, false);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
