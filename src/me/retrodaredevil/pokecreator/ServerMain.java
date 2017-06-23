package me.retrodaredevil.pokecreator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.map.MapHandler;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 * An unfinished version of what I thought the server backend would be like
 */
public class ServerMain implements Screen, MainClass{

	
	private Screen current;
	
	public static void main(String [] args){
		new ServerMain().start();
	}

	public ServerMain(){
		
	}
	
	private void start() {
		boolean error = false;
		
		try {
			this.init(null);
		} catch (SlickException e1) {
			e1.printStackTrace();
			System.out.println("Errors while initializing. Shutting down.");
			System.exit(0);
		}
		
		while(true){
			try {
				this.update(null, 0);
				error = false;
			} catch (SlickException e) {
				if(error){
					e.printStackTrace();
					System.out.println("Exiting multiple errors were thrown while running the update method. Shutting down");
					this.stop();
					System.exit(0);
				}
				e.printStackTrace();
				error = true;
			}
		}
	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		current = new MapHandler(this);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Screen getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Manager getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MapHandler getMapHanlder() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
