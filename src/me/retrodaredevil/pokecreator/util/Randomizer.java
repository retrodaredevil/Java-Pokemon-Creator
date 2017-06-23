package me.retrodaredevil.pokecreator.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author retro
 *
 */
public class Randomizer {

	static{
		random = new Random();
	}
	
	private static Random random;
	
	public static boolean getTrue(double percent){
		if(percent < 1){
			return percent >= Math.random();
		}
		double random = getRandomDouble(0, 100);
		return random <= percent;
	}
	public static boolean getTrue(int percent){
		double random = getRandomDouble(0, 100);
		return random <= percent;
		
	}
	
	public static double getRandomDouble(double min, double max){
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	public static Random getRandom(){
		return random;
	}
	
}
