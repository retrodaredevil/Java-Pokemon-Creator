package me.retrodaredevil.pokecreator.trainer;

import java.util.List;

import com.sun.istack.internal.Nullable;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Main;
import me.retrodaredevil.pokecreator.map.Block;
import me.retrodaredevil.pokecreator.map.Layer;
import me.retrodaredevil.pokecreator.map.Material;
import me.retrodaredevil.pokecreator.map.ObjectLayer.Section;
import me.retrodaredevil.pokecreator.map.PokeMap;
import me.retrodaredevil.pokecreator.map.PokeMap.LayerLayer;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.EndReason;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleIntro;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.Phase;
import me.retrodaredevil.pokecreator.trainer.client.text.AreaBubble;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog.Bubble;
import me.retrodaredevil.pokecreator.trainer.client.text.TextPopup.Speed;
import me.retrodaredevil.pokecreator.trainer.client.text.TextPopupList;
import me.retrodaredevil.pokecreator.trainer.controller.ClientController;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;
import me.retrodaredevil.pokecreator.util.Pending;
import me.retrodaredevil.pokecreator.util.Sound.Play;

/**
 * @author retro
 *
 */
public class ClientTrainer extends Trainer { // for use for single player or multiplayer client side // not to be used for a server without a player hosting

	private static ClientTrainer client;

	//	private Map<SoundType, Sound> soundMap = new HashMap<>();


	private PlayerAnimation an;

	private Main main;
	
	private Play playing = null;
	private SoundType musicType = null;
	private SoundType nextMusic = null;

	private TextPopupList popups;
	
	private AreaBubble areaBubble;
	private Speed textSpeed = Speed.NORMAL;

	
	private double zoom = 1;
	
	private String route = "none";

	private Battle battle = null;
	private BattleIntro bIntro = null;



	public ClientTrainer(String name, boolean girl, Main main) { // 468 // 332 //// 404 // 340        // 36 // 8   // 440 // 336
		super(name, girl);
		client = this;
		this.main = main;
		
		
		con = new ClientController(this);

		an = new PlayerAnimation(); 
		this.popups = new TextPopupList();
		
		areaBubble = new AreaBubble();
	}
	@Override
	public void init(GameContainer container) throws SlickException {
		super.init(container);
	}
	@Override
	public void sendAreaBubble(String text) {
		this.areaBubble.setText(text);
	}

	public Main getMain(){
		return main;
	}

	
	
	@Override
	public void playSound(SoundType type) {
		type.play(main.getResourcePack());
	}
	@Override
	public void setMusic(@Nullable SoundType music) {
		if(musicType == music || this.nextMusic == music){
			return; // the set music is already playing or a new song will soon play
		}
		if(playing != null && this.playing.isPlaying() && !this.playing.isDecrescendo()){
			playing.setDecrescendo(); // works
		}
		nextMusic = music;
	}	
	@Override
	public void resetMusic() {
		if(this.musicType != null){	
			if(this.musicType.isPlaying()){
				this.musicType.stopAll();
			}

			this.musicType.play(main.getResourcePack());

		}
	}

