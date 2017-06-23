package me.retrodaredevil.pokecreator.pokemon.animation;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.pokemon.PType;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public class PSprite {

	
	private ResourcePack current;
	
	private Image front;
	private Image back;
	
	private int yEnd = 0;
	
	
	private PType type;
	private boolean shiny;
	
	public PSprite(PType type, boolean shiny){
		this.type = type;
		this.shiny = shiny;
	}
	
	
	private void update(ResourcePack pack) throws SlickException{
		if(current == null || !current.equals(pack)){
			current = pack;
			
			String add = "";
			if(shiny){
				add = "shiny/";
			}
			front = new Image(pack.getStream("textures/pokemon/" + add + "front/" + type.getNumber() + ".png"), "front shiny: " + shiny + " " + type.getUniqueName(), false);
			front.setFilter(Image.FILTER_NEAREST);
			
			back = new Image(pack.getStream("textures/pokemon/" + add + "back/" + type.getNumber() + ".png"), "back shiny: " + shiny + " " + type.getUniqueName(), false);
			back.setFilter(Image.FILTER_NEAREST);
			yEnd = back.getHeight();
			
			final int middle = back.getWidth() / 2;
			out : for(int i = back.getHeight() / 2; i < back.getHeight(); i++){
				Color color = back.getColor(middle, i);
				if(color.getAlpha() == 0){
					for(int j = 0; j < back.getWidth(); j++){
						Color across = back.getColor(j, i);
						if(across.getAlpha() != 0){
							continue out;
						}
					}
					yEnd = i;
					System.out.println("yEnd is: " + yEnd + " for: " + type.getUniqueName());
					break;
				}
			}
			
		}
	}
	
	
	public Image getFront(ResourcePack pack) throws SlickException{
		this.update(pack);
		return front;
	}
	public int getBackYEnd(){
		return yEnd;
	}

	public Image getBack(ResourcePack pack) throws SlickException{
		this.update(pack);
		return back;
	}
	
	
}
