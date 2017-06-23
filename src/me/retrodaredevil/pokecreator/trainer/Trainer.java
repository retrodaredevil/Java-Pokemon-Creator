package me.retrodaredevil.pokecreator.trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.map.Block;
import me.retrodaredevil.pokecreator.map.Layer;
import me.retrodaredevil.pokecreator.map.Material;
import me.retrodaredevil.pokecreator.pokemon.PType;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.Stat;
import me.retrodaredevil.pokecreator.pokemon.Stat.OutOf;
import me.retrodaredevil.pokecreator.pokemon.attack.AttackList;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BackgroundBubble;
import me.retrodaredevil.pokecreator.pokemon.battle.trainer.Batteler;
import me.retrodaredevil.pokecreator.pokemon.battle.trainer.WildBatteler;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog.Bubble;
import me.retrodaredevil.pokecreator.trainer.controller.Controller;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;
import me.retrodaredevil.pokecreator.util.Pending;
import me.retrodaredevil.pokecreator.util.file.JSONFile;

/**
 * @author retro
 *
 */
public abstract class Trainer implements Screen, Batteler{

	
	
	private String name;
	
	protected Controller con;
	private Location loc;
	

	
	private boolean girl = false;
	
	private JSONFile dataFile = null; 
	
	private List<Pokemon> active = new ArrayList<>();
	
	
	public Trainer(String name, boolean girl){
		this.name = name;
		
	
		
	}
	public String getName(){
		return name;
	}
	public boolean isGirl(){
		return girl;
	}
	public boolean isBoy(){
		return !girl;
	}
	
	public void spawn(Layer map, Block block, Direction d){
		
		loc = new Location(block, 0, map, d);
		this.setMusic(map.getMusic());
	}
	
	public boolean isSpawned(){
		return loc != null;
	}
	public Location getLocation(){
		return loc;
	}
	
	/**
	 * 
	 * @deprecated because not tested and should not be called normally until checked
	 * @param p1 the first pokemon to switch
	 * @param p2 the pokemon to switch with p1
	 * @return returns false if this can't be performed (ex. if a trainer is in a battle)
	 */
	@Deprecated
	public boolean switchPokemon(Pokemon p1, Pokemon p2){
		
		if(this.getBattle() != null){
			return false;
		}
		
		int pos1 = 0;
		int pos2 = 0;
		
		for(int i = 0; i < active.size(); i++){
			Pokemon mon = active.get(i);
			
			if(mon.equals(p1)){
				pos1 = i;
			} 
			if(mon.equals(p2)){
				pos2 = i;
			}
		}
		
		active.set(pos1, p2);
		active.set(pos2, p1);
		
		
		return true;
	}
	
	public void healAllPokemon(){
		for(Pokemon p : active){
			p.fullyHeal();
		}
	}
	
	
	public abstract void playSound(SoundType type);
	public abstract void setMusic(@Nullable SoundType music);
	
	/**
	 * used to start the music from the beginning
	 */
	public abstract void resetMusic();
	
	
	
	
	/**
	 * 
	 * @param text text to be displayed
	 */
	public abstract void sendText(List<String> text, Bubble bubble);
	
	/**
	 * Use this method to ask a trainer a multiple choice question
	 * @param text the text to be displayed before the question
	 * @param question the question to be asked
	 * @param answers the answers the trainer has to choose from
	 * @return one of the possible answer choices 
	 */
	public abstract Pending<String> sendText(List<String> text, String question, List<String> answers, Bubble bubble);
	
	/**
	 * Use this method to ask the trainer a "free response" question
	 * @param text the text to be displayed before the question
	 * @param question the question to be asked
	 * @return a string typed be the trainer
	 */
	public abstract Pending<String> sendText(List<String> text, String question, Bubble bubble);
	

	
	public abstract void sendAreaBubble(String text);
	
	

	
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		con.run(container, delta);
		
//		Location loc = this.getLocation();
//		if(!this.isInBattle() && loc.getCurrentBlock().getMaterial() == Material.GRASS && this.getBattle() == null){ // TODO implement actual spawns
//			Battle b = null;																// ^ for client trainer mostly
//			b = new Battle(false, Arrays.asList(this), Arrays.asList(new WildBatteler(null, new Pokemon("enemy", PType.getType(1), new Stat(6, 10, 10, 10, 10, 10, 31), null, -1, -1, 5, Arrays.asList(AttackList.TACKLE)))), BackgroundBubble.GRASS);// battling ourselfs lol change 
//			this.setBattle(b);
//		}
//		if(this.isInBattle()){ // called in maphandler
//			this.getBattle().update(container, delta); // TODO move this to a different spot
//			
//		}
		
	}
	
	public void onBlockChange(Block previous, Block current){
		if(this.isInBattle() || this.getBattle() != null){
			System.err.println("Someone is moving while they are in a battle. returning.");
			return;
		}
		//System.out.println("Fired onBlockChange");
		Pokemon spawn = current.getMaterial().getSpawn(current);
		if(spawn != null){
			System.out.println("Setting a battle with: " + spawn.getShortName());
			
			BackgroundBubble bubble = BackgroundBubble.GRASS;
			Material m = current.getMaterial();
			if(m == Material.WATER){
				bubble = BackgroundBubble.WATER;
			} else if(m == Material.CAVE){
				bubble = BackgroundBubble.GROUND;
			}
			
			Battle b = new Battle(false, Arrays.asList(this), Arrays.asList(new WildBatteler(null, spawn)), bubble);
			this.setBattle(b);
		}
		
	}

	
	public boolean canRun(){
		return true;
	}
	
	public boolean canSurf(){ 
		return true;
	}
	
	
	
	/**
	 * 
	 * @return the percentage the player is to the next block returns 0 <= x <= 1
	 */
	public double getPercentageToNextBlock(){
		boolean run = this.loc.isRunning();
		long difference = System.currentTimeMillis() - loc.getTimeMoved();
		
		Block on = loc.getCurrentBlock();
		
		double d = 300; // the bigger, the longer is takes to get to a block
		if(run && on.canRun()){
			d = 150;
		}
		MovementType type = this.getLocation().getMovementType();
		
//		if(this.getLocation().getTarget().pushTo() != Direction.STILL){
//			type = MovementType.JUMPING;
//		}
		
		if(type == MovementType.JUMPING){
			d = 300;
		} else if(type == MovementType.BIKING){
			if(on.canBike()){
				d /= 2;
			}
			
		} else if(type == MovementType.SURFING){
			d = 160;
		}
		
		double r = difference / d;
		if(r > 1){
			return 1;
		}
		//System.out.println("diff: " + difference + " (final )d: " + d + " r: " + r + "\n\t\t\t\tx: " + this.loc.getX() + " y: " + this.loc.getY());
		return r;
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		this.active.add(new Pokemon("Pinky", PType.getType(3), Stat.random(OutOf.IV), this, -1, -1, 5, Arrays.asList(AttackList.SELFDESTRUCT, AttackList.SOLARBEAM, AttackList.HYPERBEAM, AttackList.SANDATTACK)));
		
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public Screen getNext() {
		return null;
	}
	
	@Override
	public List<Pokemon> getActive() {
		return this.active;
	}
	
	
}