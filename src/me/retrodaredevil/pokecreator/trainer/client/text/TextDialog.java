package me.retrodaredevil.pokecreator.trainer.client.text;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.trainer.controller.ClientController.Button;

/**
 * @author retro
 *
 */
public class TextDialog  implements  TextPopup, SpeedObject {

	
	private Speed speed = Speed.NORMAL;
	
	private Bubble bubble = Bubble.BATTLE_NORMAL;
	
	private List<String> text;
	private String current;
	private int currentPart = 0;
	
	private boolean done = false;
	private boolean doneWithPart = false;
	
	private Image doneArrow = null;
	private ResourcePack doneArrowSource = null;
	
	private long last;
	
	private TextPopup next = null;
	
	private int lastHeight = 0;
	private int lastWidth = 0;
	
	private boolean useButtons = true;
	private long doneTime = 0;
	
	
	/**
	 * creates a TextDialog
	 * 
	 * @param text the text to display. Each separate string is on a separate page. If text.size is 0, keepUnlessWaiting will return true
	 */
	public TextDialog(List<String> text){
		this.text = text;
		this.current = "";
		
		last = 0;
		
	}
	public void setUseButtons(boolean b){
		this.useButtons = b;
	}
	
	public void setNext(TextPopup text){
		this.next = text;
	}
	public void setBubble(Bubble bubble){
		this.bubble = bubble;
	}
	@Override
	public List<String> getLines() {
		return text;
	}
	
	public String getCurrent(){
		return this.current;
	}
	
	
 	@Override
 	public void init(GameContainer container) throws SlickException {
 	}	
 	
 	private boolean wasDown = false;
 	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(done){
			//System.out.println("returnig cuz done");
			return;
		}
		if(!this.useButtons && doneTime != 0){
			if(doneTime <= System.currentTimeMillis()){
				done = true;
				//System.out.println("returnig cuz setting done");
				return;
			}
		}
		
