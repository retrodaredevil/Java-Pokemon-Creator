package me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BackgroundBubble;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;

/**
 * @author retro
 *
 */
public class BattleBackground extends BattlePart{

	
	/**
	 * A number from 0 to 100 for how far the pokemon stand is to it's resting position (100 if it's fully there)
	 */
	protected double percent = 0;
	
	
	private Image top;
	private Image stripe;
	
	private Image team;
	private Image enemy;
	
	private Image black;
	
	private Image opening = null;
	
	
	private int teamMiddleX = 0;
	private int enemyMiddleX = 0;
	
	private int enemyY = 0;
	
	private int teamWidth = 0;
	private int enemyWidth = 0;
	
	private float teamY;
	
	public BattleBackground(BattleAnimation an) {
		super(an);
		
		
		
	}
	public void setPercent(double p){
		this.percent = p;
	}
	public double getPercent(){
		return percent;
	}

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		float ratio = c.getHeight() / 112;
		if(ratio > 1){
			ratio /= 2;
		}
		
		int topHeight = this.drawRight(top.getScaledCopy(ratio), c, 0, 0, false);
		//System.out.println(height);
		//stripe.getScaledCopy(ratio).draw(0, height);
		this.drawRight(stripe.getScaledCopy(ratio), c,0,  topHeight, true);
		
		int textHeight = animation.getDialogHeight();
		
		
		Image team = this.team.getScaledCopy(ratio);
		
		//team.draw((float) (((100 - percent) / 100) * team.getWidth() * -1), c.getHeight() - textHeight - team.getHeight() + ratio + 1);
		float teamX = (float) (((100 - percent) / 100) * c.getWidth()) ;
		teamY = c.getHeight() - textHeight + ratio + 1;
		team.draw(teamX, teamY  - team.getHeight());
		
		this.teamMiddleX = (int) (teamX + (team.getWidth() / 2));
		this.teamWidth = team.getWidth();
		
		Image enemy = this.enemy.getScaledCopy(ratio);
		
		//enemy.draw((float) (c.getWidth() - enemy.getWidth() + (((100 - percent) / 100) * enemy.getWidth() )), c.getHeight() - (2 * textHeight) - enemy.getHeight());
		
		this.enemyY = (c.getHeight() / 2)- (textHeight / 2);
		this.enemyMiddleX = (int) ((percent / 100) * c.getWidth() - (enemy.getWidth() / 2));
		this.enemyWidth = enemy.getWidth();
		
		enemy.draw((float) ((percent / 100) * c.getWidth() - enemy.getWidth()), enemyY - (enemy.getHeight() / 2) );
		
		
		if(this.opening != null && percent < 90){
			Image o = opening.getScaledCopy(ratio);
			
			//o.draw(0, c.getHeight() - o.getHeight() - textHeight);
			
			int off = (int) ((1 - percent) * ratio * 8);
			off = off % o.getWidth();
			off += -1;
			//System.out.println("off: " + off);
			
			int yOff = 0;
			
			if(percent > 60){
				double p = (30 - (90 - percent)) / 30;
				yOff = (int) (p * o.getHeight());
			}
			
			
			this.drawRight(o, c, off - o.getWidth(), c.getHeight() - textHeight - o.getHeight() + yOff + ((int) ratio) + 1, false);
		}

		
		
	}
	public int getTeamMiddleX(){
		return this.teamMiddleX;
	}
	public int getEnemyMiddleX(){
		return this.enemyMiddleX;
	}
	public int getEnemyY(){
		return this.enemyY;
	}
	public int getTeamWidth() {
		return this.teamWidth;
	}
	public int getEnemyWidth(){
		return this.enemyWidth;
	}
	
	public int getTeamY(){
		return (int) this.teamY;
	}
	
	public void renderBlack(GameContainer c) {
		if(percent > 30){
			return;
		}
		double blackPercent = percent / 30;
		
		
		
		if(blackPercent < 1){
			
			int screenHeight = c.getHeight();
			
			int amount = (int) ((screenHeight) * (blackPercent));
			amount = screenHeight - amount;
			amount /= 2;
			//System.out.println("amount: " + amount);
			
			
			
			int width = black.getWidth();
			int height = black.getHeight();
			
			int off = width - (amount % width);
			
			for(int i = 0; i < amount; i+=height){
				
				for(int j = 0; j < c.getWidth(); j+= width){
					this.black.draw(j, i - height - off);
					this.black.draw(j, screenHeight - i + height + off);
				}
				
				
			}
		}
	}
	private int drawRight(Image image, GameContainer c, int xOff, int yOff, boolean drawDown){
		int width = image.getWidth();
		
		int r = 0;
		
		for(int i = xOff; i < c.getWidth(); i+= width){
			if(drawDown){
				for(int j = yOff; j < c.getHeight(); j+=image.getHeight()){
					image.draw(i, j);
					r = j;
				}
			} else {
				image.draw(i, yOff);
				r = image.getHeight() + yOff;
			}
		}
		
		return r;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourcePack pack = ClientTrainer.getClient().getMain().getResourcePack();
		BackgroundBubble b = animation.getBattle().getBackgroundType();
		
		top = b.getTop(pack);
		stripe = b.getStripe(pack);
		
		team = b.getTeamBubble(pack);
		enemy = b.getEnemyBubble(pack);
		
		opening = b.getOpeningImage(pack);
		if(opening != null){
			System.out.println("Got opening image");
		}
		
		ImageBuffer im = null;
		im = new ImageBuffer(20, 20);
		
		for(int i = 0; i < im.getWidth(); i++){
			for(int j = 0; j < im.getHeight(); j++){
				im.setRGBA(i, j, 0, 0, 0, 255);
			}
		}
		
		
		this.black = im.getImage();
		
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		percent += delta * 0.03;
		if(percent > 100){
			percent = 100;
		}
	}

	@Override
	public void stop() {
		percent = 100;
	}

	@Override
	public Screen getNext() {
		return this;
	}

	@Override
	public boolean isDone() {
		return percent >= 100;
	}
	


}
