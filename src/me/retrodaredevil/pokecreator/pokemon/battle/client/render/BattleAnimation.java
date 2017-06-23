package me.retrodaredevil.pokecreator.pokemon.battle.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BBatteler;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.TurnPopup.TurnMenu;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePartList;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.BattleBackground;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.BattlePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.HPBarPart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.PokemonPart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.PokemonPart.BattleSide;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog.Bubble;
import me.retrodaredevil.pokecreator.trainer.client.text.TextPopup;
import me.retrodaredevil.pokecreator.trainer.client.text.TextPopupList;
import me.retrodaredevil.pokecreator.util.Pending;

/**
 * @author retro
 *
 */
public class BattleAnimation implements Screen{
	
	
	private final TextPopupList popups = new TextPopupList();
	private final List<BattlePart> parts = new ArrayList<>();
	
	private final OutcomePartList outcomeParts = new OutcomePartList();
	
	private BattleBackground backgroundPart;
	
	private final TurnPopup turnSelect;
	
	
	private Pokemon selected = null;
	
	
	private Battle battle;
	
	private BattleSide side = BattleSide.TEAM1;
	
	private Map<BattlePokemon, PokemonPart> partMap = new HashMap<>();
	
	private int dialogHeight = 0;
	
	
	private BBatteler clientBatteler;
	
	public BattleAnimation(Battle battle){
		this.battle = battle;

		if(battle.isDoubleBattle()){
			System.err.println("Battle Animation not prepared for a double battle. Please update the Battle Animation class.");
		}
		
		
		ClientTrainer trainer = ClientTrainer.getClient();
		if(trainer == null){
			throw new NullPointerException("A BattleAnimation object was attempted to be created but there is no client trainer.");
		}
		this.clientBatteler = this.battle.getBBatteler(trainer);

//		for(BBatteler b : battle.getBattelers2()){
//			if(b.getBatteler().equals(trainer)){
//				side = BattleSide.TEAM2;
//				break;
//			}
//		}
//		team = new PokemonPart(this, battle.getBattelers1().get(0).getBattlePokemon().get(0)); // TODO
//		enemy = new PokemonPart(this, battle.getBattelers2().get(0).getBattlePokemon().get(0));
		
		turnSelect = new TurnPopup(this, null);
		//this.selected = enemy.getBattlePokemon().getPokemon();
		
	}
	
	public boolean isDone(){
		return this.getBattle().isOver() && this.outcomeParts.getAllValues().size() == 0;
	}
	
//	public TextPopupList getTextpopupList() {
//		return this.popups;
//	}
	public int getDialogHeight(){
		return this.dialogHeight;
	}
	
	public OutcomePartList getOutcomePartList(){
		//System.out.println("got the part list.");
		return this.outcomeParts;
	}
	
	public boolean hasSelected(){
		return this.getSelected() != null;
	}
	
	public Pokemon getSelected(){
		return selected;
	}
	
	/**
	 * 
	 * @return returns the BattleSide that is the team that will have their back turned towards you (Not the enemy team)
	 */
	public BattleSide getBattleSide(){
		return side;
	}
	public Battle getBattle(){
		return battle;
	}

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		this.backgroundPart.render(c, g, currentPack);
		
		Iterator<BBatteler> it = this.getBattle().multiListIterator();
		
		while(it.hasNext()){
			BBatteler b = it.next();
			for(BattlePokemon p : b.getBattlePokemon()){
				
				if(!p.isOut()){ // only run rest of code if the pokemon is out
					continue;
				}
				
				PokemonPart part = this.partMap.get(p);
				if(part == null){
					part = new PokemonPart(this, p);
					this.partMap.put(p, part);
				}
				
				
				
				part.render(c, g, currentPack);
//				{
//					Pending<Turn> pend = p.getPendingTurn();
//					if(pend == null){
//						continue;
//					}
//					if(!pend.hasAnswer()){
//						this.turnSelect.setPokemonPart(part);
//					}
//				}
				
			}
		}
		this.outcomeParts.render(c, g, currentPack);
		
	
		
//		team.render(c, g, currentPack);
//		enemy.render(c, g, currentPack);
		if(outcomeParts.getAllValues().size() == 0){
			TurnMenu menu = turnSelect.getMenuType();
			//System.out.println(menu);
			if (menu != TurnMenu.FIGHT){
				popups.render(c, g, currentPack);
//				TextPopup up = popups.getCurrent();
//				String display = "null";
//				
//				if(up != null){
//					display = popups.getCurrent().getLines().toString();
//				}
//				
//				System.out.println("Rendering popups: '" + display + "'");
			}
			turnSelect.render(c, g, currentPack);
		}
	
