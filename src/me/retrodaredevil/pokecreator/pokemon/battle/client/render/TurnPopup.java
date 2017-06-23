package me.retrodaredevil.pokecreator.pokemon.battle.client.render;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import me.retrodaredevil.pokecreator.pokemon.attack.Attack;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.PokemonPart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.AttackTurn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.RunTurn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog;
import me.retrodaredevil.pokecreator.trainer.client.text.TextPopup;

/**
 * @author retro
 *
 */
public class TurnPopup implements TextPopup  {

	
	private static Image image;
	private static Image select;
	private static Image attackBubble;
	private static ResourcePack pack;
	
	
	private TurnPopup next = null;
	
	private List<String> options = new ArrayList<>();
	
	private PokemonPart part;
	private final BattleAnimation an;
	
	private volatile TurnMenu current;
	private Hover hover = Hover.UPPER_LEFT;
	
	private int runTries = 0;
	
	

	
	
	public TurnPopup(BattleAnimation an, PokemonPart part){
		this.part = part;
		this.an = an;
		this.setNone();
		
	}
	public void setPokemonPart(PokemonPart part){
		this.part = part;
		if(this.current != TurnMenu.NONE){
			this.setMain();
		}
	}
	
	@Override
	public List<String> getLines() {
		return options;
	}
	
	private static void update(ResourcePack current) throws SlickException{
		if(pack == null || pack != current){
			pack = current;
			image = new Image(pack.getStream("textures/battle/turn_bubble.png"), "turn_bubble", false);
			select = new Image(pack.getStream("textures/battle/turn_select.png"), "turn_select", false);
			attackBubble = new Image(pack.getStream("textures/battle/attack_bubble.png"), "attack_bubble", false);
		}
	}
	
	void setMain(){
		options.clear();
		
		options.add("FIGHT");
		options.add("BAG");
		options.add("POKEMON");
		options.add("RUN");
		this.current = TurnMenu.MAIN;
	}
	public void goBack(){
		this.setMain();
	}
	public void setNone(){
		this.current = TurnMenu.NONE;
		options.clear();
	}
	private void setFight(){
		options.clear();
		
		for(Attack a : this.part.getBattlePokemon().getPokemon().getAttacks()){
			options.add(a.getName().toUpperCase());
		}
		while(options.size() < 4){
			options.add("");
		}
		
		this.current = TurnMenu.FIGHT;
		
		
	}

	
	/**
	 * 
	 * @return the turn that will be selected. Null if none
	 */
	public Turn select(){
		
		if(current == TurnMenu.MAIN){
			if(hover == Hover.UPPER_LEFT){ // fight
				this.setFight();
				return null;
			} else if(hover== Hover.UPPER_RIGHT){ // bag
				
			} else if(hover == Hover.LOWER_LEFT){ // pokemon
				
			} else if(hover == Hover.LOWER_RIGHT){ // run
				runTries++;
				return new RunTurn(part.getBattleAnimation().getBattle().getBBatteler(part.getBattlePokemon().getPokemon().getOwner()), part.getBattleAnimation().getBattle(), this.part.getBattlePokemon(), runTries); // TODO implement the run turn
			}
		} else if(current == TurnMenu.FIGHT){
			List<Attack> a = this.part.getBattlePokemon().getPokemon().getAttacks();
			int number = hover.getNumber();
			if(number < a.size()){
				return new AttackTurn(a.get(number), this.part.getBattlePokemon(), part.getBattleAnimation().getBattle().getBBatteler(part.getBattlePokemon().getPokemon().getOwner()));
			} else {
				return null;
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	public void setHover(Hover hover){
		this.hover = hover;
	}
	public Hover getHover(){
		return hover;
	}
	public TurnMenu getMenuType(){
		return this.current;
	}
	
	
	
	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		
		if(this.current == TurnMenu.NONE){
			return;
		}
		
		update(currentPack);
		
		int width;
		int height;

		Image render;

		float ratio;
		
		boolean fight;
		
		if(this.current != TurnMenu.FIGHT){
			fight = false;
			render = image;
			width = c.getWidth() / 2;
			height = this.an.getDialogHeight();
			render.setFilter(Image.FILTER_NEAREST);
			render = render.getScaledCopy(width, height);
			render.draw(c.getWidth() - render.getWidth(), c.getHeight() - render.getHeight());
			ratio = render.getWidth() / 120f;
		} else {
			fight = true;
			render = attackBubble;
			width = c.getWidth();
			height = this.an.getDialogHeight();
			render.setFilter(Image.FILTER_NEAREST);
			render = render.getScaledCopy(width, height);
			render.draw(c.getWidth() - render.getWidth(), c.getHeight() - render.getHeight());
			ratio = render.getWidth() /  240f;
			
			width *= 2;
			width /= 3;
			
		}
		
		
		
		//System.out.println("ratio: " + ratio);
		
		UnicodeFont font = currentPack.getFont(ratio * 10);

		int xOff = (int) (15 * ratio);
		
		for(int i = 0; i < options.size(); i++){
			String o = options.get(i);
		
			int sh = font.getHeight(o);
			if(sh < 5){
				sh = font.getHeight("T");
			}
			
			int yOff =  (int) (12 * ratio);
			
			if(i >= 2){ // lower
				yOff += sh * 2;
				
			}
			int xRender;
			int yRender = c.getHeight() - render.getHeight() + yOff;//
			
			if(i % 2 == 0){ // left
				xRender = c.getWidth() - render.getWidth() + xOff;
				
				
			} else { // right
				xRender= c.getWidth() - (render.getWidth() / 2) + xOff;
				if(fight){
					xRender -= (width / 4);
				}
			}
			
			TextDialog.drawString(font, xRender, yRender, o, new Color(64, 64, 64));
			
			if(hover.getNumber() == i){
				//update(currentPack); // already updated at the top of method.
				Image select = TurnPopup.select;
				
				

	
				int sw = select.getWidth() * sh / select.getHeight();
				select.setFilter(Image.FILTER_NEAREST);
				select = select.getScaledCopy(sw, sh);
				int sx = xRender - (select.getWidth() * 7 / 5);

				select.drawFlash(sx + (ratio / 2), yRender + ratio, select.getWidth(), select.getHeight(), new Color(208, 200, 208));
				select.draw(sx, yRender);
				
			}
			
		}
		
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
		//System.out.println("hover: " + this.getHover().toString());
		
	}

	@Override
	public void stop() {
	}

	@Override
	public TextPopup getNext() {
		if(next != null){
			return next;
		}
		
		return this;
	}

	@Override
	public boolean keepUnlessWaiting() {
		return false;
	}
	
	
	public static enum Hover {
		UPPER_LEFT(0),UPPER_RIGHT(1),LOWER_LEFT(2),LOWER_RIGHT(3);
		
		
		private int number;
		
		private Hover(int number){
			this.number = number;
			
		}
		
		
		public int getNumber(){
			return number;
		}
		
		
		public static Hover getHover(int number){
			for(Hover hover : values()){
				if(hover.getNumber() == number){
					return hover;
				}
			}
			return null;
		}
		
		
	}
	public static enum TurnMenu {
		NONE,MAIN,FIGHT,OTHER
	}
	
	
	
	
	
	

}
