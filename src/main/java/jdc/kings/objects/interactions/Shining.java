package jdc.kings.objects.interactions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import jdc.kings.utils.Constants;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;

public class Shining {
	
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private Random r = Constants.RANDOM;
	private int randomX;
	private int randomY;
	private int width;
	private int height;
	
	private Animator animator;
	private boolean remove;
	private long timer;

	public Shining(int width, int height, int plusWidth, int plusHeight) {
		this.width = width;
		this.height = height;
		
		SpriteLoader spriteLoader = SpriteLoader.getInstance();
		if (spriteLoader.getSprites("shining") == null) {
			BufferedImage[][] sprites = new BufferedImage[10][];
			sprites[0] = spriteLoader.loadAction("/sprites/effects/magic/green-shining.png", null, 0, 5, 0, 1, 0, 0, 32, 32, plusWidth, plusHeight);
			
			spriteLoader.loadSprites("shining", sprites);
		}
		
		animator = new Animator(spriteLoader.getAction("shining", 0));
		animator.setSpeed(100);
		animator.start();
	}
	
	public void tick() {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if (elapsed > 500) {
			randomX = r.nextInt(25);
			randomY = r.nextInt(70);
			timer = System.nanoTime();
		}
		
		animator.update(System.currentTimeMillis());
		if(animator.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(
				animator.getImage(),
				x + xmap - width / 2 + randomX,
				y + ymap - height / 2 + randomY,
				null
			);
		
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

}
