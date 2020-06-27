package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.Random;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.utils.Constants;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonKnight extends Enemy {
	
	private boolean cutting;
	private boolean slicing;
	private long randomTimer;
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int CUTTING = 2;
	private static final int SLICING = 3;
	private static final int DYING = 4;

	public SkeletonKnight(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 0.35f;
		maxSpeed = 0.4f;
		fallSpeed = 0.2f;
		maxFallSpeed = 5f;
		jumpStart = -0.8f;
		maxJumpSpeed = 3.5f;
		stopJumpSpeed = 1.3f;
		flinchXSpeed = 0.5f;
		maxFlinchXSpeed = 0.8f;
		flinchYSpeed = 5.5f;
		maxFlinchYSpeed = 6f;
		
		width = 70;
		height = 110;
		cwidth = 35;
		cheight = 105;
		
		attacks.add(new Attack(7.6f, 2.5f, 3.8f, 66.6f, 4.5f, 250, 500));
		attacks.add(new Attack(9.8f, 3.1f, 5.8f, 100.9f, 6.9f, 250, 500));
		
		audioPlayer.loadAudio("skeleton-knight-axe", "/sfx/enemies/skeleton-knight/axe.mp3");
		audioPlayer.loadAudio("skeleton-knight-axe-hit", "/sfx/enemies/skeleton-knight/axe-hit.mp3");
		audioPlayer.loadAudio("skull-break", "/sfx/enemies/skull-break.mp3");
		
		health = maxHealth = 20;
		stamina = maxStamina = 15;
		damage = 6.1f;
		
		shieldDamage = 1.2f;
		shieldCost = 2.5f;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		if (spriteLoader.getSprites("skeleton-knight") == null) {
			BufferedImage[][] sprites = new BufferedImage[5][];
			
			sprites[0] = spriteLoader.loadAction("/sprites/enemies/skeleton-knight/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 322, 0, 0);
			sprites[1] = spriteLoader.loadAction("/sprites/enemies/skeleton-knight/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 325, 0, 0);
			sprites[2] = spriteLoader.loadAction("/sprites/enemies/skeleton-knight/cutting.png", this, 0, 5, 0, 1, 0, 0, 246, 265, 50, 10);
			sprites[3] = spriteLoader.loadAction("/sprites/enemies/skeleton-knight/slicing.png", this, 0, 5, 0, 1, 0, 0, 246, 265, 50, 10);
			sprites[4] = spriteLoader.loadAction("/sprites/enemies/skeleton-knight/dying.png", this, 0, 7, 0, 1, 0, 0, 200, 186, 0, 0);
		
			spriteLoader.loadSprites("skeleton-knight", sprites);
		}
	
		animator = new Animator(spriteLoader.getAction("skeleton-knight", IDLE));
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
		
		if (currentAction == CUTTING &&
				!(jumping || falling)) {
			velX = 0;
		}
		
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (stamina < maxStamina) {
			stamina += 0.04f;
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
			checkPlayerDamage();
			super.tick();
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 200) {
				flinching = false;
				flinchTimer = 0;
			}
			
			if (elapsed > 200) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (cutting) {
				animator.setFrames(spriteLoader.getAction("skeleton-knight", IDLE));
				cutting = false;
				height = 110;
				width = 70;
			}
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
				dead = true;
			}
			height = 110;
			width = 70;
		}
		
		if (currentAction == CUTTING) {
			if (animator.hasPlayedOnce()) {
				cutting = false;
				height = 110;
				width = 70;
			}
		}
		
		if (currentAction == SLICING) {
			if (animator.hasPlayedOnce()) {
				slicing = false;
				height = 110;
				width = 70;
				maxSpeed = 0.4f;
			}
		}
		
		if (currentAction != SLICING) {
			maxSpeed = 0.4f;
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				audioPlayer.play("skull-break");
				animator.setFrames(spriteLoader.getAction("skeleton-knight", DYING));
				animator.setSpeed(120);
				holdTimer = System.nanoTime();
				width = 105;
				height = 130;
				left = right = false;
				velX = 0;
			}
		} else if (cutting && !slicing) {
			if (currentAction != CUTTING) {
				attack = attacks.get(0);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = CUTTING;
					attack.setTimer(System.nanoTime());
					audioPlayer.play("skeleton-knight-axe");
					
					stamina -= attack.getCost();
					animator.setFrames(spriteLoader.getAction("skeleton-knight", CUTTING));
					animator.setSpeed(100);
					width = 105;
					height = 130;
				}
			}
		} else if (slicing && !cutting) {
			if (currentAction != SLICING) {
				attack = attacks.get(1);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = SLICING;
					attack.setTimer(System.nanoTime());
					audioPlayer.play("skeleton-knight-axe");
					
					stamina -= attack.getCost();
					animator.setFrames(spriteLoader.getAction("skeleton-knight", SLICING));
					animator.setSpeed(150);
					width = 105;
					height = 130;
					
					maxSpeed = 2f;
					jumping = true;
					
					if (facingRight) right = true;
					else left = true;
				}
			}
		} else if ((left || right) && !corner) {
			if (currentAction != WALKING) {
				previousAction = currentAction;
				currentAction = WALKING;
				animator.setFrames(spriteLoader.getAction("skeleton-knight", WALKING));
				animator.setSpeed(80);
				width = 70;
			}
		} else {
			if (currentAction != IDLE) {
				previousAction = currentAction;
				currentAction = IDLE;
				animator.setFrames(spriteLoader.getAction("skeleton-knight", IDLE));
				animator.setSpeed(120);
				width = 70;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	public void checkPlayerDamage() {
		super.checkPlayerDamage();
		if ((cutting || slicing) && attack != null) {
			attack.checkAttack(this, player, false, "skeleton-knight-axe-hit");
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 500 && !player.isDead()) {
			Random random = Constants.RANDOM;
			int r = random.nextInt(2);
			randomTimer = System.nanoTime();
			
			if (playerXDistance <= 100 && playerXDistance >= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				if (r == 1) {
					cutting = true;
					slicing = false;
				} else {
					slicing = true;
					cutting = false;
				}
			} else if (playerXDistance >= -100 && playerXDistance <= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				if (r == 1) {
					cutting = true;
					slicing = false;
				} else {
					slicing = true;
					cutting = false;
				}
			}
		}
	}

}
