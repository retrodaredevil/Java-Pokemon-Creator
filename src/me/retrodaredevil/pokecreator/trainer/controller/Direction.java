package me.retrodaredevil.pokecreator.trainer.controller;

import java.util.Arrays;
import java.util.List;

/**
 * @author retro
 *
 */
public enum Direction { // up and down are reversed because the map is weird
	UP(0, -1),
	DOWN(0, 1),
	RIGHT(1, 0),
	LEFT(-1, 0),
	
	STILL(0, 0),
	
	JUMP_UP(0, -1, false, true),
	JUMP_DOWN(0, 1, false, true),
	JUMP_RIGHT(1, 0, false, true),
	JUMP_LEFT(-1, 0, false, true),
	
	SPIN_UP(0, -1, true, true),
	SPIN_DOWN(0, 1, true, true),
	SPIN_RIGHT(1, 0, true, true),
	SPIN_LEFT(-1, 0, true, true)
	
	;
	public static final List<Direction> MAIN_DIRECTIONS = Arrays.asList(UP, DOWN, RIGHT, LEFT);
	
	private int xDir;
	private int yDir;
	
	private boolean spin;
	private boolean jump;
	
	Direction (int x, int y){
		xDir = x;
		yDir = y;
	}
	Direction(int x, int y, boolean spin, boolean jump){
		this(x, y);
		this.spin = spin;
		this.jump = jump;
		
	}
	public int getX(){
		return xDir;
	}
	public int getY(){
		return yDir;
	}
	public boolean spin(){
		return spin;
	}
	public boolean jump(){
		return jump;
	}
	
	/**
	 * this will return STILL if the given name was not identified to be any of the values from values()
	 * @param name the name of the direction
	 * @return the direction from the given name
	 */
	public static Direction getDirection(String name){
		name = name.replaceAll("_", "");
		name = name.replaceAll("-", "");
		name = name.replaceAll(" ", "");
		name = name.toLowerCase();
		for(Direction d : values()){
			String dname = d.toString();
			dname = dname.replaceAll("_", "");
			dname = dname.replaceAll("-", "");
			dname = dname.replaceAll(" ", "");
			dname = dname.toLowerCase();
			
			if(dname.equals(name)){
				return d;
			}
		}
		return Direction.STILL;
	}
	
	/**
	 * 
	 * @param d the direction that you want to get
	 * @return the jump direction or by default returns still
	 */
	public Direction getJump(){
		for(Direction a : values()){
			if(!a.spin && a.jump && a.getX() == this.getX() && a.getY() == this.getY()){
				return a;
			}
		}
		return Direction.STILL;
	}
	
	public String getDataString(){
		return "Direction{" + this.toString() + ",x:" + this.getX() + ",y:" + this.getY() + "}";
	}
	public boolean directionEquals(Direction push) {
		return this.getX() == push.getX() && this.getY() == push.getY();
	}
	public Direction getNormal() {
		if(this == STILL){
			return this;
		}
		for(Direction normal : MAIN_DIRECTIONS){
			if(this.directionEquals(normal)){
				return normal;
			}
		}
		System.err.println("Could not find a direction for this: " + this.getDataString() + " this should never happen. Returning STILL");
		return STILL;
	}
	
}
