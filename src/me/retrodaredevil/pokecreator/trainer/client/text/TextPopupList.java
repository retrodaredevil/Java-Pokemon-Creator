package me.retrodaredevil.pokecreator.trainer.client.text;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.util.ScreenList;

/**
 * @author retro
 *
 */
public class TextPopupList extends ScreenList<TextPopup>{

	
	private int height = 0;
	
	
	


	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		TextPopup current = this.getCurrent();
		if(current != null){ // mess with waiting in update
			//System.out.println("not null tho");
			if(current instanceof TextDialog){
				TextDialog text = (TextDialog) current;
				int set = text.getLastHeight();
				if(set > 3){ // idk that's pretty random. Pretty much just needs to be greater than 0
					this.height = set;
				}
				//System.out.println("height: " + height);
			}
		}
		super.render(c, g, currentPack);
	}




	public int getDialogHeight(){
		return height;
	}




}
