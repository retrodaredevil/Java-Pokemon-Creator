package me.retrodaredevil.pokecreator.map;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;

import me.retrodaredevil.pokecreator.map.ObjectLayer.Section;
import me.retrodaredevil.pokecreator.map.PokeMap.LayerLayer;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;

/**
 * @author retro
 *
 */
public class Block {
	
	private String location;
	
	private int x;
	private int y;
	
	private Layer layer;
	
	private Direction push;
	private boolean blocked;
	private boolean locked = false;
	private Material material;
	
	private Map<LayerLayer, Image> imageMap = null;
	
	private int spawnRate = 0;
	
	Block(int x, int y, String location, Layer layer, Direction push, boolean blocked, boolean locked, Material m){
		this.location = location;
		this.x = x;
		this.y = y;
		this.layer = layer;
		
		this.push = push;
		
		this.blocked = blocked;
		this.locked = locked;
		this.material = m;
		
		
		
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public String getLocationName(){
		return location;
	}
	public Material getMaterial(){
		return material;
	}
	public Layer getLayer(){
		return layer;
	}
	public boolean canComeFrom(Direction dir, MovementType type){
		
		if(dir.jump()){ // even if this is blocked, we want to make sure that if this is a jump block, they move off of it.
			return true;
		}
		if(this.isBlocked() || this.isLocked()){
			return false;
		}
		if(this.getMaterial() == Material.WATER){
			return type == MovementType.SURFING;
		}
		if(push != null && push != Direction.STILL){
			//System.out.println("here: " + push.toString());
			return dir.directionEquals(push);
		}

		return true; //
	}
	public boolean canLeaveTo(Direction dir){
		return true;
	}
	public Direction pushTo(){
		
		return push;
	}
	
	public boolean canRun(){
		return true;
	}
	public boolean canBike(){
		return true;
	}

	public Block getAdd(Direction moving) {
		PokeMap map = this.layer.getMap();
		
		int x = moving.getX() + this.getX();
		int y = moving.getY() + this.getY();
		
		if(x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight()){
			return null;
		}
		
		return this.layer.getBlock(x, y);
	}
	public Image getImage(LayerLayer layer){
		if(this.imageMap == null){
			return null;
		}
		return imageMap.get(layer);
	}
	public void setImage(LayerLayer layer, Image image){
		if(this.imageMap == null){
			imageMap = new HashMap<>();
		}
		imageMap.put(layer, image);
	}
	@Override
	public String toString() {
		return "Block{x:" + this.getX() + ",y:" + this.getY() + "}";
	}

	public boolean isBlocked() {
		return blocked;
	}
	public boolean isLocked(){
		return locked;
	}
	
	public String getText(){
		
		for(Section s : this.layer.getObjectLayer().getSections()){
			if(s.hasSign() && s.contains(this)){
				return s.getSignText();
			}
		}
		
		return "Default Sign Text";
	}




	
	
	
	
}