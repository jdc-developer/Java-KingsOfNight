package jdc.kings.objects;

import jdc.kings.utils.AudioPlayer;
import jdc.kings.view.TileMap;

public class Enemy extends GameObject {
	
	protected int damage;
	protected int shieldDamage;
	protected int shieldCost;
	
	protected float playerXDistance;
	protected float playerYDistance;
	protected long turnAroundTimer;
	protected int sightXDistance;
	protected int sightYDistance;
	
	protected Player player;
	protected AudioPlayer hit;

	public Enemy(TileMap tm) {
		super(tm);
		hit = new AudioPlayer("/sfx/enemies/hit.mp3");
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
					hit.play();
					player.hit(damage, !facingRight, false);
				}
			} else if (player.isRolling()) {
				
				long elapsed = (System.nanoTime() - player.getRollTimer()) / 1000000;
				if (elapsed < 100) {
					hit.play();
					player.hit(damage, !facingRight, false);
				}
			} else {
				hit.play();
				player.hit(damage, !facingRight, false);
			}
		}
	}
	
	protected void playerPosition() {
		playerXDistance = this.x - player.getX();
		playerYDistance = this.y - player.getY();
		if (!player.isDead() && !flinching) {
			if (playerXDistance <= sightXDistance  && playerXDistance > 0 && !jumping &&
					(playerYDistance <= sightYDistance  && playerYDistance > 0 ||
					playerYDistance >= -sightYDistance  && playerYDistance < 0)) {
				long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
				if (elapsed > 900) {
					left = true;
					right = false;
					turnAroundTimer = System.nanoTime();
				}
			} else if (playerXDistance >= -sightXDistance && playerXDistance < 0 && !jumping &&
					(playerYDistance <= sightYDistance  && playerYDistance > 0 ||
					playerYDistance >= -sightYDistance  && playerYDistance < 0)) {
				long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
				if (elapsed > 900) {
					right = true;
					left = false;
					turnAroundTimer = System.nanoTime();
				}
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

}
