package jdc.kings.objects.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Arrow;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonArcher extends Enemy {
	
	private boolean firing;
	private boolean hasFlinched;
	
	private List<Arrow> arrows = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int FIRING = 2;
	private static final int DYING = 3;

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
		flinchXSpeed = 0.5f;
		maxFlinchXSpeed = 0.8f;
		flinchYSpeed = 5.5f;
		maxFlinchYSpeed = 6f;
		
		width = 70;
		height = 90;
		cwidth = 35;
		cheight = 80;
		
		health = maxHealth = 195;
		stamina = maxStamina = 150;
		damage = 53.5f;
		arrows = new ArrayList<>();
		
		shieldDamage = 12;
		shieldCost = 20.8f;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		attacks.add(new Attack(43.5f, 23.2f, 30.1f, 0, 50.6f, 0, 0));
		
		audioPlayer.loadAudio("skeleton-archer-arrow-throw", "/sfx/enemies/skeleton-archer/arrow-throw.mp3");
		audioPlayer.loadAudio("skeleton-archer-arrow-hit", "/sfx/enemies/skeleton-archer/arrow-hit.mp3");
		audioPlayer.loadAudio("skull-break", "/sfx/enemies/skull-break.mp3");
		
		if (spriteLoader.getSprites("skeleton-archer") == null) {
			BufferedImage[][] sprites = new BufferedImage[4][];
			
			sprites[0] = spriteLoader.loadAction("/sprites/enemies/skeleton-archer/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 346, 0, 0);
			sprites[1] = spriteLoader.loadAction("/sprites/enemies/skeleton-archer/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 307, 0, 0);
			sprites[2] = spriteLoader.loadAction("/sprites/enemies/skeleton-archer/firing.png", this, 0, 5, 0, 2, 0, 0, 215, 283, 10, 0);
			sprites[3] = spriteLoader.loadAction("/sprites/enemies/skeleton-archer/dying.png", this, 0, 6, 0, 1, 0, 0, 200, 301, 0, 0);
		
			spriteLoader.loadSprites("skeleton-archer", sprites);
		}
	
		animator = new Animator(spriteLoader.getAction("skeleton-archer", IDLE));
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
		
		if (stamina < maxStamina) {
			stamina += 0.4f;
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
		
		for (int i = 0; i < arrows.size(); i++) {
			Arrow arrow = arrows.get(i);
			arrow.tick();
			
			if (x - arrow.getX() >= 1590 || x - arrow.getX() <= -1590) {
				arrow.shouldRemove(true);
			}
			if (arrows.get(i).shouldRemove()) {
				arrows.remove(i);
				i--;
			}
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 200) {
				flinching = false;
				flinchTimer = System.nanoTime();
				hasFlinched = true;
			}
			
			if (elapsed > 200) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (firing) {
				animator.setFrames(spriteLoader.getAction("skeleton-archer", IDLE));
				firing = false;
			}
		}
		
		long flinchedElapsed = (System.nanoTime() - flinchTimer) / 1000000;
		if (hasFlinched && flinchedElapsed > 800) {
			hasFlinched = false;
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
				dead = true;
			}
		}
		
		if (currentAction == FIRING) {
			if (animator.hasPlayedOnce()) {
				firing = false;
				Arrow arrow = new Arrow(tileMap, facingRight);
				arrow.setPosition(x, y - 10);
				arrows.add(arrow);
			}
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				audioPlayer.play("skull-break");
				animator.setFrames(spriteLoader.getAction("skeleton-archer", DYING));
				animator.setSpeed(120);
				holdTimer = System.nanoTime();
				left = right = false;
				velX = 0;
			}
		} else if (firing) {
			attack = attacks.get(0);
			if (currentAction != FIRING && stamina >= attack.getCost()) {
				stamina -= attack.getCost();
				currentAction = FIRING;
				audioPlayer.play("skeleton-archer-arrow-throw");
				
				animator.setFrames(spriteLoader.getAction("skeleton-archer", FIRING));
				animator.setSpeed(100);
			}
		} else if ((left || right) && !corner) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(spriteLoader.getAction("skeleton-archer", WALKING));
				animator.setSpeed(80);
				width = 70;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(spriteLoader.getAction("skeleton-archer", IDLE));
				animator.setSpeed(120);
				width = 70;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	@Override
	public void render(Graphics2D g) {
		for (int i = 0; i < arrows.size(); i++) {
			arrows.get(i).render(g);
			if (dead) {
				arrows.remove(i);
				i--;
			}
		}
		super.render(g);
	}
	
	public void checkPlayerDamage() {
		super.checkPlayerDamage();
		for (int j = 0; j < arrows.size(); j++) {
			if (arrows.get(j).intersects(player) && !player.isDead()) {
				attack.checkAttack(this, player, true, "skeleton-archer-arrow-hit", 0);
				if (player.isRolling()) {
					
					long rollElapsed = (System.nanoTime() - player.getRollTimer()) / 1000000;
					if (rollElapsed < 100) {
						arrows.get(j).setHit();
					}
				} else {
					arrows.get(j).setHit();
				}
				break;
			}
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		
		if (!player.isDead() && !hasFlinched) {
			if (playerXDistance <= 500 && playerXDistance >= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				firing = true;
			} else if (playerXDistance >= -500 && playerXDistance <= 0 &&
					(playerYDistance <= sightYDistance && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance && playerYDistance <= 0)) {
				firing = true;
			}
		}
	}

}
