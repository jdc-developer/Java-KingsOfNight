package jdc.kings.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;

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
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int STABBING = 4;
	private static final int CUTTING = 5;
	private static final int SLICING = 6;

	public Player(int x, int y) {
		super(x, y);
		width = 63;
		height = 74;
		
		moveSpeed = 2.6f;
		maxSpeed = 4.6f;
		stopSpeed = 0.4f;
		fallSpeed = 0.15f;
		maxFallSpeed = 4.0f;
		jumpStart = -8.8f;
		stopJumpSpeed = 0.3f;
		
		stabDamage = 12;
		stabRange = 40;
		
		cutDamage = 8;
		cutRange = 80;
		
		sliceDamage = 15;
		sliceRange = 120;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/player/idle.png", this, 0, 15, 22, 38, 26, 30, 0, 0));
		sprites.add(loader.loadAction("/player/walking.png", this, 0, 8, 40, 66, 30, 30, 0, 0));
		sprites.add(loader.loadAction("/player/jumping_and_falling.png", this, 0, 7, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/player/jumping_and_falling.png", this, 8, 10, 54, 118, 26, 31, 0, 0));
		sprites.add(loader.loadAction("/player/stabbing.png", this, 0, 9, 54, 94, 50, 30, 50, 0));
		sprites.add(loader.loadAction("/player/cutting.png", this, 0, 5, 60, 94, 50, 33, 50, 0));
		sprites.add(loader.loadAction("/player/slicing.png", this, 0, 7, 48, 96, 51, 28, 50, 0));
		
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
		
		if ((currentAction == STABBING || currentAction == CUTTING) &&
				!(jumping || falling)) {
			velX = 0;
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
		x += velX;
		y += velY;
		
		if (currentAction == STABBING) {
			if (animator.hasPlayedOnce()) stabbing = false;
			
		}
		
		if (currentAction == CUTTING) {
			if (animator.hasPlayedOnce()) cutting = false;
		}
		
		if (currentAction == SLICING) {
			if (animator.hasPlayedOnce()) {
				slicing = false;
				maxSpeed = 4.6f;
				if (facingRight == true) right = false;
				else left = false;
			}
		}
		
		if (stabbing) {
			if (currentAction != STABBING) {
				currentAction = STABBING;
				animator.setFrames(sprites.get(STABBING));
				animator.setSpeed(85);
				width = 63;
			}
		} else if (cutting) {
			if (currentAction != CUTTING) {
				currentAction = CUTTING;
				animator.setFrames(sprites.get(CUTTING));
				animator.setSpeed(80);
				width = 63;
			}
		} else if (slicing) {
			if (currentAction != SLICING) {
				currentAction = SLICING;
				animator.setFrames(sprites.get(SLICING));
				animator.setSpeed(100);
				width = 63;
				
				maxSpeed = 1f;
				if (facingRight) right = true;
				else left = true;
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

	public boolean isStabbing() {
		return stabbing;
	}

	public boolean isCutting() {
		return cutting;
	}

	public void setCutting(boolean cutting) {
		this.cutting = cutting;
	}

	public void setSlicing(boolean slicing) {
		this.slicing = slicing;
	}

}
