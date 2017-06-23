package me.retrodaredevil.pokecreator.trainer.client.text;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.resources.ResourcePack;

public class AreaBubble implements Screen{

	private boolean goDown = true;
	
	private double percentOut = 0;
	
	private String text = null;
	private String nextText = null;
	
	private long timeGoDown = 0;
	
	private Image image;
	private ResourcePack imageSource;
	
	public AreaBubble(){
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see me.retrodaredevil.pokecreator.Screen#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics, me.retrodaredevil.pokecreator.resources.ResourcePack)
	 */
	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		if(image == null || imageSource != currentPack){
			image = new Image(currentPack.getStream("textures/text/area_bubble.png"), "area_bubble", false);
		}
		int width = arg0.getWidth() / 2;
		int height = width / 4;
		image.setFilter(Image.FILTER_NEAREST);
		Image render = image.getScaledCopy(width, height);
		
		
		final int renderY = (int) (		((100 - percentOut) / 100) * height * -1	);
		
		
		render.draw(0, renderY);
		
		//System.out.println("renderY: " + renderY + " isDown: " + this.isDown() + " text: " + text+  " nextText: " + this.nextText);
		if(text == null){
			return;
		}
		Font font = currentPack.getFont(height * 0.4f);
		
		
		final int textX = (render.getWidth() / 2) - (font.getWidth(this.text) / 2);
		final int textY = renderY + (render.getHeight() / 2) - font.getHeight(this.text);
		
		//font.drawString(textX, textY, text, Color.darkGray);
		TextDialog.drawString(font, textX, textY, text, Color.darkGray);
		
		
	}
	public void setText(String t){
		nextText = t;
		if(this.percentOut == 100){
			goDown = true;
			this.timeGoDown = 0;
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
		

		int amount = delta / 2;
		if(goDown){
			percentOut -=amount;
		} else {
			percentOut+= amount;
		}
		if(percentOut < 0){
			percentOut = 0;
		} else if(percentOut > 100){
			percentOut = 100;
		}
		if(this.isDown()){
			text = null;
		}
		if(this.isDown() && this.nextText != null){
			this.text = this.nextText;
			this.nextText = null;
			this.goDown = false;
			this.timeGoDown = System.currentTimeMillis() + 2000;
			System.out.println("making it go up.");
		}
		if(this.timeGoDown <= System.currentTimeMillis()){
			goDown = true;
		}
	}

	@Override
	public void stop() {
		goDown = true;
	}
	public boolean isDown(){
		return goDown && percentOut == 0;
	}

	@Override
	public Screen getNext() {
		return this;
	}

}
