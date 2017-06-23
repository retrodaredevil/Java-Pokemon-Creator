package me.retrodaredevil.pokecreator.trainer.client.text;

import java.util.List;

import me.retrodaredevil.pokecreator.Screen;

/**
 * @author retro
 *
 */
public interface TextPopup extends Screen{

	
//	protected TextPopup(int size){
//		font = new TrueTypeFont(new Font("TextPopup Front Size " + size, Font.PLAIN, size), true);
//	}
	
	@Override
	public TextPopup getNext();
	
	
	public List<String> getLines();	
	

	
	
	public static enum Speed {
		SLOW(100),
		NORMAL(50),
		FAST(20);
		
		private long wait;
		
		private Speed (long timePerChar){
			this.wait = timePerChar;
		}
		
		
		
		public long getTimePerChat(){
			return wait;
		}
		
	}
	
}
