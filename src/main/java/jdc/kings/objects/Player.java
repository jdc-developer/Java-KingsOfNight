package jdc.kings.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.input.Key;
import jdc.kings.input.KeyInput;
import jdc.kings.input.enums.KeyAction;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class Player extends GameObject {
	
	private boolean stabbing;
	private int stabDamage;
	private int stabRange;
	
	private boolean cutting;
	private int cutDamage;
	private int cutRange;
	
	private boolean slicing;
	private int sliceDamage;
	private int sliceRange;
	
	private boolean rolling;
	private boolean shield;
	private long attackTimer;
	private long holdTimer;
	private long rollTimer;
	
	private int rollCost;
	private int stabCost;
	private int cutCost;
	private int sliceCost;
	
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

	public Player(TileMap tm) {
		super(tm);
		width = 63;
		height = 74;
		cwidth = 45;
		cheight = 45;
		
		moveSpeed = 2.6f;
		maxSpeed = 4.6f;
		stopSpeed = 0.4f;
		fallSpeed = 0.15f;
		maxFallSpeed = 5.0f;
		jumpStart = -7.8f;
		stopJumpSpeed = 0.3f;
		
		stabDamage = 12;
		stabRange = 70;
		
		cutDamage = 8;
		cutRange = 106;
		
		sliceDamage = 15;
		sliceRange = 120;
		
		health = maxHealth = 50;
		stamina = maxStamina = 20;
		
		rollCost = 5;
		stabCost = 4;
		cutCost = 3;
		sliceCost = 6;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/player/idle.png", this, 0, 15, 22, 38, 26, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/walking.png", this, 0, 8, 40, 66, 30, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/jumping_and_falling.png", this, 0, 7, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/jumping_and_falling.png", this, 8, 10, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/stabbing.png", this, 0, 9, 54, 94, 50, 30, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/cutting.png", this, 0, 5, 60, 94, 50, 33, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/slicing.png", this, 0, 7, 48, 96, 51, 28, 50, 0));
		sprites.add(loader.loadAction("/sprites/player/rolling.png", this, 0, 9, 54, 153, 27, 30, 0, 0));
		sprites.add(loader.loadAction("/sprites/player/shield.png", this, 0, 7, 39, 72, 24, 30, 0, 0));
		
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(100);
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
		
		if ((currentAction == STABBING || currentAction == CUTTING || currentAction == SHIELD) &&
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
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
				maxSpeed = 4.6f;
				
				if (facingRight) right = false;
				else left = false;
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
			if (elapsed > 1000) {
				flinching = false;
			}
		}
		
		if (stabbing) {
			if (currentAction != STABBING) {
				previousAction = currentAction;
				currentAction = STABBING;
				if (stamina >= stabCost) {
					animator.setFrames(sprites.get(STABBING));
					animator.setSpeed(85);
					width = 63;
					stamina -= stabCost;
				} else {
					stabbing = false;
				}
			}
		} else if (cutting) {
			if (currentAction != CUTTING) {
				previousAction = currentAction;
				currentAction = CUTTING;
				if (stamina >= cutCost) {
					animator.setFrames(sprites.get(CUTTING));
					animator.setSpeed(80);
					width = 63;
					stamina -= sliceCost;
				} else {
					cutting = false;
				}
			}
		} else if (slicing) {
			if (currentAction != SLICING) {
				previousAction = currentAction;
				currentAction = SLICING;
				if (stamina >= sliceCost) {
					animator.setFrames(sprites.get(SLICING));
					animator.setSpeed(100);
					width = 63;
					
					stamina -= sliceCost;
					maxSpeed = 1f;
					
					if (facingRight) right = true;
					else left = true;
				} else {
					slicing = false;
				}
			}
		} else if (rolling) {
			if (currentAction != ROLLING) {
				previousAction = currentAction;
				currentAction = ROLLING;
				
				if (stamina >= rollCost) {
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
			if (stabbing || cutting || slicing) {
				int range = 0;
				int damage = 0;
				
				if (stabbing) {
					long elapsed = (System.nanoTime() - attackTimer) / 1000000;
					if (elapsed > 250) {
						range = stabRange;
						damage = stabDamage;
					}
				} else if (cutting) {
					range = cutRange;
					damage = cutDamage;
				} else if (slicing) {
					long elapsed = (System.nanoTime() - attackTimer) / 1000000;
					if (elapsed > 250) {
						range = sliceRange;
						damage = sliceDamage;
					}
				}
				
				 if (facingRight) {
					 if (
							 e.getX() > x &&
							 e.getX() < x + range &&
							 e.getY() > y - height / 2 &&
							 e.getY() < y + height / 2) {
						 if (damage > 0) {
							 e.hit(damage);
						 }
					 }
				 } else {
					 if (
							 e.getX() < x &&
							 e.getX() > x - range &&
							 e.getY() > y - height / 2 &&
							 e.getY() < y + height / 2) {
						 if (damage > 0) {
							 e.hit(damage);
						 }
					 }
				 }
			}
			
			
		}
	}

	public void setStabbing(boolean stabbing) {
		this.stabbing = stabbing;
		attackTimer = System.nanoTime();
	}

	public void setCutting(boolean cutting) {
		this.cutting = cutting;
	}

	public void setSlicing(boolean slicing) {
		this.slicing = slicing;
		attackTimer = System.nanoTime();
	}

	public void setRolling(boolean rolling) {
		this.rolling = rolling;
	}

	public boolean isRolling() {
		return rolling;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public boolean isShield() {
		return shield;
	}
	
	public long getRollTimer() {
		return rollTimer;
	}

	public long getHoldTimer() {
		return holdTimer;
	}

	public void shieldDamage(int shieldDamage, int damage, int cost) {
		if (stamina < cost) {
			hit(damage);
		} else {
			if (!flinching) {
				stamina -= cost;
			}
			hit(shieldDamage);
		}
	}

}
