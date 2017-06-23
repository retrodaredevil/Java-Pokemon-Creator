package me.retrodaredevil.pokecreator.trainer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import me.retrodaredevil.pokecreator.map.Block;
import me.retrodaredevil.pokecreator.map.Material;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.TurnPopup;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.TurnPopup.Hover;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.resources.SoundType;
import me.retrodaredevil.pokecreator.trainer.ClientTrainer;
import me.retrodaredevil.pokecreator.trainer.Location;
import me.retrodaredevil.pokecreator.trainer.animation.PlayerAnimation.MovementType;
import me.retrodaredevil.pokecreator.trainer.client.text.TextDialog.Bubble;
import me.retrodaredevil.pokecreator.util.Pending;
import me.retrodaredevil.pokecreator.util.Pending.SetPending;

/**
 * @author retro
 *
 */
public class ClientController extends Controller {

	private ClientTrainer cTrainer;
	
	private SetPending<Turn> pending = null;
	
	public ClientController(ClientTrainer trainer) {
		super(trainer);
		this.cTrainer = trainer;
	}
	public Pending<Turn> createPending(){
		return this.pending = new SetPending<>();
	}

	@Override
	public Direction run(GameContainer c, int delta) {
		Input i = c.getInput();
		
		for(Button b : Button.values()){
			b.update(i);
		}
		Direction d = DButton.getPlayerDirection();
		statement : if(cTrainer.isInBattle()){
			Battle battle = cTrainer.getBattle();
			
			BattleAnimation an = battle.getBattleAnimation();
			if(an == null){
				break statement;
			}
			TurnPopup turnPop = an.getTurnPopup();
			if(turnPop != null){
				//
				//long start = System.currentTimeMillis();
				if(d != Direction.STILL){
					Hover hover = turnPop.getHover();
					int number = hover.getNumber();
					if(d == Direction.DOWN && number <= 1){
						number+=2;
					} else if(d == Direction.UP && number >= 2){
						number-= 2;
					} else {
						if(d == Direction.LEFT && number % 2 == 1){
							number -= 1;
						} else if(d == Direction.RIGHT && number % 2 == 0){
							number += 1;
						}
					}
					if(number > 3){
						number = 3;
					} else if(number < 0){
						number = 0;
					}
					turnPop.setHover(Hover.getHover(number));
					
				
				}
				if(Button.A.isPressed("ClientController-turn-a")){
					Turn select = turnPop.select();
					if(select != null && this.pending != null){
						
						//BBatteler b = battle.getBBatteler(cTrainer);
						//System.out.println("Setting the answer to: " + select.getName() + " debug in ClientController");
						this.pending.setAnswer(select);
						this.pending = null; // we don't need this any more and we don't want to keep setting the answer again and again.
					}
				}
				if(Button.B.isPressed("ClientController-turn-b")){
					turnPop.goBack();
				}
				//System.out.println("diff: " + (System.currentTimeMillis() - start)); // 0
				
			}
			
		} else {
			doMovement(i, d);
		}
		
		return d;
		
	} // 31 x 


	private void doMovement(Input i, Direction d) {
		Location loc = cTrainer.getLocation();
		
		boolean aPressed = Button.A.isPressed("ClientController-run-a");
		int size = cTrainer.getPopupList().getAllValues().size();
		
		if(size == 0){
			this.setLookingDirection(d, Button.B.wasDown(), aPressed);
		}
		
		double p = cTrainer.getPercentageToNextBlock();
		if(p >= 1){
			this.setCurrentBlock(loc.getTarget());
			
		}
		
		if(aPressed && size == 0){
			Block front = loc.getCurrentBlock().getAdd(loc.getLooking());
			if(front != null && front.getMaterial() == Material.SIGN){
				this.cTrainer.sendText(Arrays.asList(front.getText().split("<.>")), Bubble.GREY);
			}
		}
		
		if(i.isKeyDown(Input.KEY_LCONTROL) && i.isKeyDown(Input.KEY_I)){
			System.out.println("on: " + loc.getCurrentBlock().toString());
		}
	}
	
	private boolean didMove = false;
	
