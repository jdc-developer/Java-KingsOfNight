package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.Random;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.Constants;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class HellHound extends Enemy {
	
	private boolean running;
	private long randomTimer;
	
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
		flinchXSpeed = 1f;
		maxFlinchXSpeed = 2.5f;
		flinchYSpeed = 2.5f;
		maxFlinchYSpeed = 3f;
		
		width = 60;
		height = 60;
		cwidth = 35;
		cheight = 60;
		
		health = maxHealth = 20;
		bleeds = true;
		damage = 6.8f;
		
		shieldDamage = 1.5f;
		shieldCost = 3.8f;
		sightXDistance = 650;
		sightYDistance = 250;
		
		hitClip = "hellhound-bite";
		audioPlayer.loadAudio(hitClip, "/sfx/enemies/hellhound/bite.mp3");
		
		audioPlayer.loadAudio("hellhound-run", "/sfx/enemies/hellhound/running.mp3");
		audioPlayer.loadAudio("hellhound-roar", "/sfx/enemies/hellhound/roar.mp3");
		audioPlayer.loadAudio("hellhound-howl", "/sfx/enemies/hellhound/howl.mp3");
		audioPlayer.loadAudio("hellhound-howl-2", "/sfx/enemies/hellhound/howl-2.mp3");
		
		if (spriteLoader.getSprites("hellhound") == null) {
			BufferedImage[][] sprites = new BufferedImage[4][];
			sprites[0] = spriteLoader.loadAction("/sprites/enemies/hellhound/idle.png", this, 0, 6, 0, 1, 11, 22, 40, 24, 0, 0);
			sprites[1] = spriteLoader.loadAction("/sprites/enemies/hellhound/walking.png", this, 0, 12, 0, 1, 9, 19, 45, 26, 0, 0);
			sprites[2] = spriteLoader.loadAction("/sprites/enemies/hellhound/jumping.png", this, 0, 6, 0, 1, 11, 14, 47, 39, 0, 0);
			sprites[3] = spriteLoader.loadAction("/sprites/enemies/hellhound/running.png", this, 0, 5, 0, 1, 9, 22, 45, 29, 0, 0);
		
			spriteLoader.loadSprites("hellhound", sprites);
		}
		
		animator = new Animator(spriteLoader.getAction("hellhound", IDLE));
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
		
		if (dying) {
			dead = true;
			audioPlayer.close("hellhound-run");
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
			if (elapsed > 400) {
				flinching = false;
			}
			
			if (elapsed > 200) {
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
				animator.setFrames(spriteLoader.getAction("hellhound", JUMPING));
				animator.setSpeed(100);
				width = 60;
			}
		} else if ((left || right) && !corner) {
			if (currentAction != WALKING && !running) {
				currentAction = WALKING;
				animator.setFrames(spriteLoader.getAction("hellhound", WALKING));
				animator.setSpeed(100);
				width = 60;
			}
			
			if (currentAction != RUNNING && running) {
				currentAction = RUNNING;
				audioPlayer.play("hellhound-run");
				animator.setFrames(spriteLoader.getAction("hellhound", RUNNING));
				animator.setSpeed(100);
				width = 60;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(spriteLoader.getAction("hellhound", IDLE));
				animator.setSpeed(120);
				width = 60;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
		
		if (running) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 3400) {
				audioPlayer.play("hellhound-run");
			}
		} else {
			audioPlayer.close("hellhound-run");
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		
		if (playerXDistance <= sightXDistance  && playerXDistance >= 0 &&
				(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
				playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
			running = true;
			holdTimer = System.nanoTime();
		} else if (playerXDistance >= -sightXDistance && playerXDistance <= 0 &&
				(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
				playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
			running = true;
			holdTimer = System.nanoTime();
		}
		
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 150) {
			Random random = Constants.RANDOM;
			int r = random.nextInt(4);
			randomTimer = System.nanoTime();
			
			if (playerXDistance <= 100 && playerXDistance >= 0 && r == 1 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				jumping = true;
				audioPlayer.play("hellhound-roar");
			} else if (playerXDistance >= -100 && playerXDistance <= 0 && r == 1 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				jumping = true;
				audioPlayer.play("hellhound-roar");
			}
		}
	}
	
	@Override
	public void hit(float damage, boolean right, boolean shield) {
		super.hit(damage, right, shield);
		if (!dead && !dying) {
			Random random = Constants.RANDOM;
			int r = random.nextInt(2);
			
			if (r == 1) {
				audioPlayer.play("hellhound-howl");
			} else {
				audioPlayer.play("hellhound-howl-2");
			}
		}
	}

}
