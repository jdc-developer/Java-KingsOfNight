package jdc.kings.state;

import java.util.concurrent.BlockingQueue;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.state.objects.EnemySpawner;

public class EnemyThread extends Thread {
	
	private Player player;
	private BlockingQueue<Enemy> enemyQueue;
	private EnemySpawner[] spawners;
	
	@Override
	public void run() {
		while(true) {
			for (int i = 0; i < spawners.length; i++) {
				EnemySpawner spawner = spawners[i];
				if (player.getX() - spawner.getX() <= 2000 && player.getX() - spawner.getX() >= -2000 && !spawner.hasSpawned()) {
					Enemy enemy = spawner.spawnEnemy();
					enemy.setPlayer(player);
					try {
						enemyQueue.put(enemy);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public EnemySpawner[] getSpawners() {
		return spawners;
	}

	public void setSpawners(EnemySpawner[] spawners) {
		this.spawners = spawners;
	}

	public EnemyThread(Player player, BlockingQueue<Enemy> enemyQueue) {
		super();
		this.player = player;
		this.enemyQueue = enemyQueue;
		this.spawners = LevelManager.getSpawners();
	}

}
