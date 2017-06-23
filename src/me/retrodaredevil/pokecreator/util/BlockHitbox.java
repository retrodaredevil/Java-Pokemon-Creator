package me.retrodaredevil.pokecreator.util;

import me.retrodaredevil.pokecreator.map.Block;

/**
 * @author retro
 *
 */
public class BlockHitbox {

	private Block a, b;
	
	
	public BlockHitbox (Block a, Block b){
		this.a = a;
		this.b = b;
	}

	
	public boolean contains(Block block){
		return a.getX() <= block.getX() && a.getY() <= block.getY() &&
				b.getX() >= block.getX() && b.getY() >= block.getY();
	}
	
	
}
