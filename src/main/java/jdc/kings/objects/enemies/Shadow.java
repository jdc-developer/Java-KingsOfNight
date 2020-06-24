package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.utils.Constants;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class Shadow extends Enemy {
	
	private boolean rising;
	private boolean standing;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	private Map<String, AudioPlayer> sfx = new HashMap<>();
	
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
		
		health = maxHealth = 20;
		stamina = maxStamina = 15;
		damage = 7;
		
		shieldDamage = 1;
		shieldCost = 4;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		sfx.put("rising", new AudioPlayer("/sfx/enemies/shadow/rising.mp3"));
		sfx.put("agony", new AudioPlayer("/sfx/enemies/shadow/agony.mp3"));
		sfx.put("dying", new AudioPlayer("/sfx/enemies/shadow/dying.mp3"));
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/shadow/idle.png", this, 0, 4, 0, 1, 0, 0, 80, 70, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/shadow/rising.png", this, 0, 5, 0, 2, 0, 0, 80, 70, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/shadow/idle.png", this, 0, 4, 1, 2, 0, 0, 80, 70, 0, 0));
	
		animator = new Animator(sprites.get(0));
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
				animator.setFrames(sprites.get(IDLE));
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
			sfx.get("agony").stop();
		}
		
		int random = Constants.random.nextInt(120);
		long scream = (System.nanoTime() - holdTimer) / 1000000;
		if (random == 1 && scream > 2500 && !player.isDead() && standing) {
			sfx.get("agony").play();
			holdTimer = System.nanoTime();
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				sfx.get("dying").play();
				sfx.get("agony").stop();
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(150);
				holdTimer = System.nanoTime();
				left = right = false;
				velX = 0;
			}
		} else if (standing) {
			 if (currentAction != STANDING) {
					currentAction = STANDING;
					animator.setFrames(sprites.get(STANDING));
					animator.setSpeed(150);
				}
		 } else if (rising) {
			if (currentAction != RISING) {
				currentAction = RISING;
				sfx.get("rising").play();
				animator.setFrames(sprites.get(RISING));
				animator.setSpeed(150);
				holdTimer = System.nanoTime();
			}
		} else if ((left || right) && !corner) {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(150);
				maxSpeed = 1f;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
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
	public void hit(int damage, boolean right, boolean shield) {
		if (standing) {
			super.hit(damage, right, shield);
		}
	}

}
