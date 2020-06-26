package jdc.kings.objects.enemies.bosses;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.utils.Constants;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SpiderBoss extends Enemy {
	
	private boolean slashing;
	private boolean slicing;
	private long randomTimer;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int SLASHING = 2;
	private static final int SLICING = 3;

	public SpiderBoss(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 3f;
		maxSpeed = 5.1f;
		fallSpeed = 0.2f;
		maxFallSpeed = 5f;
		jumpStart = -8.8f;
		maxJumpSpeed = 5.5f;
		stopJumpSpeed = 1.3f;
		flinchYSpeed = 1.5f;
		flinchXSpeed = 0.5f;
		maxFlinchXSpeed = 1f;
		
		width = 130;
		height = 150;
		cwidth = 65;
		cheight = 80;
		
		health = maxHealth = 200;
		bleeds = true;
		stamina = maxStamina = 100;
		damage = 8;
		
		shieldDamage = 2;
		shieldCost = 4;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		attacks.add(new Attack(12, 3, 5, 100, 10, 100, 300));
		attacks.add(new Attack(15, 5, 8, 100, 15, 100, 300));
		
		audioPlayer.loadAudio("spider-boss-tear", "/sfx/enemies/bosses/spider/tearing.mp3");
		audioPlayer.loadAudio("spider-boss-whoosh", "/sfx/enemies/bosses/spider/whoosh.mp3");
		audioPlayer.loadAudio("spider-boss-scream", "/sfx/enemies/bosses/spider/scream.mp3");
		audioPlayer.loadAudio("spider-boss-short-scream", "/sfx/enemies/bosses/spider/short-scream.mp3");
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/bosses/spider/spritesheet.png", this, 0, 2, 3, 4, 334, 0, 83, 73, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/bosses/spider/spritesheet.png", this, 0, 4, 0, 1, 0, 0, 92, 71, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/bosses/spider/spritesheet.png", this, 0, 5, 1, 2, 0, 0, 96, 73, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/bosses/spider/spritesheet.png", this, 0, 5, 2, 3, 0, 0, 94, 73, 0, 0));
	
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(200);
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
		
		if ((currentAction == SLICING || currentAction == SLASHING) &&
				!(jumping || falling)) {
			velX = 0;
		}
		
		if (stamina < maxStamina) {
			stamina += 0.04f;
		}
		
		if (dying) {
			dead = true;
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
	
	public void tick() {
		getNextPosition();
		playerPosition();
		checkPlayerDamage();
		super.tick();
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 600) {
				flinching = false;
			}
			
			if (elapsed > 300) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (slicing || slashing) {
				animator.setFrames(sprites.get(IDLE));
				slicing = false;
				slashing = false;
			}
		}
		
		if (currentAction == SLASHING) {
			if (animator.hasPlayedOnce()) {
				slashing = false;
			}
		}
		
		if (currentAction == SLICING) {
			if (animator.hasPlayedOnce()) {
				slicing = false;
			}
		}
		
		int random = Constants.random.nextInt(120);
		long scream = (System.nanoTime() - holdTimer) / 1000000;
		if (random == 1 && scream > 2500 && !player.isDead()) {
			audioPlayer.play("spider-boss-short-scream");
			holdTimer = System.nanoTime();
		}
		
		if (slashing && !slicing) {
			if (currentAction != SLASHING) {
				attack = attacks.get(0);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = SLASHING;
					attack.setTimer(System.nanoTime());
					audioPlayer.play("spider-boss-whoosh");
					
					stamina -= attack.getCost();
					animator.setFrames(sprites.get(SLASHING));
					animator.setSpeed(150);
					
					if (facingRight) right = true;
					else left = true;
				}
			}
		} else if (slicing && !slashing) {
			if (currentAction != SLICING) {
				attack = attacks.get(1);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = SLICING;
					attack.setTimer(System.nanoTime());
					audioPlayer.play("spider-boss-whoosh");
					
					stamina -= attack.getCost();
					animator.setFrames(sprites.get(SLICING));
					animator.setSpeed(150);
					
					if (facingRight) right = true;
					else left = true;
				}
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(80);
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(200);
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	public void checkPlayerDamage() {
		super.checkPlayerDamage();
		if ((slashing || slicing) && attack != null) {
			attack.checkAttack(this, player, false, "spider-boss-tear");
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 500) {
			Random random = Constants.random;
			int r = random.nextInt(2);
			randomTimer = System.nanoTime();
			
			if (playerXDistance <= 120 && playerXDistance >= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				if (r == 1) {
					slashing = true;
					slicing = false;
				} else {
					slicing = true;
					slashing = false;
				}
			} else if (playerXDistance >= -120 && playerXDistance <= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				if (r == 1) {
					slashing = true;
					slicing = false;
				} else {
					slicing = true;
					slashing = false;
				}
			}
		}
	}

}
