package me.retrodaredevil.pokecreator.pokemon.battle.client.render;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import me.retrodaredevil.pokecreator.resources.ResourcePack;

/**
 * @author retro
 *
 */
public enum BackgroundBubble {
	NORMAL(0),
	GRASS(1, "grass"),
	WATER(2),
	GROUND(3),
	SWAMP(4),
	EF_ICE(5),
	EF_GROUND(6),
	EF_GHOST(7),
	EF_DRAGON(8),
	EF_CHAMPION(9)
	
;
	private static Image sheet = null;
	private static ResourcePack lastPack;
	
	private final int number;
	
	private Image image;
	
	private Image top;
	private Image stripe;
	
	private Image enemy;
	private Image team;
	
	private Image opening;
	
	private String openingName = null;
	
	private BackgroundBubble(int number){
		this.number = number;
	}
	
	private BackgroundBubble(int number, String openingName){
		this(number);
		this.openingName = openingName;
	}
	
	private void update(ResourcePack pack) throws SlickException{
		if(lastPack == null || lastPack != pack || sheet == null || image == null){
			lastPack = pack;
			
			sheet = new Image(pack.getStream("textures/battle/background.png"), "battle_background", false);
			
			System.out.println("sheet w: " + sheet.getWidth() + " h: " + sheet.getHeight());
			
			int amount = sheet.getHeight() / 10; // 112
			
			SpriteSheet tempSheet = new SpriteSheet(sheet, sheet.getWidth(), amount);
			
			image = tempSheet.getSprite(0, number);
			System.out.println("image w: " + image.getWidth() + " h: " + image.getHeight());
			
			int topSize = (int) (amount / 3.5); // 32
			top = image.getSubImage(0, 0, image.getWidth(), topSize);
			
			int stripeSize = amount / 28; // 4
			stripe = image.getSubImage(0, topSize, image.getWidth() / 2, stripeSize);
			
			int bwidth = (int) (image.getWidth() / 1.875);
			
			int enemySize = (int) (amount / 3.2); // 35
			enemy = image.getSubImage(image.getWidth() - bwidth, amount - (topSize + enemySize), bwidth, enemySize);
			
			
			int teamSize = amount / 7; // 14
			team = image.getSubImage(0, amount - teamSize, bwidth, teamSize);
			
			if(this.openingName != null){
				opening = new Image(pack.getStream("textures/battle/" + this.openingName + ".png"), "opening: " + openingName, false);
				opening.setFilter(Image.FILTER_NEAREST);
				System.out.println("there is an opening image for: " + this.name());
			}

			image.setFilter(Image.FILTER_NEAREST);
			top.setFilter(Image.FILTER_NEAREST);
			stripe.setFilter(Image.FILTER_NEAREST);
			
			enemy.setFilter(Image.FILTER_NEAREST);
			team.setFilter(Image.FILTER_NEAREST);
		
			List<Color> colors = new ArrayList<>();
			for(int i = 0; i < stripe.getHeight(); i++){
				Color c = stripe.getColor(0, i);
				if(!colors.contains(c)){
					colors.add(c);
					System.out.println("Added color: " + c.toString());
				}
			}
			//removeColors(colors, enemy);
			//removeColors(colors, team);
	
			
		}
	}
	private static void removeColors(List<Color> colors, Image image) throws SlickException{

		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				Color c = image.getColor(x, y);
				if(colors.contains(c)){
					g.fillRect(x, y, 1, 1);
					System.out.println("filling Rect");
				}
			}
		}
		g.flush();
		
	}
	
	public Image getImage(ResourcePack pack) throws SlickException{
		this.update(pack);
		
		
		return image;
	}
	public Image getTop(ResourcePack pack) throws SlickException{
		this.update(pack);
		return top;
	}
	public Image getStripe(ResourcePack pack) throws SlickException{
		this.update(pack);
		return stripe;
	}
	public Image getEnemyBubble(ResourcePack pack) throws SlickException{
		this.update(pack);
		return enemy;
	}
	public Image getTeamBubble(ResourcePack pack) throws SlickException{
		this.update(pack);
		return team;
	}
	
	
	public Image getOpeningImage(ResourcePack pack) throws SlickException{
		this.update(pack);
		return this.opening;
	}
	
	
	
}