	private void setLookingDirection(final Direction d, boolean run, boolean selectPressed){
		Location loc = cTrainer.getLocation();
		Direction moving = loc.getMoving();
		final Block on = loc.getCurrentBlock();
		final Block target = loc.getTarget();
		
		final long time = System.currentTimeMillis();
		if(d != Direction.STILL && moving == Direction.STILL){ // we are still and we want to not be still
			final Direction looking = loc.getLooking();
			
			
			if(!looking.equals(d)){
				loc.setTimeLookedNow();
				loc.setLooking(d);
				//System.out.println("Setting look now.");
			}
			final long timeLooked = loc.getTimeLooked();
			
			if(timeLooked  + 100 < time || didMove){
				loc.setMoving(d);
				loc.setLooking(d);
				moving = d;
			}
			//System.out.println("moving: " + moving);
			
			
			
		}
		didMove = moving != Direction.STILL;
		
		Direction push = on.pushTo();
		if(push != Direction.STILL){
			loc.setMoving(push);
			moving = push;
			
			loc.setLooking(push);
			loc.setMovementType(MovementType.JUMPING);
			
			
		}
		if(loc.getMoving() == Direction.STILL){
			loc.setLooking(loc.getLooking().getNormal());
			loc.setMovementType(MovementType.WALKING);
		}
		
		
		final Block b;

		{
			Block front = on.getAdd(loc.getLooking());
			if(front != null && front.getMaterial() == Material.WATER && on.getMaterial() != Material.WATER && loc.getMovementType() != MovementType.SURFING){
				b = front;
				loc.setMoving(loc.getLooking());
			} else {
				b = on.getAdd(moving);
			}
		}
		
		if(b != null && on != b && on == target){
			boolean clear = false;
			if((b.getMaterial() == Material.WATER && !b.isBlocked()) && ((cTrainer.canSurf() && selectPressed) || loc.getMovementType() == MovementType.SURFING)){
				clear = true;
			}
			if(target != b){
				boolean comeFrom = b.canComeFrom(moving, loc.getMovementType());
				boolean canLeave = on.canLeaveTo(moving);
				
				//System.out.println("comeFrome: " + comeFrom + " canLeave: " + canLeave);
				if((comeFrom || clear) && canLeave){
					loc.setTargetBlock(b);
					loc.setTimeMovedNow();
					loc.setRunning(run);
					if(b.pushTo() != Direction.STILL && b.getMaterial() != Material.WATER){
						loc.setMovementType(MovementType.JUMPING);
						this.cTrainer.playSound(SoundType.SOUND_LEDGE_JUMP);
					} else if(b.getMaterial() != Material.WATER){
						loc.setMovementType(MovementType.WALKING);
					}
					

					
					if(loc.getMovementType() == MovementType.WALKING || loc.getMovementType() == MovementType.RUNNING){
						if(run){
							loc.setMovementType(MovementType.RUNNING); // if surfing, we'll want there to be a way to go fast so we don't want them running while surfing
					
						} else {
							loc.setMovementType(MovementType.WALKING);
						}
					}
					//System.out.println("setting target. on: " +on.toString() + " target: " + target.toString() + " moving: " + moving.getDataString());
					//System.out.println("here");
					
				} else {
					loc.setMoving(Direction.STILL);
					//target = on; // ^ has conditional on == target
				}
			} else {
				// the trainer is currently moving and already has a target block
				
			}

		} else { // working
//			moving = Direction.STILL;
//			target = on;
		}
		if(target == on){ // stops players from looking like they are in a block that is blocked
			loc.setMoving(Direction.STILL);
		}
		if(on.getMaterial() == Material.WATER && target.getMaterial() == Material.WATER){
			loc.setMovementType(MovementType.SURFING);
		} else if(loc.getMovementType() == MovementType.SURFING){
			loc.setMovementType(MovementType.WALKING);
		}

//		if(on.getMaterial() == Material.WATER){
//			System.out.println("Gameover");
//			System.exit(0);
//		}
		
	}

	private void setCurrentBlock(Block b){
		Location loc = cTrainer.getLocation();
		final Block on = loc.getCurrentBlock();
		if(on == b){
			return;
		}
		cTrainer.onBlockChange(on, b);
		loc.setCurrentBlock(b);
		loc.setTargetBlock(b);
		loc.setMoving(Direction.STILL);// if this line isn't here the player will move in one direction for ever
		//System.out.println("got to on: x: " +on.getX() + " y: " + on.getY()  );
		//final long arrived = System.currentTimeMillis();
		
	}
	

	public static class Button{
		
		private static final List<Button> values = new ArrayList<>(); // this needs to be first to initialize
		
		public static final Button B = new Button(new int [] {Input.KEY_LCONTROL, Input.KEY_RCONTROL, Input.KEY_Z, Input.MOUSE_RIGHT_BUTTON});
		public static final Button FISH = new Button(new int[] {Input.KEY_LSHIFT, Input.KEY_RSHIFT, Input.MOUSE_MIDDLE_BUTTON});
		public static final Button A = new Button(new int[] {Input.MOUSE_LEFT_BUTTON, Input.KEY_SPACE, Input.KEY_NUMPADENTER});
		public static final Button START = new Button(new int[] {Input.KEY_ENTER, Input.KEY_NUMPADENTER});

		
		private final int[] def;
		private int[] current;
		
