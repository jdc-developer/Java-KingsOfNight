package jdc.kings.objects;

import jdc.kings.view.TileMap;

public class Enemy extends GameObject {
	
	protected int damage;
	protected int shieldDamage;
	protected int shieldCost;
	
	protected float playerDistance;
	protected long turnAroundTimer;
	protected int sightDistance;
	
	protected Player player;

	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() {
		return dead;
	}

	public int getDamage() {
		return damage;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	protected void checkPlayerDamage() {
		if (intersects(player)) {
			if (player.isShield()) {
				
				long elapsed = (System.nanoTime() - player.getHoldTimer()) / 1000000;
				if ((!facingRight && player.isFacingRight()) || (facingRight && !player.isFacingRight()) &&
						(elapsed > 200)) {
					player.shieldDamage(shieldDamage, damage, shieldCost, !facingRight);
					flinching = true;
					flinchDirection = player.isFacingRight() ? 1 : 2;
					flinchTimer = System.nanoTime();
				} else {
					player.hit(damage, !facingRight, false);
				}
			} else if (player.isRolling()) {
				
				long elapsed = (System.nanoTime() - player.getRollTimer()) / 1000000;
				if (elapsed < 100) {
					player.hit(damage, !facingRight, false);
				}
			} else {
				player.hit(damage, !facingRight, false);
			}
		}
	}
	
	protected void playerPosition() {
		playerDistance = this.x - player.getX();
		if (playerDistance <= sightDistance  && playerDistance > 0 && !jumping) {
			long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
			if (elapsed > 900) {
				left = true;
				right = false;
				turnAroundTimer = System.nanoTime();
			}
		} else if (playerDistance >= -sightDistance && playerDistance < 0 && !jumping) {
			long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
			if (elapsed > 900) {
				right = true;
				left = false;
				turnAroundTimer = System.nanoTime();
			}
		}
	}

}
