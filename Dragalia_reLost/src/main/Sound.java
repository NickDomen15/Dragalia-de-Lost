package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip; // used to open audio files
	URL soundURL[] = new URL[30]; // stores the filepaths of sound files
	
	// get sound files and store in array
	public Sound() {
		
		soundURL[0] = getClass().getResource("/sounds/BlueBoyAdventure.wav");
		soundURL[1] = getClass().getResource("/sounds/coin.wav");
		soundURL[2] = getClass().getResource("/sounds/powerup.wav");
		soundURL[3] = getClass().getResource("/sounds/unlock.wav");
		soundURL[4] = getClass().getResource("/sounds/fanfare.wav");
		
	}
	
	
	/**
	 * Gets the sound file from the 
	 * array and sets it to the clip
	 * @param i
	 */
	public void setFile(int i) {
		
		try {
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
		}
		catch (Exception e) {
			
		}
		
	}
	
	
	// starts the current clip
	public void play() {
		
		clip.start();
		
	}
	
	
	// loops the current clip
	public void loop() {
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	
	
	// stops the current clip
	public void stop() {
		
		clip.stop();
		
	}
	
}
