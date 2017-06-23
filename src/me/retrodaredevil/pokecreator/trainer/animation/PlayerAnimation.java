package me.retrodaredevil.pokecreator.trainer.animation;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.trainer.controller.Direction;

/**
 * @author retro
 *
 */
public class PlayerAnimation {

	
	private SpriteSheet sheet;
	private final int tilesAccross = 15;
	//private final int tilesDown = 8;
	
	public void update(ResourcePack pack){
		try {
			//sheet = new SpriteSheet(pack.getStream("textures/player/player.png").getAbsolutePath(), 128, 128);
			
			Image image = new Image(pack.getStream("textures/player/player.png"), "textures/player/player.png", false);
			
			int amount = image.getWidth() / 15;
			
			sheet = new SpriteSheet(image, amount, amount);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
		if(sheet == null){
			throw new Error("Didn't find the sheet texture");
		}
		
		for(Phase p : Phase.values()){
			if(p.getLast() != pack){
				p.updateImage(sheet, pack);
			}
		}
	}
	
	
	
	public Phase getPhase(MovementType type, Direction d, long frameDelay){
//		long time = System.currentTimeMillis();
		
		for(Phase p : Phase.values()){
//			int n = p.getFrame() - 1;
//			int max = p.getTotalFrames();
			MovementType move = p.getMovementType();
			if(!(p.d == d && type == move)){
				continue;
			}
			
			if(move.getFrameDelay() == 0){
				return p;
			}
			long timeTotal = frameDelay * p.getTotalFrames(); // total time it takes to get through all frames
			final long time = System.currentTimeMillis(); // current time
			
			long a = time % timeTotal;
			long b = frameDelay * p.getFrame();
			if(a < b){ // (0 - timeTotal) < (framedelay) * (1 - 3)
				//System.out.println("a: " + a + " b: " + b);
				return p;
			}
			
//			if(max == 1){
//				return p;
//			}
//			long b = 0;
//			int a = 0;
//			while(b < time){
//				b += msSwitch;
//				a++;
//			}
//			while(a >= max){
//				a-= max;
//			}
//			if(n == a){
//				return p;
//			}
		}
		//System.out.println("type: " + type.toString() + "\tdirection: " + d.toString());
		return null;
	}
	
	
	public static enum Phase {
		STANDING_DOWN(Direction.DOWN, MovementType.STANDING, 1, 	1, 0, false, 1),
		STANDING_UP(Direction.UP, MovementType.STANDING, 1, 	1, 1, false, 1),
		STANDING_LEFT(Direction.LEFT, MovementType.STANDING, 1, 	1, 2, false, 1),
		STANDING_RIGHT(STANDING_LEFT, true, 1, 1, Direction.RIGHT, MovementType.STANDING),
		
		
		WALKING_DOWN_0(Direction.DOWN, MovementType.WALKING, 1, 	0, 0, false, 4),
		WALKING_DOWN_1(STANDING_DOWN, false, 2, 4, Direction.DOWN, MovementType.WALKING),
		WALKING_DOWN_2(Direction.DOWN, MovementType.WALKING, 3, 	2, 0, false, 4),
		WALKING_DOWN_3(WALKING_DOWN_1, 4, 4),
		
		WALKING_UP_0(Direction.UP, MovementType.WALKING, 1, 	0, 0 + 1, false, 4),
		WALKING_UP_1(STANDING_UP, false, 2, 4, Direction.UP, MovementType.WALKING),
		WALKING_UP_2(Direction.UP, MovementType.WALKING, 3, 	2, 0 + 1, false, 4),
		WALKING_UP_3(WALKING_UP_1, 4, 4),
		
		WALKING_LEFT_0(Direction.LEFT, MovementType.WALKING, 1, 	0, 0 + 2, false, 4),
		WALKING_LEFT_1(STANDING_LEFT, false, 2, 4, Direction.LEFT, MovementType.WALKING),
		WALKING_LEFT_2(Direction.LEFT, MovementType.WALKING, 3, 	2, 0 + 2, false, 4),
		WALKING_LEFT_3(WALKING_LEFT_1, 4, 4),

		WALKING_RIGHT_0(WALKING_LEFT_0, true, 1, 4, Direction.RIGHT, MovementType.WALKING),
		WALKING_RIGHT_1(WALKING_LEFT_1, true, 2, 4, Direction.RIGHT, MovementType.WALKING),
		WALKING_RIGHT_2(WALKING_LEFT_2, true, 3, 4, Direction.RIGHT, MovementType.WALKING),
		WALKING_RIGHT_3(WALKING_RIGHT_1, 4, 4),
		
		
		RUNNING_DOWN_0(Direction.DOWN, MovementType.RUNNING, 1, 	0 + 3, 0, false, 4),
		RUNNING_DOWN_1(Direction.DOWN, MovementType.RUNNING, 2, 	1 + 3, 0, false, 4),
		RUNNING_DOWN_2(Direction.DOWN, MovementType.RUNNING, 3, 	2 + 3, 0, false, 4),
		RUNNING_DOWN_3(RUNNING_DOWN_1, 4, 4),
		
		RUNNING_UP_0(Direction.UP, MovementType.RUNNING, 1, 	0 + 3, 0 + 1, false, 4),
		RUNNING_UP_1(Direction.UP, MovementType.RUNNING, 2, 	1 + 3, 0 + 1, false, 4),
		RUNNING_UP_2(Direction.UP, MovementType.RUNNING, 3, 	2 + 3, 0 + 1, false, 4),
		RUNNING_UP_3(RUNNING_UP_1, 4, 4),
		
		RUNNING_LEFT_0(Direction.LEFT, MovementType.RUNNING, 1, 	0 + 3, 0 + 2, false, 4),
		RUNNING_LEFT_1(Direction.LEFT, MovementType.RUNNING, 2, 	1 + 3, 0 + 2, false, 4),
		RUNNING_LEFT_2(Direction.LEFT, MovementType.RUNNING, 3, 	2 + 3, 0 + 2, false, 4),
		RUNNING_LEFT_3(RUNNING_LEFT_1, 4, 4),

		RUNNING_RIGHT_0(RUNNING_LEFT_0, true, 1, 4, Direction.RIGHT, MovementType.RUNNING),
		RUNNING_RIGHT_1(RUNNING_LEFT_1, true, 2, 4, Direction.RIGHT, MovementType.RUNNING),
		RUNNING_RIGHT_2(RUNNING_LEFT_2, true, 3, 4, Direction.RIGHT, MovementType.RUNNING),
		RUNNING_RIGHT_3(RUNNING_RIGHT_1, 4, 4), 
		
		
		SURFING_DOWN_0(Direction.DOWN, MovementType.SURFING, 1, 	0 + 13, 0, false, 2),
		SURFING_DOWN_1(Direction.DOWN, MovementType.SURFING, 2, 	1 + 13, 0, false, 2),
		
		SURFING_UP_0(Direction.UP, MovementType.SURFING, 1, 	0 + 13, 0 + 1, false, 2),
		SURFING_UP_1(Direction.UP, MovementType.SURFING, 2, 	1 + 13, 0 + 1, false, 2),
		
		SURFING_LEFT_0(Direction.LEFT, MovementType.SURFING, 1, 	0 + 13, 0 + 2, false, 2),
		SURFING_LEFT_1(Direction.LEFT, MovementType.SURFING, 2, 	1 + 13, 0 + 2, false, 2),

		SURFING_RIGHT_0(SURFING_LEFT_0, true, 1, 2, Direction.RIGHT, MovementType.SURFING),
		SURFING_RIGHT_1(SURFING_LEFT_1, true, 2, 2, Direction.RIGHT, MovementType.SURFING),
		
		
		SHADOW(Direction.STILL, MovementType.JUMPING, 1, 14, 3, false, 1)
		
		
		;
		private Direction d;
		private MovementType type;
		private int frame;
		
		private int totalFrames;
		
		private int x;
		private int y;
		private boolean flipped;
		
		private Image image;
		private Image gimage;
		
		private ResourcePack lastPack = null;
		
		/**
		 * 
		 * @param d the direction
		 * @param t the movement type ex running walking etc.
		 * @param frame the number frame starts at 1 not 0
		 * @param x the x value on the sprite sheet starts at 0 like a 2d java array
		 * @param y the y value on the sprite sheet starts at 0 like a 2d java array
		 * @param flip True if you want to flip the image horizontally (Like being mirrored over the y axis)
		 * @param totalFrames the total number of frames in a movement with a direction ex: If a running left animation has 3 frames this would be 3 - Most values are either 1 or 3
		 */
		private Phase(Direction d, MovementType t, int frame, int x, int y, boolean flip, int totalFrames){// doesn't call updateImage because it could be a server
			this.d = d;
			this.type = t;
			this.frame = frame;
			
			this.x = x;
			this.y = y;
			this.flipped = flip;
			
			this.totalFrames = totalFrames;
			
		}
		private Phase(Phase parent, boolean flip, int frame, int totalFrames, Direction d, MovementType type){
			this(parent.d, type, frame, parent.x, parent.y, flip, totalFrames);
			this.d = d;
		}
		private Phase(Phase parent, int frame, int totalFrames){
			this(parent.d, parent.type, frame, parent.x, parent.y, parent.flipped, totalFrames);
		}

		private void updateImage(SpriteSheet sheet, boolean girl, ResourcePack pack){
			int g = 0;
			if(girl){
				g+=4;
			}
			Image set = sheet.getSprite(x, y + g).getFlippedCopy(flipped, false);
			set.setFilter(Image.FILTER_NEAREST);
			if(girl){
				gimage = set;
			} else {
				image = set;
			}
		}
		public void updateImage(SpriteSheet sheet, ResourcePack pack){
			this.lastPack = pack;
			this.updateImage(sheet, false, pack);
			this.updateImage(sheet, true, pack);
		}
		private ResourcePack getLast(){
			return lastPack;
		}
		
		public int getFrame(){
			return frame;
		}
		public int getTotalFrames(){
			return this.totalFrames;
		}
		public MovementType getMovementType(){
			return this.type;
		}
		public Direction getDirection(){
			return d;
		}
		
		public Image getImage(boolean girl){
			if(girl){
				return gimage;
			}
			return image;
		}
		
		
	}
	public static enum MovementType {
		STANDING(0), WALKING(170), RUNNING(100), FISHING, BIKING, SENT_OUT, FLASH, SURFING(800), JUMPING;
		
		private long frameDelay;
		
		private MovementType(){
			this(150);
		}
		
		private MovementType(long frameDelay){
			this.frameDelay = frameDelay;
		}
		
		public long getFrameDelay(){
			return frameDelay;
		}
		
	}
	
	
}