	public TextPopupList getPopupList(){
		return this.popups;
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException { // super's update will probably have something in it

		
		
		super.update(container, delta);
		Input i = container.getInput();
		this.popups.update(container, delta);
		
		
		if(this.isInBattle() && i.isKeyDown(Input.KEY_ESCAPE)){
			this.setBattle(null); // TODO remove this this is just to easily get back to the game
			System.out.println("removed battle.");
		}
		if(i.isKeyDown(Input.KEY_H) && i.isKeyDown(Input.KEY_LCONTROL)){
			healAllPokemon();
		}
		if(this.battle != null){
			BattleAnimation a = this.battle.getBattleAnimation();
			if(a != null){
				if(a.isDone()){
					battle = null;
					System.out.println("battle animation is done. Removed battle.");
				}
			}
		}
		if(!this.isInBattle() && battle != null){
			battle.update(container, delta);
		}
		
		for(Section s : this.getLocation().getLayer().getObjectLayer().getSections()){
			String route = s.getRoute();
			SoundType music = s.getMusic();
			
			if(!s.contains(this.getLocation().getCurrentBlock())){
				continue;
			}
			
			//System.out.println("inside: " + route);
			
			if(route != null && !this.route.equals(route)){
				this.sendAreaBubble(route);
				System.out.println("Entering new route: " + route + " old: " + this.route);
				this.route = route;
			}
			if(music != null){
				this.setMusic(music);
			}
			
		}
		
		this.areaBubble.update(container, delta);
		//this.areaBubble.setText("ROUTE LOL");
		
		
		if(playing == null || !playing.isPlaying()){
			if(playing != null){
				playing.stop();
			}
			playing = null;
			
			//System.out.println("playing is null");
		}

		if(playing == null && nextMusic != null){
			musicType = nextMusic;
			nextMusic = null;
		}
		if(playing == null && musicType != null && !SoundType.mute){
			playing = musicType.play(main.getResourcePack());
		}
			
		
		if(playing != null){
			playing.update();
		}
		
		if((i.isKeyDown(Input.KEY_LCONTROL) || i.isKeyDown(Input.KEY_RCONTROL))){
			if(i.isKeyPressed(Input.KEY_EQUALS)){
				this.zoom += 0.1;
			} else if(i.isKeyPressed(Input.KEY_MINUS)){
				this.zoom -=0.1;
			}
			if(zoom < 0.5){
				zoom = 0.5;
			}
			if(zoom > 2){
				zoom = 2;
			}
		}
		
		if(bIntro != null){
			if(!bIntro.isInitialized()){
				bIntro.init(container);
			}
			bIntro.update(container, delta);
			bIntro = bIntro.getNext(); // may set it to null
			//TODO in the Battle class somehow while bIntro is going on, the user may be able to change their hover position.
		}
	}

	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		//Input in = c.getInput();
		boolean inBattle = this.battle != null;
		
		if(inBattle && bIntro == null){ 
			this.getBattle().render(c, g, currentPack);
			
			
		} else {
			this.mapRender(c, g, currentPack);
			
		}


	}
	
