package jdc.kings.view;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 
 * @author The Java Hub
 * {https://www.youtube.com/watch?v=zRi0vzQbuqY}
 *
 */
public class Animator {
	
	private List<BufferedImage> frames;
	private BufferedImage sprite;
	
	private volatile boolean running = false;
	
	private long previousTime, speed;
	private int frameAtPause, currentFrame;
	
	public Animator(List<BufferedImage> frames) {
		this.frames = frames;
	}
	
	public void update(long time) {
		if (running) {
			if (time - previousTime >= speed) {
				currentFrame++;
				try {
					sprite = frames.get(currentFrame);
				} catch (IndexOutOfBoundsException e) {
					currentFrame = 0;
					sprite = frames.get(currentFrame);
				}
				
				previousTime = time;
			}
		}
	}
	
	public void start() {
		running = true;
		previousTime = 0;
		frameAtPause = 0;
		currentFrame = 0;
	}
	
	public void stop() {
		running = false;
		previousTime = 0;
		frameAtPause = 0;
		currentFrame = 0;
	}
	
	public void pause() {
		frameAtPause = currentFrame;
		running = false;
	}
	
	public void resume() {
		currentFrame = frameAtPause;
		running = true;
	}
	
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

}
