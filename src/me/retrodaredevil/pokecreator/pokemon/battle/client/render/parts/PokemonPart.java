package me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.animation.PSprite;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * 
 * This class is used to create a part that you can render that will display a pokemon. 
 * It should be stored while a pokemon is in battle but not when a pokemon is not in battle
 *
 */
/**
 * @author retro
 *
 */
public class PokemonPart extends BattlePart{
	
	private BattlePokemon mon;
	

	private final HPBarPart hp;
	
	
	// TODO add a stat that gets updated
	
	
	public PokemonPart (BattleAnimation an, BattlePokemon mon){
		super(an);
		this.mon = mon;
		
		boolean b = false;
		if(animation.getBattleSide() == mon.getBattleSide()){
			b = true;
		}
		hp = new HPBarPart(an, this, b);
		
	}
	
	public BattlePokemon getBattlePokemon(){
		return this.mon;
	}
	

	
	
	

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack pack) throws SlickException {
		
		if(animation.getBattleSide() == mon.getBattleSide()){
			this.renderBack(c, pack);
		} else {
			this.renderFront(c, pack);
		}
		hp.render(c, g, pack);
		
	}

	
	/**
	 * 
	 * this renders the pokemon and it's hp bar as if you were facing it as the enemy and you could see the front of it
	 */
	private void renderFront(GameContainer c, ResourcePack pack) throws SlickException{ // the enemy
		PSprite sprite = this.mon.getPokemon().getPokemonType().getSprite(this.mon.getPokemon().isShiny());

		BattleBackground b = this.animation.getBattleBackground();
		int size = b.getEnemyWidth() / 2;
		Image render = sprite.getFront(pack).getScaledCopy(size, size);
	//double ratio = render.getHeight() / sprite.getBack(pack).getHeight();
		//int height = animation.getTextpopupList().getDialogHeight();
		
		
		int x = b.getEnemyMiddleX() - (render.getWidth() / 2);
		int y = (int) (b.getEnemyY() - (render.getHeight() * (2.0 / 3)));
		
		if(b.getPercent() == 100 && this.isSelected()){
			
			long amount =  System.currentTimeMillis() % 1200;
			if(amount > 800){
				amount = 1200 - amount;
			}
			int a = (int) ((amount / 800.0) * 192 + 64);
			Color filter = new Color(255,255,255, a);
			//System.out.println("a: " + a);
			
			render.draw(x, y, filter);
			//System.out.println("running front with filter");
		} else {
			render.draw(x, y);
			
		}
		
		
	}
	
	/**
	 * 
	 * this renders the pokemon and it's hp bar as if you were on it's team and you could see the back of it
	 */
	private void renderBack(GameContainer c, ResourcePack pack)throws SlickException{
		PSprite sprite = this.mon.getPokemon()
				.getPokemonType()
				.getSprite(this.mon.getPokemon().isShiny());
		

		BattleBackground b = this.animation.getBattleBackground();
		int size = b.getTeamWidth() / 2;
		
		Image orig = sprite.getBack(pack);
		//ImageUtil.setColor(orig, 0, 0, new Color(0, 0, 0)); // works
		
		Image render = orig.getScaledCopy(size, size);
		double ratio = render.getHeight() / sprite.getBack(pack).getHeight();
		int add = 0;
		
		if(b.getPercent() == 100 && this.isSelected() && System.currentTimeMillis() % 800 < 400){
			add = (int) (ratio * 2);
		}
		render.draw(b.getTeamMiddleX() - (render.getWidth() / 2), (float) (b.getTeamY() - (sprite.getBackYEnd() * ratio) + add - (ratio * 6)));
		
	}
	public boolean isSelected(){
		return animation.getSelected() == this.mon.getPokemon();
	}
	
	

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		hp.update(container, delta);

		
	}

	public HPBarPart getHPBarPart() {
		return this.hp;
	}

	@Override
	public boolean isDone() {
		return false;
	}
	
	@Override
	public void stop() {
	}

	@Override
	public Screen getNext() {
		return this;
	}
	
	
	public static enum BattleSide {
		TEAM1, TEAM2;
		
	}
	public static enum SideLocation {
		MIDDLE(false, 0, false),
		LEFT_DOUBLE(true, 1, false), // pokemon on the left is first in the play's team
		RIGHT_DOUBLE(true, -1, false),
		
		/**
		 * use this for a double battle if a pokemon can be on either side. 
		 * Note that this shouldn't be used in all locations and make sure you check the method to see if you can use this
		 */
		UNSPECIFIED(true, 0, true),
		U_LEFT_DOUBLE(true, 1, true),
		U_RIGHT_DOUBLE(true, -1, true)
		
		;
		
		private boolean doubleBattle;
		private int amount;
		private boolean unspec;
		
		private SideLocation(boolean doubleBattle, int amount, boolean unspec){
			this.doubleBattle = doubleBattle;
			this.amount = amount;
			this.unspec = unspec;
		}
		
		/**
		 * 
		 * @return returns true if the side location is unspecified meaning that a certain pokemon will be able to change sides. If false, that means that the pokemon can only be on one side
		 */
		public boolean isUnspecified(){
			return this.unspec;
		}
		
		public boolean isDoubleBattle(){
			return this.doubleBattle;
		}
		
		public int getAmount(){
			return amount;
		}
		
		
		
	}
	

}
