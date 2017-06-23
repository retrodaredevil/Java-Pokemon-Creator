package me.retrodaredevil.pokecreator.util;

/**
 * 
 * @author retro
 *
 * this class can be used to wait for an answer without doing a while(true) loop to wait (Checking when a method is called)
 *
 * @param <T> the type to be returned and the thing you are waiting for an answer on
 */
/**
 * @author retro
 *
 * @param <T>
 */
public interface Pending<T> {

	
	public default boolean hasAnswer(){
		return this.getAnswer() != null;
	}
	public T getAnswer();
	
	
	
	public static class SetPending<T> implements Pending<T>{

		private T answer;
		

		@Override
		public T getAnswer() {
			return answer;
		}
		
		public void setAnswer(T answer){
			this.answer = answer;
		}
		
	}
	
	
}
