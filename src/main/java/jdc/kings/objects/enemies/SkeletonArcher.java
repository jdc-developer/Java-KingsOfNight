package jdc.kings.objects.enemies;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Arrow;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonArcher extends Enemy {
	
	private boolean firing;
	
	private List<Arrow> arrows = new ArrayList<>();
	private List<BufferedImage[]> sprites = new ArrayList<>();
	private Map<String, AudioPlayer> sfx = new HashMap<>();
	
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
		
		health = maxHealth = 20;
		stamina = maxStamina = 15;
		damage = 7;
		arrows = new ArrayList<>();
		
		shieldDamage = 1;
		shieldCost = 4;
		
		sightXDistance = 650;
		sightYDistance = 250;
		
		attacks.add(new Attack(4, 2, 3, 0, 4, 0, 0));
		
		sfx.put("arrow-throw", new AudioPlayer("/sfx/enemies/skeleton-archer/arrow-throw.mp3"));
		sfx.put("arrow-hit", new AudioPlayer("/sfx/enemies/skeleton-archer/arrow-hit.mp3"));
		sfx.put("skull-break", new AudioPlayer("/sfx/enemies/skull-break.mp3"));
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 346, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 307, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-archer/firing.png", this, 0, 5, 0, 2, 0, 0, 215, 283, 10, 0));
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
		if (!dying) {
			getNextPosition();
			playerPosition();
			checkPlayerDamage();
			super.tick();
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
				flinchTimer = 0;
			}
			
			if (elapsed > 200) {
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
				sfx.get("skull-break").play();
				animator.setFrames(sprites.get(DYING));
				animator.setSpeed(120);
				holdTimer = System.nanoTime();
			}
		} else if (firing) {
			attack = attacks.get(0);
			if (currentAction != FIRING && stamina >= attack.getCost()) {
				stamina -= attack.getCost();
				currentAction = FIRING;
				sfx.get("arrow-throw").play();
				
				animator.setFrames(sprites.get(FIRING));
				animator.setSpeed(100);
			}
		} else if ((left || right) && !corner) {
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
				attack.checkAttack(this, player, true, sfx.get("arrow-hit"));
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
		
		if (!player.isDead()) {
			if (playerXDistance <= 500 && playerXDistance > 0 &&
					(playerYDistance <= 500 && playerYDistance > 0 ||
					playerYDistance >= -250 && playerYDistance < 0)) {
				firing = true;
			} else if (playerXDistance >= -500 && playerXDistance < 0 &&
					(playerYDistance <= 500 && playerYDistance > 0 ||
					playerYDistance >= -250 && playerYDistance < 0)) {
				firing = true;
			}
		}
	}

}
