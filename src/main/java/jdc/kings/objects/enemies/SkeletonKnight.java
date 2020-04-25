package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonKnight extends Enemy {
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;

	public SkeletonKnight(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 5f;
		maxSpeed = 6f;
		fallSpeed = 0.2f;
		maxFallSpeed = 10.0f;
		jumpStart = -4.8f;
		stopJumpSpeed = 1.3f;
		flinchYSpeed = 2.5f;
		flinchXSpeed = 2f;
		maxFlinchXSpeed = 3.5f;
		
		width = 60;
		height = 110;
		cwidth = 35;
		cheight = 105;
		
		health = maxHealth = 20;
		damage = 7;
		
		shieldDamage = 1;
		shieldCost = 4;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 322, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 325, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/cutting.png", this, 0, 5, 0, 2, 0, 0, 246, 265, 30, 0));
	
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(120);
		animator.start();
	}
	
	private void getNextPosition() {
		if (left) {
			velX -= moveSpeed;
			if (velX < -maxSpeed) {
				velX = -maxSpeed;
			}
		} else if (right) {
			velX += moveSpeed;
			if (velX > maxSpeed) {
				velX = maxSpeed;
			}
		}
		
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (falling) {
			velY += fallSpeed;
			
			if (velY > 0) jumping = false;
			if (velY < 0 && !jumping) velY += stopJumpSpeed;
			
			if (velY > maxFallSpeed) velY = maxFallSpeed;
		}
	}
	
	@Override
	public void tick() {
		getNextPosition();
		super.tick();
		
		 if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(100);
				width = 60;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(120);
				width = 60;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}

}
