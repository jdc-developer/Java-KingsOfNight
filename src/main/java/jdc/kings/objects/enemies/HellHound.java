package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.Constants;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class HellHound extends Enemy {
	
	private boolean running;
	private long randomTimer;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int RUNNING = 3;

	public HellHound(TileMap tm) {
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
		height = 60;
		cwidth = 35;
		cheight = 60;
		
		health = maxHealth = 20;
		bleeds = true;
		damage = 7;
		
		shieldDamage = 1;
		shieldCost = 4;
		sightDistance = 650;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/idle.png", this, 0, 6, 0, 1, 11, 22, 40, 24, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/walking.png", this, 0, 12, 0, 1, 9, 19, 45, 26, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/jumping.png", this, 0, 6, 0, 1, 11, 14, 47, 39, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/running.png", this, 0, 5, 0, 1, 9, 22, 45, 29, 0, 0));
	
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
		playerPosition();
		checkPlayerDamage();
		super.tick();
		
		if (currentAction == JUMPING) {
			if (animator.hasPlayedOnce()) jumping = false;
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
			
			if (elapsed > 100) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
		}
		
		if (jumping) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				turnAroundTimer = System.nanoTime();
				animator.setFrames(sprites.get(JUMPING));
				animator.setSpeed(100);
				width = 60;
			}
		} else if (left || right) {
			if (currentAction != WALKING && !running) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(100);
				width = 60;
			}
			
			if (currentAction != RUNNING && running) {
				currentAction = RUNNING;
				animator.setFrames(sprites.get(RUNNING));
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
	
	public void playerPosition() {
		super.playerPosition();
		if (playerDistance <= sightDistance  && playerDistance > 0) {
			running = true;
		} else if (playerDistance >= -sightDistance && playerDistance < 0) {
			running = true;
		}
		
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 150) {
			Random random = Constants.random;
			int r = random.nextInt(4);
			randomTimer = System.nanoTime();
			
			if (playerDistance <= 100 && playerDistance > 0 && r == 1) {
				jumping = true;
			} else if (playerDistance >= -100 && playerDistance < 0 && r == 1) {
				jumping = true;
			}
		}
	}

}
