package me.retrodaredevil.pokecreator.map;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import me.retrodaredevil.pokecreator.map.PokeMap.LayerLayer;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;
import me.retrodaredevil.pokecreator.util.Logger;

/**
 * @author retro
 *
 */
public class Layer {
		
		private String name;
		private PokeMap map;
		
		private TiledMap tmap;

		private Block[][] grid;
		
		private ObjectLayer objectLayer;
		
		private SoundType music = SoundType.MUSIC_ROUTE_1;
		
		public Layer (String name, PokeMap map){
			this.name = name;
			this.map = map;
			
			
			this.tmap = map.getMap();
			
			
			
			grid = new Block[tmap.getWidth()][tmap.getHeight()];
			for(int i = 0; i < tmap.getWidth(); i++){
				
				for(int j = 0; j < tmap.getHeight(); j++){
					final int x = i;
					final int y = j;
					String location = "";

					int id = this.getTileId(x, y, LayerLayer.BACKGROUND);
					int id2 = this.getTileId(x, y, LayerLayer.ENTITYLAYER);
					
					String spush = tmap.getTileProperty(id, "push", "still");
					if(spush.equals("still")){
						spush = tmap.getTileProperty(id2, "push", "still");
					}
					
					String sblocked = tmap.getTileProperty(id, "blocked", "false");
					if(sblocked.equals("false")){
						sblocked = tmap.getTileProperty(id2, "blocked", "false");
					}
					
					String scustom = tmap.getTileProperty(id, "custom", "normal"); // water, sign // cut // grass(spawn)
					if(scustom.equals("normal")){
						scustom = tmap.getTileProperty(id2, "custom", "normal");
					}
					
					String slocked = tmap.getTileProperty(id, "blocked", "false");
					if(slocked.equals("false")){
						slocked = tmap.getTileProperty(id2, "blocked", "false");
					}
					
					String ssign = tmap.getTileProperty(id, "sign", "false");
					if(ssign.equals("false")){
						ssign = tmap.getTileProperty(id2, "sign", "false");
					}
					
					String sspawn = tmap.getTileProperty(id, "spawn", "false");
					if(sspawn.equals("false")){
						sspawn = tmap.getTileProperty(id2, "spawn", "false");
					}
					
					Direction push = Direction.getDirection(spush).getJump();
					boolean blocked = sblocked.equals("true");
					Material material = Material.getMaterial(scustom);
					boolean locked = slocked.equals("true");
					
					if(ssign.equals("true")){
						material = Material.SIGN;
					}
					if(sspawn.equals("true")){
						material = Material.GRASS;
					}
				
					
					if(blocked && material == Material.NORMAL){
						material = Material.BARRIER;
					} else if(push != Direction.STILL && material == Material.NORMAL){
						material = Material.PUSH;
					} 
					if(material == Material.WATER && blocked){
						material = Material.BARRIER;
					}
					
					Block b = new Block(i, j, location, this, push, blocked, locked, material);
					
					grid[x][y] = b;
//					if(blocked){ // works
//						System.out.println("blocked: " + b.toString());
//					}
				}
				
			}
			
			objectLayer = new ObjectLayer(this, this.tmap);
			
			
		}
		public String getName(){
			return name;
		}
		public SoundType getMusic(){
			return music;
		}
		public void setMusic(SoundType music){
			this.music = music;
		}
		
		public Block getBlock(int x, int y){
			return this.grid[x][y];
		}

		
		
		public int getTileId(int x, int y, LayerLayer layer){
			return tmap.getTileId(x, y, this.getLayerIndex(layer));
		}
		public int getLayerIndex(LayerLayer layer){
			String layerName = layer.getName(name);
			//System.out.println(layerName);
			return tmap.getLayerIndex(layerName);
		}
		public boolean hasFront(){
			return tmap.getLayerCount() > 1;
		}
		
//		public void render(int x, int y, LayerLayer layer){
//			tmap.render(x, y, this.getLayerIndex(layer));
//			
//		}
		public PokeMap getMap() {
			return map;
		}
		
		public void render(GameContainer c, LayerLayer layer, final int x, final int y, final int xoff, final int yoff, double zoom){
			final int ppb = (int) (tmap.getTileHeight() * zoom);
			Logger.log("ppb", "ppb: " + ppb);
			//Logger.log("print loc", "print loc x: " + x + " y: " + y);
			final int xx = x / ppb;
			final int yy = y / ppb;
			
			//System.out.println("xx: " + xx + " yy: " + yy);
			//System.out.println("here");
			
			int index = this.getLayerIndex(layer);
			
			final int w = map.getWidth();
			final int h = map.getHeight();
			
			for(int i = -1; i <= (Display.getWidth() / ppb) + 1; i++){
				for(int j = -1; j <= (Display.getHeight() / ppb) + 1; j++){
					
					int tx = i;
					int ty = j;

					int gx = tx + xx;
					int gy = ty + yy;

					if(gx < 0 || gy < 0 || gx >= w || gy >= h){
						continue;
					}
					//Logger.log("first tile", "x: " + tx + "\ty:"+ ty);

					Block b = this.getBlock(gx, gy);
//					if(tx == 0 && ty == 0){
//						System.out.println("render 00: " + b.toString() + " xx: " + xx + " yy: " + yy);
//					}

//					String p = "tx: " + tx + "\tty:" + ty;
//					Logger.log(p, p);

					Image image = b.getImage(layer);
					if(image == null){
						image = tmap.getTileImage(gx, gy, index);
						if(image != null){
							image.setFilter(Image.FILTER_NEAREST);
						}
						b.setImage(layer, image);
					}
					if(image == null){
						continue;
					}
					image = image.getScaledCopy(ppb, ppb);
					int renderX = (i * ppb) + xoff; // xoff would be 0 if smooth isn't on
					int renderY = (j * ppb) + yoff;
					
					image.draw(renderX, renderY);

//					if(i == 0 && j == 0){
//						System.out.println("rx: " + renderX + " ry: " + renderY);
//					}
				}
			}
		}
		public ObjectLayer getObjectLayer() {
			return this.objectLayer;
		}
		
		
	}