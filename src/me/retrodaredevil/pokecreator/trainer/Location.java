package me.retrodaredevil.pokecreator.trainer;

import me.retrodaredevil.pokecreator.map.Block;
import me.retrodaredevil.pokecreator.map.Layer;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;

/**
 * @author retro
 *
 */
public class Location {
	
	private Block on, target;
	
	private long timeMoved = 0;
	private long timeLooked = 0;
	//private long timeSetMove = 0;
	
	private boolean running = false;
	
	private Direction looking = Direction.DOWN; // can only be left, right, up or down

	
	private Direction moving = Direction.STILL;
	private MovementType movement = MovementType.WALKING;
	
	
	
	
	
	private Layer layer;
	
//	private double percent;
	
	/**
	 * 
	 * @param on the block the trainer is on or moving from
	 * @param target the block the trainer is moving to
	 * @param percent the percent of the way there - 1.0 is 100% 
	 */
	public Location(Block on,double percent, Layer layer, Direction d){
		this.on = on;
		this.target = on;
		
		this.running = false;
		this.looking = d;
		
		this.layer = layer;
		
		
	}
	public void setMovementType(MovementType type){
		this.movement = type;
	}
	public MovementType getMovementType(){
		return this.movement;
	}
	
	public long getTimeMoved(){
		return this.timeMoved;
	}
	public void setRunning(boolean b){
		this.running = b;
		
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	public Layer getLayer(){
		return layer;
	}
	
	

	
//	public void setTargetBlock(Block b){
//		if(this.target != b){
//			this.target = b;
//			this.timeMoved = System.currentTimeMillis();
//			this.wasRunning = this.isRunning();
//		}
//		
//	}
	public Block getCurrentBlock(){
		return this.on;
	}

	public Direction getLooking() {
		return looking;
	}
	public Direction getMoving(){
		return moving;
	}
	public void setMoving(Direction d){
		this.moving = d;
	}
	
	
	public Block getTarget() {
		return target;
	}

	public long getTimeLooked() {
		return timeLooked;
	}
	public void setTimeLookedNow(){
		this.timeLooked = System.currentTimeMillis();
	}

	public void setTimeMovedNow() {
		this.timeMoved = System.currentTimeMillis();
	}

	public void setLooking(Direction d) {
		this.looking = d.getNormal();
	}

	public void setCurrentBlock(Block b) {
		this.on = b;
	}

	public void setTargetBlock(Block b) {
		this.target = b;
	}
	
	
	
	
}