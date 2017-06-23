package me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog;

/**
 * @author retro
 *
 */
public class HPBarPart extends BattlePart{

	private PokemonPart part;
	
	/**
	 * this will be used to tell how far along the hp bar is to the current hp
	 */
	private float percent = 100;

	private final BarType bar; // TODO implement the xp bar. Image implemented
	
	private int hp;
	
	
	public HPBarPart(BattleAnimation an, PokemonPart part, boolean xpBar){
		super(an);
		this.part = part;

		Pokemon mon = part.getBattlePokemon().getPokemon();
		
		hp = mon.getHP();
		percent = (100f * hp) / mon.getMaxHP();
		if(xpBar){
			bar = BarType.TEAM_XP;
		} else {
			bar=  BarType.ENEMY;
		}
		//System.out.println("Bar type: " + bar.toString());
		
	}
	public float getCurrentPercent() {
		return percent;
	}
	public void setCurrentPecent(float percent){
		this.percent = percent;
	}
	
	public void setHP(int hp){
		this.hp = hp;
	}
	public int getHP(){
		return hp;
	}

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {

		Image render = bar.getImage(currentPack, false);
		
		BattleBackground back = animation.getBattleBackground();
		
		int width;
		if(this.bar == BarType.ENEMY){
			width = back.getEnemyWidth();
		} else {
			width = back.getTeamWidth();
		}
		
		
		
		int height  = render.getHeight() * (width / render.getWidth());
		render.setFilter(Image.FILTER_NEAREST);
		render = render.getScaledCopy(width, height);
		
		int x;
		int y;
		
		if(this.bar == BarType.ENEMY){
			x = c.getWidth() - back.getEnemyWidth() - render.getWidth();
			y = back.getEnemyY() - render.getHeight();
 			//System.out.println("rending enemy hp bar. rx: " + x + " ry: " + y + " image h: " + render.getHeight() + " w: " + render.getWidth());
 		} else {
 			x = width;
 			y = c.getHeight() - animation.getDialogHeight() - render.getHeight();
 			//System.out.println("rending team hp bar. rx: " + x + " ry: " + y + " image h: " + render.getHeight() + " w: " + render.getWidth());
 		}
		render.draw(x, y);
		
		double ratio = render.getWidth() / 110;
		
		float size = (float) (ratio * 9);
		
		UnicodeFont font = currentPack.getFont(size);
		
		Pokemon mon = this.part.getBattlePokemon().getPokemon();
		final int renderWidth = render.getWidth();
		final int renderHeight = render.getHeight();
		
		String display = "Lv" + mon.getLevel();
		
		//font.drawString(x + render.getWidth() - font.getWidth(display), y, display);
		
		
		Color color = new Color(64, 64, 64);
		
		

		TextDialog.drawString(font, x + renderWidth - (font.getWidth(display)) - (renderWidth / 7), (int) (y + (renderHeight / 5.7)), display, color);
		
		display = mon.getNickName();
		if(display == null){
			display = mon.getPokemonType().getName();
		}
		
		TextDialog.drawString(font, x + (renderWidth / 7), (int) (y + (renderHeight / 5.7)), display, color);
		
		
		if(this.bar == BarType.TEAM_XP){
			display = hp + "/" + mon.getMaxHP();
			TextDialog.drawString(font, x + renderWidth - (font.getWidth(display)) - (renderWidth / 7), (int) (y + (renderHeight / 1.6)), display, color);

		}
		
		//render = render.getsc
		
		
		
		
	}
	protected Image editImage(Image image){
		
		return image;
	}
	
	

	
	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
	}

	
	
	
	@Override
	public void stop() {
	}

	@Override
	public Screen getNext() {
		return this;
	}

	@Override
	public boolean isDone() {
		return false;
	}
	
	
	
	public static enum BarType {
		ENEMY(0), TEAM_XP(1);
		
		private static ResourcePack current;
		
		private Image image;
		
		private int level;
		
		private BarType (int level){
			this.level = level;
		}
		

		/**
		 * 
		 * @param current the current resource pack
		 * @param force if true, this will update the image and get the original. This is useful if you changed the color of a pixel on an image and want to reset it.
		 * @return returns the bar image
		 */
		public Image getImage(ResourcePack current, boolean force) throws SlickException {
			update(current, force);
			return image;
		}
		
		
		private static void update(ResourcePack current, boolean force) throws SlickException{
			
			if(current == null || BarType.current != current || force){

				Image whole = new Image(current.getStream("textures/battle/health_bars.png"), "health_bars", false);

				
				int height = whole.getHeight() / 2;
				int width = whole.getWidth();
				
				for(BarType type : values()){
					type.image = whole.getSubImage(0, type.level * height, width, height);
				}
			}
			
		}
		
		
		
		
		
	}




	
	
	
}
