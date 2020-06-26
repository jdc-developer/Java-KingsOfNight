package jdc.kings.state;

import java.awt.Graphics;

import jdc.kings.objects.Enemy;
import jdc.kings.view.BossHUD;

public class BossState extends GameState {
	
	private Enemy boss;
	private BossHUD hud;
	private float x;
	private float y;
	private boolean running;
	
	private float reduceSound = 0;
	
	public BossState(Enemy boss, String musicPath, String bossName, float x, float y) {
		this.boss = boss;
		this.x = x;
		this.y = y;
		this.hud = new BossHUD(boss, bossName);
		bgMusic = "bossMusic";
		audioPlayer.loadAudio(bgMusic, musicPath);
	}
	
	public void start() {
		running = true;
		audioPlayer.loop(bgMusic);
	}
	
	@Override
	public void tick() {
		if (boss.getPlayer().isDead()) {
			reduceSound += -0.06f;
			if (reduceSound <= -25) {
				audioPlayer.close(bgMusic);
			} else {
				audioPlayer.reduceSound(bgMusic, reduceSound);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		hud.render(g);
	}
	
	public Enemy getBoss() {
		return boss;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isRunning() {
		return running;
	}

}
