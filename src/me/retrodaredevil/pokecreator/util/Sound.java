package me.retrodaredevil.pokecreator.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import me.retrodaredevil.pokecreator.resources.SoundType;

/**
 * @author retro
 *
 */
public class Sound {
	
	private static final Mixer mixer;
	
	static{
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		mixer = AudioSystem.getMixer(infos[0]);
		
	}
	
	private List<Play> playing = new ArrayList<>();
	
	private Getter<InputStream> getter;
	
	private SoundType type;
	
	public Sound (Getter<InputStream> getter, SoundType type){
		
		this.getter = getter;
		this.type = type;
	}
	
	public Getter<InputStream> getInputStreamGetter(){
		return this.getter;
	}
	
	public Play play(){
		Play p = new Play(this);
		p.play();
		playing.add(p);
		return p;
	}
	public void stopAll(boolean remove){ // works // doesn't remove things from playing (should be like that cuz someone could be looping through it)

		Iterator<Play> it = this.playing.iterator();
		while(it.hasNext()){
			Play p = it.next();
			p.stop();

			if(remove && !p.isPlaying()){
				it.remove();
			}
		}
	}
	public void update(){
		Iterator<Play> it = this.playing.iterator();
		while(it.hasNext()){
			Play p = it.next();

			if(!p.isPlaying()){
				it.remove();
			} else {
				p.update();
			}
		}
	}
	public boolean isAnyPlaying(){
		for(Play p : playing){
			if(p.isPlaying()){
				return true;
			}
		}
		return false;
	}
	
	public List<Play> getPlaying(){
		return playing;
	}

	public SoundType getType(){
		return this.type;
	}
	
	public static class Play {
		
		private static boolean sent = false;
		
		private Clip c;
		private AudioInputStream as;
		private BufferedInputStream bis;
		
		private boolean paused = false;
		private long started = 0;
		
		private Sound sound;
		
		private InputStream stream;
		
		private long de = 0;
		private boolean quiet = false;
		
		public Play(Sound s){
		
			this.sound = s;
			
			
			stream = sound.getInputStreamGetter().get();
			try {
				
				c = (Clip) mixer.getLine(new DataLine.Info(Clip.class, null));
				bis = new BufferedInputStream(stream);
				as = AudioSystem.getAudioInputStream(bis);
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				//e.printStackTrace();
				if(!sent){
					sent = true;
					e.printStackTrace();
					System.err.println("error with sound: " + s.getType().getName() + ". Note that we will not print any more sound errors to avoid filling up the console there may still be errors.");
				}
			}
			
		}
		
		/**
		 * as long as the update method is being called constantly, this will make the music fade out
		 */
		public void setDecrescendo(){
			this.de = System.currentTimeMillis();
			System.out.println("de is now: " + de);
		}
		public boolean isDecrescendo(){
			return de != 0;
		}
		public void update(){
			if(de == 0){
				return;
			}
			FloatControl control =  (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			
			long diff = System.currentTimeMillis() - de;
			float value = (float) diff / -50;
			if(value < -80){
				value = -80;
				this.quiet = true;
				//this.stop();
			}
			control.setValue(value);
		}
		
		
		public Sound getSound(){
			return sound;
		}
		
		/**
		 * This should be called once. It will start playing the sound
		 */
		private void play(){
			if(SoundType.mute){
				return;
			}
			if(c == null || as == null){
				Logger.log("sound null", "'c' or 'as' are null in Sound.java. returning and not playing sound.");
				return;
			}
			Logger.log("sound play", "playing!!!");
			try {
				c.open(as);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			c.start();
			started = System.currentTimeMillis();
		}
		public boolean isPlaying(){ // works
			if(started == 0){
				return false;
			}
			long time = System.currentTimeMillis();
			
			if((de != 0 && System.currentTimeMillis() - de > 2000) || this.quiet){ // quiet turns true in the update method when the volume get's below 80 (the minimum for turning the volume down)
				return false;
			}
			
			
			return c.isActive() || started + 50 > time;
		}
		public void stop(){ // works
			if(!c.isRunning()){
				return;
			}
			c.close();
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			c.drain();
//			c.flush();
		}
		public void pause(){ // works
			c.stop();
			paused = true;
		}
		public void resume(){ // works
			c.start();
			paused = false;
		}
		
		public boolean isPaused(){ // should work
			return paused;
		}
		
		public Clip getClip(){
			return c;
		}
		
		
	}


	

}