	/**
	 * Also renders the AreaBubble and TextPopups
	 */
	private void mapRender(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException{
		final int ppb = (int) (this.getLocation().getLayer().getMap().getMap().getTileHeight() * zoom);

		final double percent = this.getPercentageToNextBlock();

		Location loc = this.getLocation();
		Layer layer = loc.getLayer();
		PokeMap currentMap = layer.getMap();


		currentMap.setRenderLayer(LayerLayer.BACKGROUND, zoom, percent);
		currentMap.render(c, g, currentPack);

		currentMap.setRenderLayer(LayerLayer.ENTITYLAYER,zoom, percent);
		currentMap.render(c, g, currentPack);


		{ // render player
			//final int ppb = 64; // pixels per block
			an.update(currentPack); // updating here because we have the currentPack variable here
			
			MovementType type = loc.getMovementType();

			Block on = loc.getCurrentBlock();
			Block target = loc.getTarget();

			if(loc.getMoving() == Direction.STILL && (loc.getMovementType() == MovementType.WALKING || loc.getMovementType() == MovementType.RUNNING)){
				type = MovementType.STANDING;
			}
			if(type == MovementType.WALKING && loc.isRunning()){
				type = MovementType.RUNNING;
			}
			if(loc.getMoving() != Direction.STILL && ((on.getMaterial() == Material.WATER && target.getMaterial() != Material.WATER) || (on.getMaterial() != Material.WATER && target.getMaterial() == Material.WATER))){
				type = MovementType.JUMPING; // will be jumping if the player is jumping on or is jumping off the water
			}
			long delay = type.getFrameDelay();
			Phase p = an.getPhase(type, loc.getLooking(), delay);
			if(p == null){
				p = an.getPhase(MovementType.WALKING, loc.getLooking().getNormal(), delay);
			}
			if(p == null){
				throw new NullPointerException("p is null. Looking: " + loc.getLooking());
			}
			//System.out.println(p.toString());
			Image image = p.getImage(this.isGirl());
			final int width = (int) ((ppb * 2));
			image = image.getScaledCopy(width, width);

			int x = Display.getWidth() / (ppb * 2);
			int y = Display.getHeight() / (ppb * 2);

			x *= ppb;
			y*= ppb;

			x-=ppb/2;
			y-=ppb;
			//			if(loc.getCurrentBlock().getAdd(Direction.DOWN).getMaterial() == Material.WATER){ // works but no transpartent and continues while leaving a water block
			//				Image reflection = image.getFlippedCopy(false, true);
			//				reflection.setFilter(100);
			//				reflection.draw(x, y + (ppb * 2));
			//			}
			
			
			if(type == MovementType.JUMPING ){
				Image shadow = Phase.SHADOW.getImage(this.isGirl());
						
						
				shadow = shadow.getScaledCopy(width, width); // doesn't matter should be same for each gender but remember to implement just incase 
				
				
				shadow.draw(x, y);

				
				
				boolean first = this.getLocation().getTarget().pushTo() != Direction.STILL;
				
				

				int amount = 80;
//				if(percent < 0.1){
//					amount = 10;
//				} else if(percent < 0.3){
//					amount = 20;
//				} else if(percent < 0.5){
//					amount = 32;
//				}
				
				double d;
				if(first){
					d = percent;
				} else {
					d = 1 - percent;
				}


				y -= amount * d * zoom;
			}

			image.draw(x, y);
		}


		currentMap.setRenderLayer(LayerLayer.FRONTLAYER, zoom, percent);
		currentMap.render(c, g, currentPack);

		g.setColor(Color.white);

// font24.dr ------ // "on: " + loc.getCurrentBlock().toString() + "target: " + loc.getTarget().toString() + " d: " + loc.getMoving() + " type: " + loc.getMovementType().toString()
		//currentPack.getFont(24f).drawString(250, 10, "sections: " + this.getLocation().getLayer().getObjectLayer().getSections().get(0).getSignText(), Color.white);
	
		this.popups.render(c, g, currentPack);
		this.areaBubble.render(c, g, currentPack);
		
		//g.drawString("on: " + loc.getCurrentBlock().toString() + "target: " + loc.getTarget().toString() + " d: " + loc.getMoving() + " type: " + loc.getMovementType().toString(), 250, 10);

		//g.setFont(Font.cre);
	}
	
	
	
	@Override
	public void sendText(List<String> text, Bubble bubble) {
		TextDialog message = new TextDialog(text);
		message.setSpeed(this.textSpeed);
		message.setBubble(bubble);
		this.popups.add(message);
		
		
		
	}
	@Override
	public Pending<String> sendText(List<String> text, String question, Bubble bubble) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Pending<String> sendText(List<String> text, String question, List<String> answers, Bubble bubble) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ClientTrainer getClient(){ // there's only going to be one client trainer
		return client;
	}
	@Override
	public Pending<Turn> getTurn(String canDo, BattlePokemon mon, boolean canStruggle) {
		return ((ClientController)this.con).createPending();
	}
	@Override
	public void tell(TurnOutcome doTurn, Battle battle) {
		BattleAnimation a = battle.getBattleAnimation();
		
		for(OutcomePart p : doTurn.getOutcome(battle)){
			a.getOutcomePartList().add(p);
			System.out.println(p.toString());
		}
		
		
	}
	@Override
	public Battle getBattle() {
		return this.battle;
	}
	@Override
	public boolean isInBattle() {
		Battle b = this.getBattle();
		if(b == null){
			return false;
		}
		if(b.isOver()){
			return false;
		}
		return true;
	}
	@Override
	public boolean setBattle(Battle b) {
		if(this.isInBattle()){
			if(b == null){
				battle.setOver(EndReason.UNKNOWN);
				battle = null; // TODO remove when not in use
				System.out.println("Set the battle to null for an unknown reason.");
				return true;
			}
			return false;
		}
		if(b != null){
			this.battle = b;
			this.bIntro = new BattleIntro(this, battle);
			
			System.out.println("Set the battle to something that's not null.");
			return true;
		}
		return false;
	}
	

}
