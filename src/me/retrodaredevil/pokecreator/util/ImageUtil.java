package me.retrodaredevil.pokecreator.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;

/**
 * @author retro
 *
 */
public class ImageUtil {

	
	/**
	 * 
	 * @deprecated doesn't work lol
	 */
	@Deprecated
	public static void  setColorDeprecated(Image image, int x, int y, Color set) {
		Texture texture = image.getTexture();
		byte[] pixelData = texture.getTextureData();
		
		
		float textureOffsetX = image.getTextureOffsetX();
		float textureOffsetY = image.getTextureOffsetY();
		
		int xo = (int) (textureOffsetX * texture.getTextureWidth());
		int yo = (int) (textureOffsetY * texture.getTextureHeight());
		
		if (image.getTextureWidth() < 0) {
			x = xo - x;
		} else {
			x = xo + x;
		} 
		
		if (image.getTextureHeight() < 0) {
			y = yo - y;
		} else {
			y = yo + y;
		}
		
		int offset = x + (y * texture.getTextureWidth());
		offset *= texture.hasAlpha() ? 4 : 3;
		
		byte r = translate(set.getRed());
		byte g = translate(set.getGreen());
		byte b = translate(set.getBlue());
		pixelData[offset] = r;
		pixelData[offset + 1] = g;
		pixelData[offset + 2] = b;
		if(texture.hasAlpha()){
			byte a = translate(set.getAlpha());
			pixelData[offset + 3] = a;
		}
		
		
		
	}
	private static byte translate(int b) {
		if (b < 0) {
			return (byte) (256 + b);
		} else if( b >= 256){
			return (byte) (b - 256);
		}
		
		return (byte) b;
	}

	
	public static void setColor(Image image, int x, int y, Color set) throws SlickException{
		
		Color current = image.getColor(x, y);
		if(current.equals(set)){
			return;
		}
		
		Graphics g;
		g = image.getGraphics();
		g.setColor(set);
		g.fillRect(x, y, 1, 1);
		g.flush();

		
	}
	/**
	 * 
	 * @param image the image to perform the action on
	 * @param replace the color to be replaced
	 * @param set the color to replace the color being replaced
	 * @param minX the minimum x value while replacing
	 * @param maxX the maximum x value while replacing
	 * @param minY the minimum y value while replacing
	 * @param maxY the maximum y value while replacing
	 * @throws SlickException
	 */
	public static void replaceColor(Image image, Color replace, Color set, int minX, int maxX, int minY, int maxY) throws SlickException{

		Graphics g = image.getGraphics();
		g.setColor(set);
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		for(int x = minX; x < width && x <= maxX; x++){
			for(int y = minY; y < height && y <= maxY; y++){
				Color c = image.getColor(x, y);
				
				if(c.equals(replace)){
					g.fillRect(x, y, 1, 1);
				}
				
			}
		}
		g.flush();
		
		
	}
	
	
}
