package jdc.kings.state;

import java.util.LinkedList;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.state.objects.EnemySpawner;

public class EnemyThread extends Thread {
	
	private Player player;
	private LinkedList<Enemy> enemies;
	private EnemySpawner[] spawners;
	private boolean running = false;
	
	@Override
	public void run() {
		running = true;
		while(running) {
			for (int i = 0; i < spawners.length; i++) {
				EnemySpawner spawner = spawners[i];
				if (player.getX() - spawner.getX() <= 2000 && player.getX() - spawner.getX() >= -2000 && !spawner.hasSpawned()) {
					Enemy enemy = spawner.spawnEnemy();
					enemy.setPlayer(player);
					enemies.add(enemy);
				}
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public LinkedList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(LinkedList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public EnemySpawner[] getSpawners() {
		return spawners;
	}

	public void setSpawners(EnemySpawner[] spawners) {
		this.spawners = spawners;
	}

	public boolean isRunning() {
		return running;
	}

	public EnemyThread(Player player, LinkedList<Enemy> enemies) {
		super();
		this.player = player;
		this.enemies = enemies;
		this.spawners = LevelManager.getSpawners();
	}

}
