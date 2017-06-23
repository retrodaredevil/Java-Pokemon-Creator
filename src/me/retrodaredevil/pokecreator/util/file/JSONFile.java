package me.retrodaredevil.pokecreator.util.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




/**
 * @author retro
 * My own simple implementation of a json file
 */
public class JSONFile implements JSONThing{

	private File file;
	private JSONObject obj;
	
	
	public JSONFile(File jsonFile){
		this.file = jsonFile;
		
		JSONParser parser = new JSONParser();
		
		
		try(FileReader reader = new FileReader(jsonFile)){
			
			obj = (JSONObject) parser.parse(reader);
		} catch(IOException | ParseException e){
			obj = new JSONObject();
		} 
	}
	
	public File getFile(){
		return file;
	}
	
	@Override
	public JSONObject getObject(){
		return obj;
	}
	
	public void save(){
		
		try {
			
			if(!file.exists()){
				file.mkdirs();
				file.createNewFile();
			}
			
			FileWriter writer = new FileWriter(file);
			String s = obj.toJSONString();
			
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
