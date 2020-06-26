package jdc.kings.objects.interactions;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import jdc.kings.objects.GameObject;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class Arrow extends GameObject {

	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	
	public Arrow(TileMap tm, boolean right) {
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 7.8f;
		if (right) velX = moveSpeed;
		else velX = -moveSpeed;
		
		width = 35;
		height = 10;
		cwidth = 15;
		cheight = 7;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites = loader.loadAction("/sprites/enemies/skeleton-archer/arrow.png", this, 0, 1, 0, 1, 0, 0, 150, 24, 0, 0);
		
		animator = new Animator(sprites);
		animator.setSpeed(80);
		animator.start();
	}
	
	public void setHit() {
		if (hit) return;
		hit = true;
		velX = 0;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void tick() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if (velX == 0 && !hit) {
			setHit();
		}
		
		animator.update(System.currentTimeMillis());
		if (hit && animator.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}

}
