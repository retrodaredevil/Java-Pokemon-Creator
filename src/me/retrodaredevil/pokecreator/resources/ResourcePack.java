package me.retrodaredevil.pokecreator.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import me.retrodaredevil.pokecreator.util.file.SimpleZipFile;

/**
 * @author retro
 * 
 */
public class ResourcePack {

	/**
	 * A basic Resource Pack that correspondes to the folder "resources" in the same folder as the jar file.
	 */
	public static final ResourcePack BASIC = new ResourcePack("resources"); //new ResourcePack(new File("resources"));
	
	
	private File folder;
	private SimpleZipFile zip;
	
	private Font font;
	private Font boldFont;
	private Map<Float, UnicodeFont> map = new HashMap<>(); 
	private Map<Float, UnicodeFont> boldMap = new HashMap<>();
	
	/**
	 * 
	 * @param file the file that holds the textures and the sounds and etc. folders 
	 */
	public ResourcePack(File file){
		this.folder = file;
		

		this.createFonts();
		
	}
	public ResourcePack (String path){
		File file = new File(path);
		if(file.isDirectory()){
			this.folder = file;
		} else {
			this.zip = new SimpleZipFile(file);
		}
		this.createFonts();
	}
	
	public ResourcePack(SimpleZipFile file){
		this.zip = file;
		
		this.createFonts();
	}
	
	private void createFonts(){
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, this.getStream("textures/text/font.ttf"));
			
			boldFont = new Font("Bold: " + font.getFontName(), Font.BOLD, font.getSize());
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isFolder(){
		return folder != null;
	}
	public boolean isZipFile(){
		return this.zip != null;
	}
	
	public SimpleZipFile getZipFile(){
		return this.zip;
	}
	public File getFolder(){
		return folder;
	}
	public InputStream getStream(String path){
		if(zip == null){
			File file = new File(folder, path);
			if(!file.exists() && this != BASIC){
				return BASIC.getStream(path);
			}
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				return null;
			}
		}
		
		try {
			return this.zip.getStream(zip.getZipEntry(path));
		} catch (IOException e) {
			//e.printStackTrace();
			
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public UnicodeFont getFont(float size, boolean bold){
		Map<Float, UnicodeFont> map = this.map;
		if(bold){
			map = this.boldMap;
		}
		UnicodeFont r = map.get(size);
		
		if(r != null){
			return r;
		}
		Font font = this.font;
		if(bold){
			font = this.boldFont;
		}
		r = new UnicodeFont(font.deriveFont(size));
		r.getEffects().add(new ColorEffect(Color.white));
		
		r.addAsciiGlyphs();
		try {
			r.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		map.put(size, r);
		return r;
		
	}
	public UnicodeFont getFont(float size){
		return this.getFont(size, false);
	}
	
	
	
	
	
	
}
