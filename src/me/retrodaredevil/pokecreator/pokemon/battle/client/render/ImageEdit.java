package me.retrodaredevil.pokecreator.pokemon.battle.client.render;

import org.newdawn.slick.Image;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;

/**
 * @author retro
 *
 */
public interface ImageEdit {
	public default int getXDrawOffset(Image image, boolean enemy, BattlePokemon mon){
		return 0;
	}
	public default int getYDrawOffset(Image image, boolean enemy, BattlePokemon mon){
		return 0;
	}
	
	public default float getResizeRatio(Image image, boolean enemy, BattlePokemon mon){
		return 1;
	}
	public default Image editImage(Image image, boolean enemy, BattlePokemon mon){
		return image;
	}
}
