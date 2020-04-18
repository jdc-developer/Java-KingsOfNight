package jdc.kings.view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import jdc.kings.utils.Constants;

public class Background {
	
	private Image image;
	
	private float x;
	private float y;
	private float velX;
	private float velY;
	
	private float moveScale;
	
	public Background(String s, float ms) {
		try {
			image = new ImageIcon(getClass().getResource(s)).getImage();
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(float x, float y) {
		this.x = (x * moveScale) % Constants.WIDTH;
		this.y = (y * moveScale) % Constants.HEIGHT - 64;
	}
	
	public void setVector(float velX, float velY) {
		this.velX = velX;
		this.velY = velY;
	}
	
	public void update() {
		x += velX;
		y += velY;
	}
	
	public void render(Graphics g) {
		g.drawImage(image, (int)x, (int)y, Constants.WIDTH,
				Constants.HEIGHT, null);
		
		if(x < 0) {
			g.drawImage(
				image,
				(int)x + Constants.WIDTH,
				(int)y,
				Constants.WIDTH,
				Constants.HEIGHT,
				null
			);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x - Constants.SCALE,
				(int)y,
				Constants.WIDTH,
				Constants.HEIGHT,
				null
			);
		}
	}
	
}



