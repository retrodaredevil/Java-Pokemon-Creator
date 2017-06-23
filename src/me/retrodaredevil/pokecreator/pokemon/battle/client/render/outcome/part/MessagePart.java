package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog;

/**
 * @author retro
 *
 */
public class MessagePart extends OutcomePart{

	private TextDialog d;
	private List<String> text;
	private boolean init = false;


	
	public MessagePart(Battle battle, List<String> s) {
		super(battle);
		this.text = s;
		
		//System.out.println("Created MessageOutcome.");
		
		
	}
	public List<String> getLines(){
		return text;
	}
	@Override
	public String toString() {
		return "MessageOutcome{text:" + text.toString() + ",initialized:" + init + "}";
	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		//System.out.println("rendering MessageOutcome");
		if(d != null){
			d.render(arg0, g, currentPack);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		//System.out.println("Updating MessageOutcome");
		if(d != null){
			//System.out.println("Current: '" + d.getCurrent() + "'");
			d.update(container, delta);
		}
	}
	

	@Override
	public void init(GameContainer container) throws SlickException {
		//System.out.println("inited in MessageOutcome!");
		init = true;
		if(text.size() == 0){
			return;
		}
		d = new TextDialog(text);
		d.setUseButtons(false);
		//battle.getBattleAnimation().getTextpopupList().add(d);
	}
	@Override
	public Screen getNext() {
		//System.out.println("Calling get next for MessageOutcome");
		if(init && d.getNext() != d){
			//System.out.println("returing null for getNext() in MessageOutcome");
			return null; // is called
		}
		
		
		return this;
	}

}