		backgroundPart.renderBlack(c);
		
		
	}
	
	

	@Override
	public void init(GameContainer container) throws SlickException {
		this.backgroundPart = new BattleBackground(this);
		backgroundPart.init(container);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {

		//System.out.println("size: " + outcomeParts.getAllValues().size());
		
		{
			outcomeParts.update(container, delta);
//			OutcomePart part = this.outcomeParts.getCurrent();
//			if(part != null){
//				System.out.println(part.toString());
//			}
		}
		
		
		popups.update(container, delta); // to make sure that the current is not null
//		if(turnSelect.getMenuType() == TurnMenu.FIGHT){
//			popups.finishCurrent();
//			//System.out.println("Finished current");
//		} 
		
		if(this.backgroundPart.getPercent() == 100) {

			//this.setSelected(this.team.getBattlePokemon().getPokemon()); // TODO make this more better I guess (selected pokemon in BattleAnimation)
			if(this.selected == null){
				
				BBatteler b = this.clientBatteler;
				for(BattlePokemon p : b.getBattlePokemon()){

					PokemonPart part = this.partMap.get(p);
					if(part == null){
						part = new PokemonPart(this, p);
						this.partMap.put(p, part);
					}
					
					part.update(container, delta); // TODO make sure that this will only update if it's not out // not putting if statement here because it may still need to animate it going down
					if(p.isOut()){
						Pending<Turn> pend = p.getPendingTurn(b);

						if(!pend.hasAnswer()){
							this.turnSelect.setPokemonPart(part);
							this.selected = p.getPokemon();
						}
					}

				}
				
			}
			TextPopup pop = popups.getCurrent();
			if(pop == null || pop.getLines().size() == 0){
				
				TextDialog add = new TextDialog(Arrays.asList("What will \n " + this.getSelected().getShortName() + " do?")){
					@Override
					public boolean keepUnlessWaiting() {
						return true;
					}
				};
				add.setUseButtons(false);
				add.setBubble(Bubble.BATTLE_NORMAL);
				this.popups.add(add); 
				popups.update(container, delta);
				//System.out.println("Added the textdialog. equal: " + (popups.getCurrent() == add) + " if false problem keepUnlessWaiting: " + pop.keepUnlessWaiting());
				pop = add;
			}
			

			if(!this.outcomeParts.hasCurrent() && turnSelect.getMenuType() == TurnMenu.NONE){
				turnSelect.setMain();
			} 
			turnSelect.update(container, delta);
			
			if(outcomeParts.getAllValues().size() > 0){
				turnSelect.setNone();
			}
		} else {
			turnSelect.setNone();
		}

		
		
		
		if(!popups.hasCurrent()){
			TextDialog text = new TextDialog(Arrays.asList());
			text.setBubble(Bubble.BATTLE_NORMAL);
			popups.add(text);
			//System.out.println("Added a blank TextDialog to popups. kus: " + text.keepUnlessWaiting() + " should be true <");
		} 
		int heightSet = popups.getDialogHeight();
		if(heightSet > 3){
			this.dialogHeight = heightSet;
		}
		
		this.backgroundPart.update(container, delta);
	}
	
	public List<BattlePart> getParts(){
		return parts;
	}
	

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Screen getNext() {
		return this;
	}
	public BattleBackground getBattleBackground() {
		return this.backgroundPart;
	}
	public TurnPopup getTurnPopup() {
		return this.turnSelect;
	}

	public HPBarPart getHPBar(BattlePokemon mon){
		
		
		return this.partMap.get(mon).getHPBarPart();
	}
	
	
}