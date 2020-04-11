package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import jdc.kings.view.Animator;

public abstract class GameObject {
	
	protected float x;
	protected float y;
	protected float velX;
	protected float velY;
	
	protected int width;
	protected int height;
	
	protected Animator animator;
	protected boolean facingRight = true;
	protected int currentAction;
	protected int previousAction;
	
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	protected float moveSpeed;
	protected float maxSpeed;
	protected float stopSpeed;
	protected float fallSpeed;
	protected float maxFallSpeed;
	protected float jumpStart;
	protected float stopJumpSpeed;
	
	public GameObject(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public void render(Graphics g) {
		if (facingRight) {
			g.drawImage(animator.getImage(),
					(int)(x - width / 2),
					(int)(y - height / 2),
					null);
		} else {
			BufferedImage image = animator.getImage();
			g.drawImage(image,
					(int)(x - width / 2 + width),
					(int)(y - height / 2),
					-image.getWidth(),
					height,
					null);
		}
		
		animator.update(System.currentTimeMillis());
	}

}
