package me.retrodaredevil.pokecreator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author retro
 * A simple way to log something once.
 */
public class Logger {

	private static List<String> logged = new ArrayList<>();
	
	public static void log(String name, String print){
		if(logged.contains(name)){
			return;
		}
		System.out.println(print);
		logged.add(name);
	}
	
	
}
