package jdc.kings.objects.enemies;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Arrow;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonArcher extends Enemy {
	
	private boolean firing;
	private boolean evading;
	private long randomTimer;
	
	private int fire;
	private int maxFire;
	private int fireCost;
	
	private List<Arrow> arrows = new ArrayList<>();
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int FIRING = 2;
	private static final int EVADING = 3;
	private static final int DYING = 4;

	public SkeletonArcher(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 1f;
		maxSpeed = 1.4f;
		fallSpeed = 0.2f;
		maxFallSpeed = 5f;
		jumpStart = -0.8f;
		maxJumpSpeed = 3.5f;
		stopJumpSpeed = 1.3f;
		flinchYSpeed = 5.5f;
		flinchXSpeed = 1f;
		maxFlinchXSpeed = 1.5f;
		
		width = 70;
		height = 90;
		cwidth = 35;
		cheight = 80;
		
		health = maxHealth = 20;
		damage = 7;
		fire = maxFire = 2500;
		fireCost = 200;
		arrows = new ArrayList<>();
		
		shieldDamage = 1;
		shieldCost = 4;
		
		sightDistance = 650;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 346, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 307, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/firing.png", this, 0, 5, 0, 2, 0, 0, 200, 283, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/evading.png", this, 0, 5, 0, 2, 0, 0, 200, 275, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/dying.png", this, 0, 6, 0, 1, 0, 0, 200, 301, 0, 0));
	
		animator = new Animator(sprites.get(0));
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
		
		if (currentAction == FIRING &&
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
	
	@Override
	public void tick() {
		if (!dead) {
			getNextPosition();
			playerPosition();
			checkPlayerDamage();
			super.tick();
		}
		
		fire += 1;
		if (fire > maxFire) fire = maxFire;
		if (firing && currentAction != FIRING) {
			if (fire > fireCost) {
				fire -= fireCost;
				animator.setFrames(sprites.get(FIRING));
				animator.setSpeed(100);
				Arrow arrow = new Arrow(tileMap, facingRight);
				arrow.setPosition(x, y);
				arrows.add(arrow);
			}
		}
		
		for (int i = 0; i < arrows.size(); i++) {
			arrows.get(i).tick();
			if (arrows.get(i).shouldRemove()) {
				arrows.remove(i);
				i--;
			}
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
			
			if (elapsed > 100) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (firing) {
				animator.setFrames(sprites.get(IDLE));
				firing = false;
			}
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
			}
		}
		
		if (currentAction == FIRING) {
			if (animator.hasPlayedOnce()) {
				firing = false;
			}
		}
		
		if (currentAction == EVADING) {
			if (animator.hasPlayedOnce()) {
				evading = false;
				maxSpeed = 1.4f;
			}
		}
		
		if (dead) {
			if (currentAction != DYING) {
				currentAction = DYING;
				animator.setFrames(sprites.get(DYING));
				animator.setSpeed(120);
				holdTimer = System.nanoTime();
			}
		} else if (evading && !firing) {
			if (currentAction != EVADING) {
				currentAction = EVADING;
				
				animator.setFrames(sprites.get(EVADING));
				animator.setSpeed(150);
				maxSpeed = 2f;
				jumping = true;
				
				if (facingRight) right = true;
				else left = true;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(80);
				width = 70;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(120);
				width = 70;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	@Override
	public void render(Graphics g) {
		for (int i = 0; i < arrows.size(); i++) {
			arrows.get(i).render(g);
		}
		super.render(g);
	}
	
	public void checkPlayerDamage() {
		super.checkPlayerDamage();
		if (firing && attack != null) {
			attack.checkAttack(this, player);
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 500) {
			
			if (playerDistance <= 100 && playerDistance > 0) {
				firing = true;
			} else if (playerDistance >= -100 && playerDistance < 0) {
				firing = true;
			}
		}
	}

}
