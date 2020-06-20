package jdc.kings.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.utils.Constants;

public class MapState extends GameState {
	
	private BufferedImage image;
	
	private float x;
	private float y;
	private float velX;
	private float velY;
	
	public MapState() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/backgrounds/map.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		StateManager.getInstance().showLoader(false);
		
		x += velX;
		if (x > 0) {
			x = 0;
		}
		
		if (x + image.getWidth() <= Constants.WIDTH) {
			int value = image.getWidth() - Constants.WIDTH;
			x = -value;
		}
		
		y += velY;
		if (y > 0) {
			y = 0;
		}
		
		if (y + image.getHeight() <= Constants.HEIGHT) {
			int value = image.getHeight() - Constants.HEIGHT;
			y = -value;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, (int)x, (int)y, image.getWidth(), image.getHeight(), null);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UP) {
			velY = 10;
		}
		
		if (key == KeyEvent.VK_DOWN) {
			velY = -10;
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			velX = -10;
		}
		
		if (key == KeyEvent.VK_LEFT) {
			velX = 10;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
			velY = 0;
		}
		
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
			velX = 0;
		}
	}

}
