package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.Constants;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class Shadow extends Enemy {
	
	private boolean rising;
	private boolean standing;
	
	private static final int IDLE = 0;
	private static final int RISING = 1;
	private static final int STANDING = 2;
	private static final int DYING = 3;

	public Shadow(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 0.4f;
		maxSpeed = 1f;
		fallSpeed = 0.2f;
		maxFallSpeed = 5f;
		jumpStart = -0.8f;
		maxJumpSpeed = 3.5f;
		stopJumpSpeed = 1.3f;
		flinchXSpeed = maxFlinchXSpeed = 0;
		flinchYSpeed = maxFlinchYSpeed = 0;
		
		width = 70;
		height = 90;
		cwidth = 35;
		cheight = 80;
		
		health = maxHealth = 195;
		stamina = maxStamina = 150;
		damage = 73.7f;
		
		shieldDamage = 11.9f;
		shieldCost = 40.8f;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		audioPlayer.loadAudio("shadow-rising", "/sfx/enemies/shadow/rising.mp3");
		audioPlayer.loadAudio("shadow-agony", "/sfx/enemies/shadow/agony.mp3");
		audioPlayer.loadAudio("shadow-dying", "/sfx/enemies/shadow/dying.mp3");
		
		if (spriteLoader.getSprites("shadow") == null) {
			BufferedImage[][] sprites = new BufferedImage[3][];
			
			sprites[0] = spriteLoader.loadAction("/sprites/enemies/shadow/idle.png", this, 0, 4, 0, 1, 0, 0, 80, 70, 0, 0);
			sprites[1] = spriteLoader.loadAction("/sprites/enemies/shadow/rising.png", this, 0, 5, 0, 2, 0, 0, 80, 70, 0, 0);
			sprites[2] = spriteLoader.loadAction("/sprites/enemies/shadow/idle.png", this, 0, 4, 1, 2, 0, 0, 80, 70, 0, 0);
			
			spriteLoader.loadSprites("shadow", sprites);
		}
		
	
		animator = new Animator(spriteLoader.getAction("shadow", IDLE));
		currentAction = IDLE;
		animator.setSpeed(150);
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
		
		if (currentAction == RISING &&
				!(jumping || falling)) {
			velX = 0;
		}
		
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (stamina < maxStamina) {
			stamina += 0.4f;
		}
		
		if (stamina < 0) {
			stamina = 0;
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
		if (!dead) {
			getNextPosition();
			playerPosition();
			
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (standing || (rising && elapsed > 400)) {
				checkPlayerDamage();
			}
			super.tick();
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
				flinchTimer = 0;
			}
			
			if (elapsed > 200) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (rising) {
				animator.setFrames(spriteLoader.getAction("shadow", IDLE));
				rising = false;
			}
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				dead = true;
			}
		}
		
		
		if (currentAction == STANDING) {
			maxSpeed = 0.4f;
			shield = false;
			if (!player.isDead()) {
				if (playerXDistance >= 350 && playerXDistance > 0) {
					standing = false;
				} else if (playerXDistance <= -350 && playerXDistance < 0) {
					standing = false;
				}
			}
		} else {
			shield = true;
		}
		
		if (currentAction == RISING) {
			if (animator.hasPlayedOnce()) {
				rising = false;
				standing = true;
			}
		}
		
		if (player.isDead()) {
			audioPlayer.stop("shadow-agony");
		}
		
		int random = Constants.RANDOM.nextInt(120);
		long scream = (System.nanoTime() - holdTimer) / 1000000;
		if (random == 1 && scream > 2500 && !player.isDead() && standing) {
			audioPlayer.play("shadow-agony");
			holdTimer = System.nanoTime();
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				audioPlayer.play("shadow-dying");
				audioPlayer.stop("shadow-agony");
				animator.setFrames(spriteLoader.getAction("shadow", IDLE));
				animator.setSpeed(150);
				holdTimer = System.nanoTime();
				left = right = false;
				velX = 0;
			}
		} else if (standing) {
			 if (currentAction != STANDING) {
					currentAction = STANDING;
					animator.setFrames(spriteLoader.getAction("shadow", STANDING));
					animator.setSpeed(150);
				}
		 } else if (rising) {
			if (currentAction != RISING) {
				currentAction = RISING;
				audioPlayer.play("shadow-rising");
				animator.setFrames(spriteLoader.getAction("shadow", RISING));
				animator.setSpeed(150);
				holdTimer = System.nanoTime();
			}
		} else if ((left || right) && !corner) {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(spriteLoader.getAction("shadow", IDLE));
				animator.setSpeed(150);
				maxSpeed = 1f;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(spriteLoader.getAction("shadow", IDLE));
				animator.setSpeed(150);
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	public void playerPosition() {
		super.playerPosition();
		
		if (!player.isDead() && !standing) {
			if (playerXDistance <= 50 && playerXDistance >= 0 &&
					(playerYDistance <= 50 && playerYDistance >= 0 ||
					playerYDistance >= -50 && playerYDistance <= 0)) {
				rising = true;
			} else if (playerXDistance >= -50 && playerXDistance <= 0 &&
					(playerYDistance <= 50 && playerYDistance >= 0 ||
					playerYDistance >= -50 && playerYDistance <= 0)) {
				rising = true;
			}
		}
	}
	
	@Override
	public void hit(float damage, boolean right, boolean shield) {
		if (standing) {
			super.hit(damage, right, shield);
		}
	}

}
