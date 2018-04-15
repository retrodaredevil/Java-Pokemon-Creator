package me.retrodaredevil.pokecreator;

import me.retrodaredevil.pokecreator.map.MapHandler;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.resources.ServerType;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.screens.TitleScreen;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author retro
 *
 */
public class Main extends BasicGame implements Screen, MainClass {

	
	private volatile Screen screen; // is volatile so the second thread running(nested in this class) can use and update this variable (so it isn't cached)
	
//	private static boolean kcode = false;
//	private int wayThere = 0;
//	
//	private long lastKCodePress = 0;
	
	private Manager manager;
	private ResourcePack pack = ResourcePack.BASIC;
	
	private boolean isInitialized = false;
	
	
	public Main() {
		super("Pokemon Creator");
	}
	public static void main(String[] args){
		System.out.println("hereer;jal;ksdjfals;djf");
		System.setProperty("org.lwjgl.librarypath",  new File("resources/dlls").getAbsolutePath());
		try{
			Main main = new Main();
			AppGameContainer app =  new AppGameContainer(main);
			app.setDisplayMode(1000, 800, false);
			
			Display.setResizable(true);
		
			app.setAlwaysRender(true);
			
			app.start();
			app.setSoundOn(true);
			
			
		} catch(SlickException e){
			System.out.println();
			System.out.println("Starting stacktrace print...");
			e.printStackTrace();
			System.out.println("Ended stacktrace print.");
			System.out.println();
		}
	}
	
//	/ *
//	 * @deprecated doesn't work
//	 * @param old
//	 */
//	@Deprecated
//	public void restart(GameContainer old){
//		old.exit();
//		try{
//			AppGameContainer app =  new AppGameContainer(this);
//			app.setDisplayMode(1000, 800, false);
//			
//			Display.setResizable(true);
//		
//			app.start();
//			app.setSoundOn(true);
//			
//		} catch(SlickException e){
//			e.printStackTrace();
//		}
//	}

	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		//long then = System.currentTimeMillis();
		
		this.screen.render(arg0, g, currentPack);
		//System.out.println("Took " + (System.currentTimeMillis() - then) + "ms");
	}
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		this.render(container, g, this.pack);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		if(this.isInitialized){
			return;
		}
		this.isInitialized = true;
		
		ServerType.setType(ServerType.CLIENT_ONLY); // remember this could turn into a SERVER_CLIENT // still have to code that tho
		screen = new TitleScreen(this);
		screen.init(container);
		
		Manager.createNew(this);
		manager = Manager.getInstance();
		
		try {
			manager.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//new Sound(this.getResourcePack().getStream("sounds/music/intro/title_screen.wav"), "logo_intro.wav").play();
		
		new Thread(){
			Scanner scanner = new Scanner(System.in);
			@Override
			public void run() {
				ServerType type = ServerType.getType();
				System.out.println("Seeing if this supports secret command line...");
				while(  !( screen instanceof MapHandler && !type.isConnected() && type.isClient() )  ){
					//\screen.getClass();
					//System.out.println("");
					//System.out.println("screen: " + screen.getClass().getName() + " type.toString; " + type.toString());
				}
				
				MapHandler handler = (MapHandler) screen;
				
				ClientTrainer t = (ClientTrainer) handler.getTrainers().get(0);

				System.out.println("Waiting for input...");
				String input = scanner.nextLine();

				System.out.println("Received Input: \"" + input + "\"");

				if(input.toLowerCase().startsWith("sendtest")){
					System.out.println("Doing something with the input...");
					//t.sendText(Arrays.asList("This is the test message", "I'm not quite done yet.", "How are you doing?", "Alright now I'm done."), Bubble.BUBBLE);
					t.sendAreaBubble("ROUTE TEST");
				}
				System.out.println("end of run");
				this.run();
			}
		}.start();
		//container.setClearEachFrame(true);
	}
//	private int lastH;
//	private int lastW;

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		AppGameContainer agc = (AppGameContainer) container;
		agc.setDisplayMode(Display.getWidth(), Display.getHeight(), Display.isFullscreen());
	
		int fps = agc.getFPS();
		if(fps < 30){
			agc.setShowFPS(true);
		} else {
			agc.setShowFPS(false);
		}
		
//		if((Display.getWidth() != lastW || Display.getHeight() != lastH) && lastW != 0 && lastH != 0){
//			try {
//				Display.setDisplayMode(new DisplayMode(Display.getWidth(), Display.getHeight()));
//				
//				
//			} catch (LWJGLException e) {
//				e.printStackTrace();
//			}
//		}
//		lastH = Display.getHeight();
//		lastW = Display.getWidth();
		try{
			screen.update(container, delta);
		} catch(OutOfMemoryError e){
			e.printStackTrace();
			
			System.err.println("Exitting because of out of memory error. Stack trace above.");
			System.exit(0);
		} catch(SlickException e){
			throw e;
		} 
		Screen s = screen.getNext();
		if(s != screen && s != null){
			screen.stop();
			this.screen = s;
			screen.init(container);
		}
		SoundType.update();
		
//		Input in = container.getInput();
//		if(in.isKeyDown(Input.KEY_LCONTROL) && in.isKeyDown(Input.KEY_R) && in.isKeyDown(Input.KEY_LALT)){
//			System.out.println("restarting window");
//			restart(container);
//			System.out.println("restarted window");
//		}
		
		
		
		
/*	
 *		int[] keys = new int[] {Input.KEY_UP, Input.KEY_UP, Input.KEY_DOWN, Input.KEY_DOWN, Input.KEY_LEFT, Input.KEY_RIGHT, Input.KEY_LEFT, Input.KEY_RIGHT,
 *			Input.KEY_B, Input.KEY_A, Input.KEY_ENTER
 *		};		
 *		if(wayThere >= keys.length){
 *			kcode = !kcode;
 *			wayThere = 0;
 *			//System.out.println("did kcode");
 *			return;
 *		}
 *
 *		if(this.lastKCodePress + 1000 <= System.currentTimeMillis()){
 *			this.wayThere = 0;
 *		} 
 *		
 *		int key = keys[wayThere];
 *		if(in.isKeyPressed(key)){
 *			wayThere++;
 *			this.lastKCodePress = System.currentTimeMillis();
 *			//System.out.println("kcode++ wayThere: " + wayThere);
 *		}
 * 
 */
	}
	
	/**
	 * returns the current screen. This method will almost always have the correct screen but should not be used randomly cuz idk why it would be 
	 */
	@Override
	public Screen getNext() {
		return this.screen;
	}
	
	@Override
	public void stop() {
		screen.stop();
	}
	
	@Override
	public boolean closeRequested() {
		System.out.println("About to shutdown classes...");
		try{
			this.stop();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("An error has occurred while trying to shut down the program.\n"+
			"The program should shut down, but data may not have been saved properly. Please report this to the author and make sure your game data is not corrupt.");
		}
		System.out.println("Done shutting down classes... Program should close.");
		return super.closeRequested();
	}
	@Override
	public Manager getManager() {
		return manager;
	}
	@Override
	public MapHandler getMapHanlder() {
		if(this.screen instanceof MapHandler){
			return (MapHandler) screen;
		}
		return null;
	}
	
	public ResourcePack getResourcePack() {
		return this.pack;
	}


}
