package me.retrodaredevil.pokecreator;

import java.io.File;
import java.io.IOException;

import me.retrodaredevil.pokecreator.resources.FileManager;
import me.retrodaredevil.pokecreator.resources.ServerType;
import me.retrodaredevil.pokecreator.util.file.JSONFile;

/**
 * @author retro
 *
 */
public class Manager{

	public static final String PROGRAM_NAME = "Pokemon Creator";
	private static Manager instance;
	
	private MainClass cl;
	private FileManager manager;
	
	private JSONFile dataFile;
	
	
	private Manager(MainClass clazz){
		this.cl = clazz;
		
		

	}
	@SuppressWarnings("unchecked") 
	public synchronized void init() throws IOException { // synchronized makes it so only one thread can run this code at a time
		
		boolean setPath = false;
		File jsonFile = new File("data.json");
		if(jsonFile.exists()){
			dataFile = new JSONFile(jsonFile);
			if(dataFile.getObject().get("path") == null){
				setPath = true;
			}
		} else {
			jsonFile.createNewFile();
			dataFile = new JSONFile(jsonFile);
			setPath = true;
		}
		
		File folder;
		if(setPath){
		
			if(this.useProgramFiles()){
				folder = new File(System.getenv("APPDATA"), PROGRAM_NAME);
				
			} else {
				folder = new File("");
			}
			
			dataFile.getObject().put("path", folder.getAbsolutePath());
			dataFile.save();
		} else {
			folder = new File((String) dataFile.getObject().get("path"));
		} 
		
		manager = new FileManager(folder);
		
	}
	
	
	
	
	
	
	public boolean useProgramFiles(){
		return ServerType.getType().isClient();
	}
	
	public FileManager getManager(){
		return manager;
	}
	public MainClass getMainClass(){
		return cl;
	}
	
	public static void createNew(MainClass clazz){
		if(instance != null){
			System.err.println("Already an instance of Manager. Not creating a new one.");
			return;
		}
		instance = new Manager(clazz);
	}
	public static Manager getInstance(){
		return instance;
	}
	
	
}
