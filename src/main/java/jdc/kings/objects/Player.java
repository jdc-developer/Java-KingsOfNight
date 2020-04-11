package jdc.kings.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;

public class Player extends GameObject {
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;

	public Player(int x, int y) {
		super(x, y);
		width = 63;
		height = 74;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/player/idle.png", this, 15, 22, 38, 26, 30));
		sprites.add(loader.loadAction("/player/walking.png", this, 8, 40, 66, 30, 30));
		
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(100);
		animator.start();
	}

	public void tick() {
		x += velX;
		y += velY;
		
		if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(100);
				width = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(100);
				width = 30;
			}
		}
		if (right) facingRight = true;
		if (left) facingRight = false;
	}

}
