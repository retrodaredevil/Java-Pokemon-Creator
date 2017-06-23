package me.retrodaredevil.pokecreator.screens;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Main;
import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.map.MapHandler;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;

/**
 * @author retro
 *
 */
public class TitleScreen implements Screen{

	private static final SoundType intro = SoundType.MUSIC_LOGO_INTRO;
	private static final SoundType music = SoundType.MUSIC_TITLE_SCREEN_INTRO;
	
	private boolean start = false;
	
	private Image backgroundGrass = null;
	
	private List<Image> titleFrames = null;
	
	private long starting = 0;
	private Image current = null;
	
	private int titleY = 0;
	
	private Main main;
	
	public TitleScreen(Main clientMain){
		main = clientMain;
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		if(current != null){
			current.draw(0, 0);
		}
		if(titleY != 0){
			
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourcePack pack = main.getResourcePack();
		starting = System.currentTimeMillis();
		
		//String path = "resources/textures/titlescreen/";
		//this.backgroundGrass = new Image(path + "background/background.png");
		
		this.backgroundGrass = new Image(pack.getStream("textures/titlescreen/background/background.png"), "background", false);
		
		titleFrames = new ArrayList<>();
		for(int i = 0; i < 5; i ++){
			//Image im = new Image(path + "maintitle/" + (i + 1) + ".png");
			Image im = new Image(pack.getStream("textures/titlescreen/maintitle/" + (i + 1) + ".png"), "im" + i, false);
			titleFrames.add(im);
		}
		
		
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		long time = System.currentTimeMillis() - starting;
		
		if(time < 9000){
			current = backgroundGrass;
			if(!intro.isPlaying() ){
				intro.play(main.getResourcePack());
				//System.out.println("playing...");
			}
			
			titleY = (int) (time / 3);
			if(titleY >400){
				titleY = 400;
			}
			
			
			
		} else {
			if(!music.isPlaying()){
				music.play(main.getResourcePack());
			}
			
			titleY = 0;
			int compare = (int) (time / 1000);
			
			while(compare >= titleFrames.size()){
				compare -= titleFrames.size();
			}
			this.current = titleFrames.get(compare);
			
		}
		if(container.getInput().isKeyDown(Input.KEY_ENTER)){
			this.start = true;
		}
		
		
		
	}
	
	@Override
	public void stop() {
		intro.stopAll();
		music.stopAll();
	}

	@Override
	public Screen getNext() {
		if(start){
			// do stuff with map handler or init game //TODO
			return new MapHandler(main, new ClientTrainer("Josh",true, main));
		}
		return this;
	}


}