		if(currentPart >= text.size()){ 
			if(useButtons){
				if(Button.A.wasDown() || Button.B.wasDown()){
					done = true;
				}
			} else if(doneTime == 0){
				//System.out.println("Set done time to now.");
				doneTime = System.currentTimeMillis() + 700;
			}
			//System.out.println("returnig cuz currentPart >= text.size");
			return;
		}
		long perChar =  speed.getTimePerChat();
		if((Button.A.wasDown() || Button.B.wasDown()) && useButtons){
			perChar /= 2;
		}
		if(last == 0 || last + perChar <= System.currentTimeMillis()){
			
			boolean wasDown = this.wasDown;
			this.wasDown = Button.A.wasDown() || Button.B.wasDown();

			
			last = System.currentTimeMillis();
			String full = text.get(currentPart); 

			int chars = current.length();
			
			if(current.equals(full)){
				doneWithPart = true;
				if((useButtons && this.wasDown && !wasDown) || (!useButtons)){
					if(useButtons){
						SoundType.SOUND_SELECT.play(ClientTrainer.getClient().getMain().getResourcePack());
					}
					
					currentPart++; // DONE maybe implement only doing this if select is pressed DONE
					if(useButtons || currentPart < text.size()){
						current = "";
					}
					doneWithPart = false;
					//System.out.println("currentPart: " + currentPart + " text.size: " + text.size());
				}
				return;
			}
			current = current + full.charAt(chars);
			
		}
	}
	@Override
	public void render(GameContainer arg0, Graphics g, ResourcePack currentPack) throws SlickException {
		
		Image image = bubble.getImage(currentPack);
		
		final int screenWidth = arg0.getWidth();
		image.setFilter(Image.FILTER_NEAREST);
		float ratio1 = screenWidth / image.getWidth();
		
		image = image.getScaledCopy(screenWidth, (int) (image.getHeight() * ratio1)); // scale the image
		
		int y = arg0.getHeight() - image.getHeight();
		
		image.draw(0, y);
		
		this.lastWidth = image.getWidth();
		this.lastHeight = image.getHeight() - (image.getHeight() / 50);
		
		
		if(this.text.size() == 0){
			return;
		}
		
		final int imageWidth = image.getWidth(); 		
		final int pixelWidth = 238; // is the width of the bubble--..d--.png is before being resized. Used as a final value and is just a way to make sure the text is lined up.
	
		final double ratio = imageWidth / pixelWidth; // this ratio is not based off of the original image source meaning that if you resize the source this should not change if the screen size is the same
		
		final int xOff = (int) (ratio * bubble.getXOff());
		final int yOff = (int) (ratio * bubble.getYOff());
		
		
		final int xRender = xOff;
		final int yRender = yOff + y;
		
		final float size = (float) ratio * 10;
		
		
		Font font = currentPack.getFont(size);
		int backOff = (int) Math.ceil(ratio / 1.5);
		//font.drawString(backOff + xRender, backOff + yRender, current, Color.lightGray);
//		font.drawString((backOff / -3) + xRender, yRender, current, Color.gray);
//		font.drawString(xRender, (int) ((backOff * 1.1) + yRender), current, Color.gray);
		
		String[] split = current.split(" ");
		int part = 0;
		
		
		String first = "";
		String second = "";
		
		String lastFirst = "";
		boolean widthboo = false;
		while((widthboo = font.getWidth(first) < screenWidth - (xRender * 2)) && (part < split.length)){
			lastFirst = first;
			
			String space;
			if(first.length() > 0){
				space = " ";
			} else {
				space = "";
			}
			first = first + space + split[part];
			part++;
			
			
			if(first.contains("\n") || first.contains("\\n")){

				first = first.replaceAll("\n", "");
				first = first.replaceAll("\\\\n", "");
				break;
			} else if (part + 1 < split.length && (first + split[part + 1]).contains("\\n")){
				first = first.replaceAll("\\\\", "");
				
			}
		}
		if(!widthboo){ 
			first = lastFirst;
			part--;
		} 
//		first = first.replaceAll("\n", "");
//		first = first.replaceAll("\\\\n", "");
		//System.out.println(first);
//		else if (text.size() > 0){
//			split = text.get(0).split(" ");
//		} // doesn't work
		
		while(font.getWidth(second) < screenWidth - (xRender * 2) && part < split.length){
			//lastSecond = second;
			
			String space;
			if(second.length() > 0){
				space = " ";
			} else {
				space = "";
			}
			second = second + space + split[part];
			part++;
		}
		//second = lastSecond;
		
		int yRenderSecond = arg0.getHeight() - (yOff + font.getHeight(second));

		drawString(font, xRender, yRender, first, bubble.getDefaultTextColor(), backOff);
		drawString(font, xRender, yRenderSecond, second, bubble.getDefaultTextColor(), backOff); // second
		if(this.doneWithPart && this.currentPart < this.text.size() - 1){
			if(doneArrow == null || this.doneArrowSource != currentPack){
				doneArrowSource = currentPack;
				
				doneArrow = new Image(currentPack.getStream("textures/text/done_arrow.png"), "done_arrow", false);
				
			}
			int d = (int) (6 * ratio);
			doneArrow.setFilter(Image.FILTER_NEAREST);
			Image render = doneArrow.getScaledCopy(d, d);
			
			
			int doneX;
			int doneY;
			
			if(second.length() == 0){
				doneX = font.getWidth(first);
				doneY = yRender;
			} else {
				doneX = font.getWidth(second);
				doneY = yRenderSecond;
			}
			int off = (int) ((System.currentTimeMillis() / 50) % 20);
			if(off >= 10){
				off = 20 - off;
			}
			doneY+=off;
			
			doneX+=xOff;
			
			render.draw(doneX, doneY);
			
			//drawString(currentPack.getFont(16), 100, 200, "wasDown: " + this.wasDown, Color.white, backOff);
			//System.out.println("Scale: " + scale + " width: " + render.getWidth());
		}
		//System.out.println("yay rendering: " + first + " | " + second + " | " + current);
		//System.out.println(current);
	}
	
	public static void drawString(Font font, int x, int y, String text, Color color, int backOff){
		font.drawString(backOff + x, backOff + y, text, Color.lightGray);
		
		font.drawString(x, y, text, color);
	}
	public static void drawString(Font font, int x, int y, String text, Color color){
		drawString(font, x, y, text, color, font.getHeight(text) / 10);
	}
	public int getLastHeight(){
		return this.lastHeight;
	}
	public int getLastWidth(){
		return this.lastWidth;
	}

	@Override
	public void stop() {
		done = true;
	}

	@Override
	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	@Override
	public Speed getSpeed() {
		return speed;
	}

	@Override
	public TextPopup getNext() {
		if(done){
			
			return this.next;
		}
		return this;
	}
	@Override
	public boolean keepUnlessWaiting() {
		return this.text.size() == 0;
	}
	
	
	
	
	
	public static enum Bubble {
		BUBBLE(0, new Color(49, 81, 206)),
		GREY(1, Color.darkGray),
		BATTLE_NORMAL(2, Color.white),
		BATTLE_THICK(3, Color.white)
		;
		private static ResourcePack imageSource = null; // static because when updateImages is called it will update all of them

		private Image image;
		
		private final int y;
		private final Color textColor;
		
		
		private Bubble(int y, Color textColor){
			this.y = y;
			this.textColor = textColor;
			
			
		}
		
		public int getXOff() {
			return 15;
		}
		public int getYOff(){
			return 12;
		}
		
		public Color getDefaultTextColor(){
			return textColor;
		}
		
		public Image getImage(ResourcePack current) throws SlickException{
			if(imageSource != current){
				updateImages(current);
			}
			
			return image; // TODO
		}
		
		private static void updateImages(ResourcePack pack) throws SlickException{
			Image allImage = new Image(pack.getStream("textures/text/text_bubbles.png"), "bubbles", false);
			SpriteSheet all = new SpriteSheet(allImage, allImage.getWidth(), allImage.getHeight() / 4); // so this image can be any dimensions
			for(Bubble b : Bubble.values()){
				b.image = all.getSprite(0, b.y);
			}
			
			imageSource = pack;
		}
		
	}
	

}
