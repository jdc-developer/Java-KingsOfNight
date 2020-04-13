package jdc.kings.objects;

import jdc.kings.view.TileMap;

public class Enemy extends GameObject {
	
	protected int damage;
	protected int shieldDamage;
	protected int shieldCost;
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

	@Override
	public void tick() { }

}
