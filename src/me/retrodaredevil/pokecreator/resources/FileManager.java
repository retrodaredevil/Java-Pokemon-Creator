package me.retrodaredevil.pokecreator.resources;

import java.io.File;

/**
 * @author retro
 *
 */
public class FileManager {

	
	
	private File folder;
	
	public FileManager(File folder){
		this.folder = folder;
	}
	
	
	public File getFolder(){
		return folder;
	}
	
	public void choose(){
		
	}
	public void setFolder(File folder){
		this.folder = folder;
	}
	public File getFile(String path){
		return new File(folder, path);
	}
	
	
}
