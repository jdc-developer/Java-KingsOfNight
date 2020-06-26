package jdc.kings.state;

import java.util.concurrent.BlockingQueue;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.state.objects.EnemySpawner;

public class SpawnerThread extends Thread {
	
	private Player player;
	private BlockingQueue<Enemy> enemyQueue;
	private EnemySpawner[] spawners;
	private volatile boolean running;
	
	@Override
	public void run() {
		running = true;
		while(running) {
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
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public SpawnerThread(Player player, BlockingQueue<Enemy> enemyQueue) {
		super();
		this.player = player;
		this.enemyQueue = enemyQueue;
		this.spawners = LevelManager.getSpawners();
	}

}
