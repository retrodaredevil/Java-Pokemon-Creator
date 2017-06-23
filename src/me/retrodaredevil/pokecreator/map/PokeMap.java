package me.retrodaredevil.pokecreator.map;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.trainer.Location;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;


/**
 * @author retro
 *
 */
public class PokeMap implements Screen{
	private TiledMap map;
	
	private List<Layer> layers = new ArrayList<>();
	
	private MapHandler handler;
	
	private LayerLayer renderLayer = LayerLayer.BACKGROUND;
	private double zoom = 1;
	private double toNextBlock = 0;
	
	private Block spawn;
	
	
	public PokeMap (TiledMap map, MapHandler handler){
		this.map = map;
		this.handler = handler;
		
		String[] split = map.getMapProperty("layers", "").split(",");

		outer : for(String s : split){
			for(Layer layer : this.layers){
				if(layer.getName().equals(s)){
					continue outer;
				}
			}
			Layer layer = new Layer(s, this);
			this.layers.add(layer);
		}
		
		if(this.layers.size() == 0){
			System.out.println("Couldn't add any layers to a PokeMap.");
			return;
		}
		
		String mapSpawn = map.getMapProperty("spawn", null);
		if(mapSpawn == null){
	
			spawn = this.layers.get(0).getBlock(0, 0);
			System.out.println("No spawn set in map properties. Using " + spawn.toString());
		} else {
			String[] parts = mapSpawn.split(",");
			try{
				spawn = this.getLayer(parts[0]).getBlock(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
			} catch(NumberFormatException e){
				e.printStackTrace();
				System.out.println("Error parsing a part of: " + mapSpawn + " 0, 0 is now the spawn");
				spawn = this.layers.get(0).getBlock(0, 0);
			}
		}
		

		
	}
	
	public MapHandler getMapHandler(){
		return handler;
	}
	
	public List<Layer> getLayers(){
		return layers;
	}
	
	public Layer getLayer(String name){
		for(Layer layer : this.getLayers()){
			if(layer.getName().equalsIgnoreCase(name)){
				return layer;
			}
		}
		
		return null;
	}
	
	public int getWidth(){
		return map.getWidth();
	}
	public int getHeight(){
		return map.getHeight();
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		ClientTrainer trainer = ClientTrainer.getClient();
		if(trainer == null){
			throw new NullPointerException("Called a render method while there is no client");
		}
		final int ppb = (int) (this.map.getTileHeight() * zoom);
		Location loc = trainer.getLocation();
		
		final double percent = this.toNextBlock;
		
		
		Direction moving = loc.getMoving();
		
		double px = (percent * moving.getX() * ppb) * -1;
		double py = (percent * moving.getY() * ppb) * -1;
		
		Block on = loc.getCurrentBlock();
		
		// (w / -2) = -500
		// (h / -2) = -400
		
		int x = Display.getWidth() / (ppb * 2); // the 2 is so we get half of the screen
		int y = Display.getHeight() / (ppb * 2);
		
		x *= ppb;
		y*= ppb;
		
		int renderX =  (on.getX() * ppb) - x;
		int renderY =  (on.getY() * ppb) - y;
		
		if(moving == Direction.STILL || loc.getCurrentBlock() == loc.getTarget()){ // 8 6
			px = 0;
			py = 0;
		} else {
			//System.out.println("\t\t\t\t\t\t\t\tpx: " + px + " py: " + py + "\trx: " + renderX + " ry: " + renderY);
//			px = 0;
//			py = 0;
		}
		
		
		//map.render((w / -2) + ((int) (loc.getX() * ppb)), (h / -2) + ((int) (loc.getY() * ppb)), loc.getLayer().getLayerIndex(renderLayer));
		//System.out.println(renderLayer.toString()); working
		loc.getLayer().render(c, renderLayer, renderX, renderY , (int) px, (int) py, zoom);
	}
	public void setRenderLayer(LayerLayer layer, double zoom, double toNextBlock){
		this.renderLayer = layer;
		this.zoom = zoom;
		this.toNextBlock = toNextBlock;
	}
	@Override
	public void update(GameContainer container, int delta) throws SlickException { // the only method out of the overrided methods that will be used by servers
		if(container != null){ // now we know this is a client and we should check for input
			
		}
		
	}
	@Override
	public Screen getNext() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	public Block getSpawn() {
		return spawn;
	}
	
	
	public TiledMap getMap(){
		return map;
	}
	public static enum LayerLayer {
		/**
		 * used for things that you walk on and don't interact with. Barriers like walls are the exception to this rule as long as they don't need a background behind them
		 * 
		 * This is rendered first with no exceptions
		 */
		BACKGROUND(1), 
		
		/**
		 * used for things like patches of grass, signs, warps, ladders, barriers (not walls though(anything barrier that needs a background) and some walkways that need a background, etc. 
		 * 			- Any thing you might interact with and that doesn't have a background
		 * 
		 * this is rendered second - after the background probably with no exceptions
		 */
		ENTITYLAYER(2), 
		
		/**
		 * Used for things like rooftops of buildings, the tops of trees - any thing that you would want to go over the player if they stood on it
		 * 
		 * This is rendered last and is rendered after the or all players are. With a few exceptions
		 */
		FRONTLAYER(3);
		
		private int number;
		
		private LayerLayer(int number){
			this.number = number;
		}
		
		public int getNumber(){
			return number;
		}
		
		public String getName(String name){
			return name + ":" + this.getNumber();
		}
		
	}

}
