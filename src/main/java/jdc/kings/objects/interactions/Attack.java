package jdc.kings.objects.interactions;

import jdc.kings.objects.GameObject;
import jdc.kings.utils.AudioPlayer;

public class Attack {
	
	private float damage;
	private float shieldDamage;
	private float shieldCost;
	private float range;
	private float cost;
	private int startTime;
	private int endTime;
	private long timer;
	
	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getShieldDamage() {
		return shieldDamage;
	}
	
	public void setShieldDamage(float shieldDamage) {
		this.shieldDamage = shieldDamage;
	}
	
	public float getShieldCost() {
		return shieldCost;
	}
	
	public void setShieldCost(float shieldCost) {
		this.shieldCost = shieldCost;
	}
	
	public float getRange() {
		return range;
	}
	
	public void setRange(float range) {
		this.range = range;
	}
	
	public float getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public Attack(float damage, float shieldDamage, float shieldCost, float range, float cost, int startTime, int endTime) {
		super();
		this.damage = damage;
		this.shieldDamage = shieldDamage;
		this.shieldCost = shieldCost;
		this.range = range;
		this.cost = cost;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void checkAttack(GameObject attacking, GameObject attacked, boolean ranged, String hitClip, float bonus) {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if ((elapsed > startTime && elapsed < endTime && !attacked.isDead()) || ranged) {
			
			if (attacked.isShield()) {
				
				long shieldElapsed = (System.nanoTime() - attacked.getHoldTimer()) / 1000000;
				if ((!attacking.isFacingRight() && attacked.isFacingRight()) || (attacking.isFacingRight() && !attacked.isFacingRight()) &&
						(shieldElapsed > 200)) {
					attacked.shieldDamage(shieldDamage, damage, shieldCost, !attacking.isFacingRight());
				} else {
					consumateAttack(attacking, attacked, ranged, hitClip, bonus);
				}
			} else if (attacked.isRolling()) {
				
				long rollElapsed = (System.nanoTime() - attacked.getRollTimer()) / 1000000;
				if (rollElapsed < 100) {
					consumateAttack(attacking, attacked, ranged, hitClip, bonus);
				}
			} else {
				consumateAttack(attacking, attacked, ranged, hitClip, bonus);
			}
		}
	}
	
	private void consumateAttack(GameObject attacking, GameObject attacked, boolean ranged, String hitClip, float bonus) {
		AudioPlayer audioPlayer = AudioPlayer.getInstance();
		if (attacking.isFacingRight()) {
			 if (
					 attacked.getX() > attacking.getX() &&
					 attacked.getX() < attacking.getX() + range &&
					 attacked.getY() > attacking.getY() - attacking.getHeight() / 2 &&
					 attacked.getY() < attacking.getY() + attacking.getHeight() / 2) {
				 
				 attacked.hit(damage + bonus, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 } else if (ranged) {
				 attacked.hit(damage + bonus, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 }
		 } else {
			 if (
					 attacked.getX() < attacking.getX() &&
					 attacked.getX() > attacking.getX() - range &&
					 attacked.getY() > attacking.getY() - attacking.getHeight() / 2 &&
					 attacked.getY() < attacking.getY() + attacking.getHeight() / 2) {
				 
				 attacked.hit(damage + bonus, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 } else if (ranged) {
				 attacked.hit(damage + bonus, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 }
		 }
	}

}
