package jdc.kings.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
		cwidth = 60;
		cheight = 60;
		
		moveSpeed = 2.6f;
		maxSpeed = 4.6f;
		stopSpeed = 0.4f;
		fallSpeed = 0.15f;
		maxFallSpeed = 4.0f;
		jumpStart = -7.8f;
		stopJumpSpeed = 0.3f;
		
		stabDamage = 12;
		stabRange = 40;
		
		cutDamage = 8;
		cutRange = 80;
		
		sliceDamage = 15;
		sliceRange = 120;
		
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
				if (previousAction != WALKING && previousAction != FALLING &&
						previousAction != JUMPING) {
					if (facingRight) right = false;
					else left = false;
				}
			}
		}
		
		if (currentAction == SHIELD) {
			animator.holdLastFrame();
		}
		
		if (stabbing) {
			if (currentAction != STABBING) {
				previousAction = currentAction;
				currentAction = STABBING;
				animator.setFrames(sprites.get(STABBING));
				animator.setSpeed(85);
				width = 63;
			}
		} else if (cutting) {
			if (currentAction != CUTTING) {
				previousAction = currentAction;
				currentAction = CUTTING;
				animator.setFrames(sprites.get(CUTTING));
				animator.setSpeed(80);
				width = 63;
			}
		} else if (slicing) {
			if (currentAction != SLICING) {
				previousAction = currentAction;
				currentAction = SLICING;
				animator.setFrames(sprites.get(SLICING));
				animator.setSpeed(100);
				width = 63;
				
				maxSpeed = 1f;
				if (facingRight) right = true;
				else left = true;
			}
		} else if (rolling) {
			if (currentAction != ROLLING) {
				previousAction = currentAction;
				currentAction = ROLLING;
				animator.setFrames(sprites.get(ROLLING));
				animator.setSpeed(80);
				width = 63;
				
				maxSpeed = 5.5f;
				if (facingRight) right = true;
				else left = true;
			}
		} else if (shield) {
			if (currentAction != SHIELD) {
				currentAction = SHIELD;
				animator.setFrames(sprites.get(SHIELD));
				animator.setSpeed(100);
				width = 63;
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
				currentAction = JUMPING;
				animator.setFrames(sprites.get(JUMPING));
				animator.setSpeed(100);
				width = 63;
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

	public void setStabbing(boolean stabbing) {
		this.stabbing = stabbing;
	}

	public void setCutting(boolean cutting) {
		this.cutting = cutting;
	}

	public void setSlicing(boolean slicing) {
		this.slicing = slicing;
	}

	public void setRolling(boolean rolling) {
		this.rolling = rolling;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

}