		private Map<String, Boolean> wasDownMap = new HashMap<>(); // for the isPressed(String def) method
		
		private boolean wasDown = false;
		private long lastPress = 0;
		private long lastRelease = 0;
		
		
		private Button(int[] button){
			this.def =button;
			this.current = def;
			System.out.println("here");
			values.add(this);
		}
		
		@Deprecated
		private boolean isDown(Input i){
			for(int b : this.current){
				if(i.isKeyDown(b)){
					return true;
				}else {
					try {
						if(i.isMouseButtonDown(b)){
							return true;
						}
					} catch(ArrayIndexOutOfBoundsException e){
						
					}
				}
			}
			return false;
			
		}
		public boolean wasDown(){
			return this.wasDown;
		}
		
		/**
		 * 
		 * @param def the string to define where the method is being called from. To prevent multiple parts of code from using the same string, don't use things like "normal" and use class names etc.
		 * @return
		 */
		public boolean isPressed(String def){
			boolean down = this.wasDown();
			Boolean wasDown1 = this.wasDownMap.get(def);
			boolean wasDown;
			if(wasDown1 == null){
				wasDown = false;
			} else {
				wasDown = wasDown1;
			}
			wasDownMap.put(def, down);
			return down && !wasDown;
		}
		
		
		public boolean isPressed(Input i){
			for(int b : this.current){
				if(i.isKeyPressed(b)){
					return true;
				} else {
					try {
						if(i.isMousePressed(b)){
							return true;
						}
					} catch(ArrayIndexOutOfBoundsException e){
						
					}
				}
			}
			return false;
		}
		protected long getLastPressed(){
			return this.lastPress;
		}
		protected long getLastRelease(){
			return this.lastRelease;
		}
		
		private void update(Input i){
			long time = System.currentTimeMillis();
			
			boolean down = this.isDown(i);
			
			if(!wasDown && down){
				lastPress = time;
			} else if(wasDown && !down){
				lastRelease = time;
			}
			wasDown = down;
			
			
		}
		
		
		public static List<Button> values(){
			return values;
		}
	}
	
	
	public static class DButton extends Button{
		public static DButton UP = new DButton(new int[]{Input.KEY_W, Input.KEY_UP}, Direction.UP);
		
		public static DButton DOWN = new DButton(new int[] {Input.KEY_S, Input.KEY_DOWN}, Direction.DOWN);
		public static DButton LEFT = new DButton(new int[] {Input.KEY_A, Input.KEY_LEFT}, Direction.LEFT);
		public static DButton RIGHT = new DButton(new int[]{Input.KEY_D, Input.KEY_RIGHT}, Direction.RIGHT);
		
		
		private Direction d;
		
		private DButton(int[] button, Direction d){
			super(button);
			this.d = d;
		}
		
		
		
		public Direction getDirection(){
			return d;
		}
		
		public static Direction getPlayerDirection(){
			List<Direction> no = new ArrayList<>();
			
			int amount = 0;
			
			boolean up = UP.wasDown();
			boolean down = DOWN.wasDown();
			boolean left = LEFT.wasDown();
			boolean right = RIGHT.wasDown();
			
			if(up){
				amount++;
			}
			if(down){
				amount++;
			}
			if(left){
				amount++;
			}
			if(right){
				amount++;
			}
			
			
			if(amount == 1){
				if(up){
					return Direction.UP;
				} else if(down){
					return Direction.DOWN;
				} else if(left){
					return Direction.LEFT;
				} else if(right){
					return Direction.RIGHT;
				}
			}
			
			if(up && down){
				no.add(Direction.DOWN);
				no.add(Direction.UP);
			}
			if(left && right){
				no.add(Direction.LEFT);
				no.add(Direction.RIGHT);
			}
			if(no.size() == 4 || amount == 0){
				return Direction.STILL;
			}
			if(no.size() == 2){
				if(no.contains(Direction.DOWN)){
					return (left ? Direction.LEFT : Direction.RIGHT);
				} else {
					return (down ? Direction.DOWN : Direction.UP);
				}
			}
			if((up || down) && (left || right)){
				long y;
				long x;
				if(up){
					y = UP.getLastPressed();
				} else {
					y = DOWN.getLastPressed();
				}
				if(left){
					x = LEFT.getLastPressed();
				} else {
					x = RIGHT.getLastPressed();
				}
				if(y > x){
					if(up){
						return Direction.UP;
					} else {
						return Direction.DOWN;
					}
				} else {
					if(left){
						return Direction.LEFT;
					}else{
						return Direction.RIGHT;
					}
				}
			}
			return Direction.STILL;
			
		}
		
		
	}
	

}
