package jdc.kings.view;

import java.awt.image.BufferedImage;

/**
 * 
 * @author The Java Hub, ForeignGuyMike
 * {@link https://www.youtube.com/watch?v=zRi0vzQbuqY}
 * {@link https://www.youtube.com/watch?v=zUOkojY_Ylo}
 *
 */
public class Animator {
	
	private BufferedImage[] frames;
	private int currentFrame;
	
	private boolean playedOnce;
	private volatile boolean running = false;
	
	private long previousTime, speed;
	
	public Animator(BufferedImage[] frames) {
		this.frames = frames;
		this.playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		previousTime = 0;
		playedOnce = false;
	}
	
	public void update(long time) {
		if (running) {
			if (time - previousTime >= speed) {
				currentFrame++;
				previousTime = time;
			}
			if (currentFrame == frames.length) {
				currentFrame = 0;
				playedOnce = true;
			}
		}
	}
	
	public void start() {
		running = true;
		previousTime = 0;
		currentFrame = 0;
	}
	
	public void stop() {
		running = false;
		previousTime = 0;
		currentFrame = 0;
	}
	
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	public int getFrame() {
		return currentFrame;
	}
	
	public BufferedImage getImage() {
		return frames[currentFrame];
	}
	
	public boolean hasPlayedOnce() {
		return playedOnce;
	}

	public boolean isRunning() {
		return running;
	}

}
