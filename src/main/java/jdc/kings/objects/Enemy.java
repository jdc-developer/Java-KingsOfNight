package jdc.kings.objects;

import jdc.kings.view.TileMap;

public abstract class Enemy extends GameObject {
	
	protected float damage;
	protected float shieldDamage;
	protected float shieldCost;
	protected boolean corner;
	
	protected float playerXDistance;
	protected float playerYDistance;
	protected long turnAroundTimer;
	protected int sightXDistance;
	protected int sightYDistance;
	
	protected Player player;
	protected String hitClip;

	public Enemy(TileMap tm) {
		super(tm);
		hitClip = "enemy-hit";
		audioPlayer.loadAudio(hitClip, "/sfx/enemies/hit.mp3");
	}
	
	protected void checkPlayerDamage() {
		if (intersects(player) && !dying && !dead) {
			if (player.isShield()) {
				
				long elapsed = (System.nanoTime() - player.getHoldTimer()) / 1000000;
				if ((!facingRight && player.isFacingRight()) || (facingRight && !player.isFacingRight()) &&
						(elapsed > 200)) {
					player.shieldDamage(shieldDamage, damage, shieldCost, !facingRight);
					flinching = true;
					flinchDirection = player.isFacingRight() ? 1 : 2;
					flinchTimer = System.nanoTime();
				} else {
					audioPlayer.play(hitClip);
					player.hit(damage, !facingRight, false);
				}
			} else if (player.isRolling()) {
				
				long elapsed = (System.nanoTime() - player.getRollTimer()) / 1000000;
				if (elapsed < 100) {
					audioPlayer.play(hitClip);
					player.hit(damage, !facingRight, false);
				}
			} else {
				audioPlayer.play(hitClip);
				player.hit(damage, !facingRight, false);
			}
		}
	}
	
	protected void playerPosition() {
		playerXDistance = this.x - player.getX();
		playerYDistance = this.y - player.getY();
		
		if (!player.isDead() && !flinching) {
			if (playerXDistance <= sightXDistance  && playerXDistance >= 0 && !jumping &&
					(playerYDistance <= sightYDistance  && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance  && playerYDistance <= 0)) {
				long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
				if (elapsed > 900) {
					left = true;
					right = false;
					turnAroundTimer = System.nanoTime();
				}
			} else if (playerXDistance >= -sightXDistance && playerXDistance <= 0 && !jumping &&
					(playerYDistance <= sightYDistance  && playerYDistance >= 0 ||
					playerYDistance >= -sightYDistance  && playerYDistance <= 0)) {
				long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
				if (elapsed > 900) {
					right = true;
					left = false;
					turnAroundTimer = System.nanoTime();
				}
			}
		}
		
		if (((left && !bottomLeft) || (right && !bottomRight)) && (!jumping && !falling && !flinching)) {
			velX = 0;
			corner = true;
		} else {
			corner = false;
		}
	}
	
	public float getDamage() {
		return damage;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}
