package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;

public class Blood {
	
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	private boolean right;
	
	private Animator animator;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Blood(int x, int y, int width, int height, int bloodIndex, boolean right) {
		this.x = x;
		this.y = y;
		this.right = right;
		
		this.width = width;
		this.height = height;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		
		switch(bloodIndex) {
			case 0:
				sprites = loader.loadActionByRows("/sprites/blood/blood.png", 0, 4, 0, width, height);
				break;
			case 1:
				sprites = loader.loadActionByRows("/sprites/blood/blood.png", 0, 4, 1, width, height);
				break;
			case 2:
				sprites = loader.loadActionByRows("/sprites/blood/blood-explosion.png", 0, 10, 0, width, height);
				break;
			case 3:
				sprites = loader.loadActionByRows("/sprites/blood/blood.png", 0, 4, 3, width, height);
				break;
		}

		animator = new Animator(sprites);
		animator.setSpeed(100);
		animator.start();
		
	}
	
	public void tick() {
		animator.update(System.currentTimeMillis());
		if(animator.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void render(Graphics g) {
		if (!right) {
			g.drawImage(
					animator.getImage(),
					x + xmap - width / 2,
					y + ymap - height / 2,
					null
				);
		} else {
			BufferedImage image = animator.getImage();
			g.drawImage(image,
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2),
					-image.getWidth(),
					height,
					null);
		}
		
	}

}
