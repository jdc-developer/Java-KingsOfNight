package jdc.kings.objects.interactions;

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
				sprites = loader.loadAction("/sprites/blood/blood.png", null, 0, 4, 0, 1, 0, 0, width, height, 0, 0);
				break;
			case 1:
				sprites = loader.loadAction("/sprites/blood/blood.png", null, 0, 4, 1, 2, 0, 0, width, height, 0, 0);
				break;
			case 2:
				sprites = loader.loadAction("/sprites/blood/blood-explosion.png", null, 0, 10, 0, 1, 0, 0, width, height, 0, 0);
				break;
			case 3:
				sprites = loader.loadAction("/sprites/blood/blood.png", null, 0, 4, 2, 3, 0, 0, width, height, 0, 0);
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
