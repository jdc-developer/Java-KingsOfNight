package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jdc.kings.objects.interactions.Attack;
import jdc.kings.objects.interactions.Blood;
import jdc.kings.utils.Constants;
import jdc.kings.view.Animator;
import jdc.kings.view.Tile;
import jdc.kings.view.TileMap;

public abstract class GameObject {
	
	protected TileMap tileMap;
	protected int tileSize;
	protected float xmap;
	protected float ymap;
	
	protected float x;
	protected float y;
	protected float velX;
	protected float velY;
	
	protected int width;
	protected int height;
	
	protected int cwidth;
	protected int cheight;
	
	protected int currRow;
	protected int currCol;
	protected float xdest;
	protected float ydest;
	protected float xtemp;
	protected float ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected Animator animator;
	protected Attack attack;
	protected List<Attack> attacks = new ArrayList<>();
	protected LinkedList<Blood> bloodLosses = new LinkedList<>();
	
	protected boolean facingRight = true;
	protected int currentAction;
	protected int previousAction;
	
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean shielding;
	
	protected float moveSpeed;
	protected float maxSpeed;
	protected float stopSpeed;
	protected float fallSpeed;
	protected float maxFallSpeed;
	protected float jumpStart;
	protected float stopJumpSpeed;
	protected float flinchXSpeed;
	protected float maxFlinchXSpeed;
	protected float flinchYSpeed;
	
	protected int health;
	protected float stamina;
	protected int maxHealth;
	protected float maxStamina;
	
	protected boolean rolling;
	protected boolean shield;
	protected boolean dead;
	protected boolean flinching;
	
	protected long flinchTimer;
	protected long holdTimer;
	protected long rollTimer;
	
	protected int flinchDirection;
	
	public GameObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize() + tm.getIncreaseSize();
	}
	
	public boolean intersects(GameObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth,
				(int)y - cheight,
				cwidth,
				cheight);
	}

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
	
	public boolean isFacingRight() {
		return facingRight;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public int getHealth() {
		return health;
	}

	public float getStamina() {
		return stamina;
	}

	public boolean isFlinching() {
		return flinching;
	}

	public long getFlinchTimer() {
		return flinchTimer;
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

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(float velX, float velY) {
		this.velX = velX;
		this.velY = velY;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	public void checkTileMapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + velX;
		ydest = y + velY;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if (velY < 0) {
			if (topLeft || topRight) {
				velY = 0;
				ytemp = currRow * tileSize + cheight / 2;
			} else {
				ytemp += velY;
			}
		}
		if (velY > 0) {
			if (bottomLeft || bottomRight) {
				velY = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			} else {
				ytemp += velY;
			}
		}
		
		calculateCorners(xdest, y);
		if (velX < 0) {
			if (topLeft || bottomLeft) {
				velX = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			} else {
				xtemp += velX;
			}
			
			if (x <= 25) {
				velX = 0;
				xtemp = 25;
			}
		}
		if (velX > 0) {
			if (topRight || bottomRight) {
				velX = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			} else {
				xtemp += velX;
			}
		}
		
		if (!falling) {
			calculateCorners(x, ydest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}

	private void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        
    	if (currRow < bottomTile) {
    		currRow = bottomTile - 1;
    	}
        
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
	}
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
				x + xmap - width > Constants.WIDTH ||
				y + ymap + height < 0 ||
				y + ymap - height > Constants.HEIGHT;
	}
	
	public void tick() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if (flinchDirection == 2) {
			velX -= flinchXSpeed;
			if (velX < -maxFlinchXSpeed) {
				velX = -maxFlinchXSpeed;
			}
		} else if (flinchDirection == 1) {
			velX += flinchXSpeed;
			if (velX > maxFlinchXSpeed) {
				velX = maxFlinchXSpeed;
			}
		}
		
		for(int i = 0; i < bloodLosses.size(); i++) {
			bloodLosses.get(i).tick();
			if(bloodLosses.get(i).shouldRemove()) {
				bloodLosses.remove(i);
				i--;
			}
		}
	}

	public void render(Graphics g) {
		setMapPosition();
		if (facingRight) {
			g.drawImage(animator.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null);
		} else {
			BufferedImage image = animator.getImage();
			g.drawImage(image,
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2),
					-image.getWidth(),
					image.getHeight(),
					null);
		}
		
		for(int i = 0; i < bloodLosses.size(); i++) {
			bloodLosses.get(i).setMapPosition(
				(int)tileMap.getX(), (int)tileMap.getY());
			bloodLosses.get(i).render(g);
		}
		
		animator.update(System.currentTimeMillis());
	}
	
	public void hit(int damage, boolean right, boolean shield) {
		if (dead || flinching || shielding) return;
		health -= damage;
		if (health < 0) health = 0;
		if (health == 0) dead = true;
		
		if (!shield) {
			flinching = true;
			flinchTimer = System.nanoTime();
			
			bloodLosses.add(
					new Blood((int)x, (int)y, 128, 128,  0, right));
			
			velY -= flinchYSpeed;
			if (right) {
				flinchDirection = 2;
			} else {
				flinchDirection = 1;
			}
		} else {
			shielding = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	public void shieldDamage(int shieldDamage, int damage, int cost, boolean right) {
		if (stamina < cost) {
			hit(damage, right, false);
		} else {
			if (!shielding) {
				stamina -= cost;
			}
			hit(shieldDamage, right, true);
		}
	}

}
