package me.retrodaredevil.pokecreator.pokemon.battle.client.render;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.util.Logger;

/**
 * @author retro
 *
 */
public class BattleIntro implements Screen{

	private boolean init = false;
	
	protected volatile boolean done = false;
	
	protected volatile ClientTrainer trainer;
	protected Battle battle;
	
	private Thread t = null;
	private volatile boolean running =false;
	
	protected volatile long start = 0;
	
	
	public BattleIntro(ClientTrainer trainer, Battle battle){
		this.trainer = trainer;
		this.battle = battle;
		
	}
	
	
	
	
	
	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(final GameContainer container) throws SlickException {
		init = true;
		running = true;
		final ResourcePack currentPack = trainer.getMain().getResourcePack();
		t = new Thread(){
			BattleIntro current = BattleIntro.this;
			long last = 0;
			@Override
			public void run() {
				
				do {
					long delta = 0;
					if(last != 0){
						delta = System.currentTimeMillis() - last;
					}
					
					try {
						current.update(container, (int)delta);
						current.render(container, container.getGraphics(), currentPack);
					} catch (SlickException e) {
						e.printStackTrace();
						this.interrupt();
						current.done = true;
					}
					last = System.currentTimeMillis();
					
				} while(running && !done);
			}
		};
		t.start();
		start = System.currentTimeMillis();
		battle.init(container);
		running = false;
	}
	public boolean isInitialized(){
		return init;
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		done = true;
	}

	@Override
	public void stop() {//
		done = true;
	}

	
	@Override
	public final BattleIntro getNext() {
		if(done && !t.isAlive()){
			return null;
		}
		return this;
	}

}
