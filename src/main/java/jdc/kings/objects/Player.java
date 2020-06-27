package jdc.kings.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.input.Key;
import jdc.kings.input.KeyInput;
import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class Player extends GameObject {
	
	private int rollCost;
	
	private boolean stabbing;
	private boolean cutting;
	private boolean slicing;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int STABBING = 4;
	private static final int CUTTING = 5;
	private static final int SLICING = 6;
	private static final int ROLLING = 7;
	private static final int SHIELD = 8;
	private static final int DYING = 9;

	public Player(TileMap tm) {
		super(tm);
		width = 63;
		height = 74;
		cwidth = 45;
		cheight = 60;
		
		moveSpeed = 2.6f;
		maxSpeed = 4.6f;
		stopSpeed = 0.4f;
		fallSpeed = 0.15f;
		maxFallSpeed = 5.0f;
		jumpStart = -7.8f;
		maxJumpSpeed = 5.8f;
		stopJumpSpeed = 0.3f;
		flinchXSpeed = 2.8f;
		maxFlinchXSpeed = 4f;
		flinchYSpeed = 5.5f;
		maxFlinchYSpeed = 5.8f;
		
		attacks.add(new Attack(13.5f, 4, 4.5f, 80.5f, 3, 250, 400));
		attacks.add(new Attack(8.2f, 2.8f, 3, 105.2f, 2.5f, 50, 250));
		attacks.add(new Attack(15, 5, 4.5f, 106.9f, 6, 250, 400));
		
		audioPlayer.loadAudio("player-cut", "/sfx/player/cutting.mp3");
		audioPlayer.loadAudio("player-slice", "/sfx/player/slicing.mp3");
		audioPlayer.loadAudio("player-roll", "/sfx/player/roll.mp3");
		audioPlayer.loadAudio("player-air-roll", "/sfx/player/air-roll.mp3");
		audioPlayer.loadAudio("player-shield", "/sfx/player/shield.mp3");
		audioPlayer.loadAudio("player-stab", "/sfx/player/stabbing.mp3");
		audioPlayer.loadAudio("player-sword-hit", "/sfx/player/sword-hit.mp3");
		audioPlayer.loadAudio("player-jump", "/sfx/player/jump.mp3");
		
		health = maxHealth = 50;
		stamina = maxStamina = 20;
		bleeds = true;
		
		rollCost = 5;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/player/idle.png", this, 0, 15, 0, 1, 22, 38, 26, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/walking.png", this, 0, 8, 0, 1, 40, 66, 30, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/jumping_and_falling.png", this, 0, 7, 0, 1, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/jumping_and_falling.png", this, 8, 10, 0, 1, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/stabbing.png", this, 0, 9, 0, 1, 54, 94, 50, 30, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/cutting.png", this, 0, 5, 0, 1, 60, 94, 50, 33, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/slicing.png", this, 0, 7, 0, 1, 48, 96, 51, 28, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/rolling.png", this, 0, 9, 0, 1, 54, 153, 27, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/shield.png", this, 0, 7, 0, 1, 39, 72, 24, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/dying.png", this, 0, 15, 0, 1, 34, 69, 29, 30, 0, 0));
		
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(100);
		animator.start();
	}
	
	private void getNextPosition() {
		if (left && !dead) {
			velX -= moveSpeed;
			if (velX < -maxSpeed) {
				velX = -maxSpeed;
			}
		} else if (right && !dead) {
			velX += moveSpeed;
			if (velX > maxSpeed) {
				velX = maxSpeed;
			}
		} else {
			if (velX > 0) {
				velX -= stopSpeed;
				if (velX < 0) {
					velX = 0;
				}
			} else if (velX < 0) {
				velX += stopSpeed;
				if (velX > 0) {
					velX = 0;
				}
			}
		}
		
		if ((currentAction == STABBING || currentAction == CUTTING || currentAction == SHIELD || currentAction == SLICING) &&
				!(jumping || falling)) {
			velX = 0;
		}
		
		if (currentAction == ROLLING && (jumping || falling)) {
			maxSpeed = 6.5f;
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

	public void tick() {
		getNextPosition();
		super.tick();
		
		if (currentAction == STABBING) {
			if (animator.hasPlayedOnce()) stabbing = false;
			if (previousAction == ROLLING) {
				rolling = false;
				maxSpeed = 4.6f;
				if (facingRight) right = false;
				else left = false;
			}
		}
		
		if (currentAction == CUTTING) {
			if (animator.hasPlayedOnce()) cutting = false;
			if (previousAction == ROLLING) {
				rolling = false;
				maxSpeed = 4.6f;
				if (facingRight) right = false;
				else left = false;
			}
		}
		
		if (currentAction == SLICING) {
			if (animator.hasPlayedOnce()) {
				slicing = false;
				rolling = false;
			}
		}
		
		if (currentAction == ROLLING) {
			if (animator.hasPlayedOnce()) {
				rolling = false;
				maxSpeed = 4.6f;
				
				KeyInput keyInput = KeyInput.getInstance();
				Key rightKey = keyInput.findKey(KeyAction.RIGHT);
				Key leftKey = keyInput.findKey(KeyAction.LEFT);
				
				if (facingRight && !rightKey.isPressed()) {
					right = false;
				} else if (!leftKey.isPressed()) {
					left = false;
				}
			}
		}
		
		if (currentAction == SHIELD) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
			}
		}
		
		if (currentAction == JUMPING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
			}
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 700) {
				flinching = false;
			}
			
			if (elapsed > 500) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
				dead = true;
			}
		}
		
		if (shielding) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 500) {
				shielding = false;
			}
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				animator.setFrames(sprites.get(DYING));
				animator.setSpeed(70);
				holdTimer = System.nanoTime();
				right = false;
				left = false;
			}
		} else if (stabbing) {
			if (currentAction != STABBING) {
				attack = attacks.get(0);
				
				if (stamina >= attack.getCost()) {
					previousAction = currentAction;
					currentAction = STABBING;
					audioPlayer.play("player-stab");
					
					animator.setFrames(sprites.get(STABBING));
					animator.setSpeed(85);
					width = 63;
					stamina -= attack.getCost();
					attack.setTimer(System.nanoTime());
				} else {
					stabbing = false;
				}
			}
		} else if (cutting) {
			if (currentAction != CUTTING) {
				attack = attacks.get(1);
				
				if (stamina >= attack.getCost()) {
					previousAction = currentAction;
					currentAction = CUTTING;
					audioPlayer.play("player-cut");
					
					animator.setFrames(sprites.get(CUTTING));
					animator.setSpeed(80);
					width = 63;
					stamina -= attack.getCost();
					attack.setTimer(System.nanoTime());
				} else {
					cutting = false;
				}
			}
		} else if (slicing) {
			if (currentAction != SLICING) {
				attack = attacks.get(2);
				if (stamina >= attack.getCost()) {
					previousAction = currentAction;
					currentAction = SLICING;
					audioPlayer.play("player-slice");
					
					animator.setFrames(sprites.get(SLICING));
					animator.setSpeed(100);
					width = 63;
					
					stamina -= attack.getCost();
					attack.setTimer(System.nanoTime());
				} else {
					slicing = false;
				}
			}
		} else if (rolling) {
			if (currentAction != ROLLING) {
				if (stamina >= rollCost) {
					previousAction = currentAction;
					currentAction = ROLLING;
					
					if (previousAction == JUMPING || previousAction == FALLING) {
						audioPlayer.play("player-air-roll");
					} else {
						audioPlayer.play("player-roll");
					}
					
					animator.setFrames(sprites.get(ROLLING));
					animator.setSpeed(80);
					width = 63;
					
					stamina -= rollCost;
					rollTimer = System.nanoTime();
					maxSpeed = 5.5f;
					
					if (facingRight) right = true;
					else left = true;
				} else {
					rolling = false;
				}
				
			}
		} else if (shield) {
			if (currentAction != SHIELD) {
				currentAction = SHIELD;
				animator.setFrames(sprites.get(SHIELD));
				animator.setSpeed(100);
				width = 63;
				holdTimer = System.nanoTime();
			}
		} else if (velY > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animator.setFrames(sprites.get(FALLING));
				animator.setSpeed(400);
				width = 63;
			}
		} else if (velY < 0) {
			if (currentAction != JUMPING) {
				previousAction = currentAction;
				currentAction = JUMPING;
				
				if (!flinching) {
					audioPlayer.play("player-jump");
				}
				
				animator.setFrames(sprites.get(JUMPING));
				animator.setSpeed(100);
				width = 63;
				holdTimer = System.nanoTime();
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(100);
				width = 63;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(100);
				width = 63;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	public void checkAttack(List<Enemy> enemies) {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if ((stabbing || cutting || slicing) && attack != null) {
				attack.checkAttack(this, e, false, "player-sword-hit");
			}
			
		}
	}
	
	@Override
	public void hit(float damage, boolean right, boolean shield) {
		if (shield) {
			audioPlayer.play("player-shield");
		}
		super.hit(damage, right, shield);
	}

	public void setStabbing(boolean stabbing) {
		this.stabbing = stabbing;
	}

	public void setCutting(boolean cutting) {
		this.cutting = cutting;
	}

	public void setSlicing(boolean slicing) {
		this.slicing = slicing;
	}
	
	public boolean isAttacking() {
		return stabbing || cutting || slicing;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public float getHealth() {
		return health;
	}
	
	public float getMaxStamina() {
		return maxStamina;
	}
	
	public float getStamina() {
		return stamina;
	}

}
