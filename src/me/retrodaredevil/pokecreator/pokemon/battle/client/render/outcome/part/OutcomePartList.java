package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.util.ScreenList;

/**
 * @author retro
 *
 */
public class OutcomePartList extends ScreenList<OutcomePart> {

	private MessagePart message = null;
	
	
	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		super.render(c, g, currentPack);

		OutcomePart part = getCurrent();
		if(message != null && message != part){
			message.render(c, g, currentPack);
			//System.out.println("rending a message");
		}
		
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
	
		super.update(container, delta);
		
		OutcomePart part = getCurrent();
		if(part instanceof MessagePart){
			message = (MessagePart) part;
	
		}
		
		if(message != null && message != part){
			message.update(container, delta);
		}
		
		if(this.getAllValues().size() == 0){
			message = null;
		}
		
	}
	
	@Override
	public void finishCurrent() {
		super.finishCurrent();

		OutcomePart part = getCurrent();
		if(this.message != null && this.message != part){
			part.stop();
		}
	}

	
	
	
	
}
