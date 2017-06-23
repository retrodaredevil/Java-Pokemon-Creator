package me.retrodaredevil.pokecreator.resources;

import java.io.InputStream;

import me.retrodaredevil.pokecreator.util.Getter;
import me.retrodaredevil.pokecreator.util.Sound;
import me.retrodaredevil.pokecreator.util.Sound.Play;

/**
 * @author retro
 *
 */
public enum SoundType {
	BLANK_SOUND("blank_sound"){
		@Override
		public boolean isPlaying() {
			return false;
		}
		@Override
		public Play play(ResourcePack pack) {
			return null;
		}
		@Override
		public void stopAll() {
		}
	},
	MUSIC_LOGO_INTRO("music/intro/logo_intro"),
	MUSIC_TITLE_SCREEN_INTRO("music/intro/title_screen"),
	
	MUSIC_ROUTE_1("music/route/route1"),
	MUSIC_ROUTE_3("music/route/route3"),
	MUSIC_ROUTE_11("music/route/route3"),
	
	
	
	SOUND_LEDGE_JUMP("map/ledge"),
	SOUND_SELECT("random/select")
	
;
	public static boolean mute = false;
	
	private String name;
	private String path;
	
	private Sound sound;
	
	
	
	private SoundType(String path){
		this.name = path.replaceAll("/", ".");
		this.path = "sounds/" + path + ".wav";
	}
	
	public String getName(){
		return name;
	}
	
	public String getPath(){
		return path;
	}
	
	private void createSound(ResourcePack pack){
		sound = new Sound(new Getter<InputStream>() {
			
			@Override
			public InputStream get() {
				return pack.getStream(path);
			}
		}, this);
	}
	public boolean isPlaying(){
		if(sound == null){
			return false;
		}
		return sound.isAnyPlaying();
	}
	public Play play(ResourcePack currentPack){
		if(sound == null){
			this.createSound(currentPack);
			System.out.println("creating sound: " + this.name());
		}
		return sound.play();
	}

	/**
	 * calls {@link Sound#stopAll(boolean)} where remove is true. Don't call this if you are looping through it.
	 */
	public void stopAll() {
		if(sound == null){
			return;
		}
		sound.stopAll(true);
	}
	public static SoundType getSoundTypeFromShortName(String name){
		//System.out.println("name: " + name);
		if(name == null || name.length() == 0){
			return null;
		}
		String[] split = name.split("\\.|/");
		if(split.length == 0){
			return null;
		}
		String compare = split[split.length -1];
		
		//System.out.println("compare: " + compare);
		
		for(SoundType type : values()){
			String[] nsplit = type.getName().split("\\.|/");
			String typeName = nsplit[nsplit.length -1];
			
			
			
			if(typeName.equalsIgnoreCase(compare)){
				return type;
			}
		}
		return null;
		
	}
	public static SoundType getSoundTypeFromFullName(String name){
		if(name == null){
			return null;
		}
		for(SoundType type : values()){
			if(type.getName().equalsIgnoreCase(name)){
				return type;
			}
		}
		return null;
	}
	public static void update(){
		for(SoundType type : values()){
			Sound sound = type.sound;
			if(sound != null){
				sound.update();
			}
		}
	}
	
	
}
