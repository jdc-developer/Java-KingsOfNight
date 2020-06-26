package jdc.kings.utils;

import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	private static AudioPlayer instance;
	private HashMap<String, Clip> clips = new HashMap<>();
	
	private AudioPlayer() {}
	
	public static AudioPlayer getInstance() {
		if (instance == null) {
			instance = new AudioPlayer();
		}
		return instance;
	}
	
	public void loadAudio(String key, String path) {

		Clip clip = clips.get(key);
		if (clip == null) {
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(
						getClass().getResourceAsStream(path));
				AudioFormat baseFormat = ais.getFormat();
				AudioFormat decodeFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(),
						false
					);
				AudioInputStream dais = AudioSystem.getAudioInputStream(
						decodeFormat, ais);
				clip = AudioSystem.getClip();
				clip.open(dais);
				
				clips.put(key, clip);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void play(String key) {
		Clip clip = clips.get(key);
		stop(key);
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void loop(String key) {
		Clip clip = clips.get(key);
		stop(key);
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void reduceSound(String key, float value) {
		Clip clip = clips.get(key);
		FloatControl gainControl = 
			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(value);
	}

	public void stop(String key) {
		Clip clip = clips.get(key);
		if (clip.isRunning()) clip.stop();
	}
	
	public void close(String key) {
		Clip clip = clips.get(key);
		stop(key);
		clip.close();
	}
	
	public void removeClip(String key) {
		clips.remove(key);
	}
	
	public void clear() {
		if (clips.size() > 0) {
			for (String key : clips.keySet()) {
				Clip clip = clips.get(key);
				clip.close();
			}
			clips.clear();
		}
	}

}
