package me.retrodaredevil.pokecreator.util.file;

import java.io.InputStream;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author retro
 *
 */
public class JSONReader implements JSONThing{

	private JSONObject obj;
	
	@SuppressWarnings("resource")
	public JSONReader(InputStream stream){
		JSONParser parser = new JSONParser();
		
		Scanner s = new Scanner(stream).useDelimiter("\\A");
		String contents = s.hasNext() ? s.next() : "";
		try {
			obj = (JSONObject) parser.parse(contents);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public JSONObject getObject() {
		return obj;
	}

}
