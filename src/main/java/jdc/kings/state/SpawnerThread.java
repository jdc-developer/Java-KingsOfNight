package jdc.kings.state;

import java.util.concurrent.BlockingQueue;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Item;
import jdc.kings.objects.Player;
import jdc.kings.state.objects.EnemySpawner;
import jdc.kings.state.objects.ItemSpawner;

public class SpawnerThread extends Thread {
	
	private volatile boolean running;
	private Player player;
	private BlockingQueue<Enemy> enemyQueue;
	private BlockingQueue<Item> itemQueue;
	
	private EnemySpawner[] enemySpawners;
	private ItemSpawner[] itemSpawners;
	
	@Override
	public void run() {
		running = true;
		while(running) {
			for (int i = 0; i < enemySpawners.length; i++) {
				EnemySpawner spawner = enemySpawners[i];
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
			
			for (int i = 0; i < itemSpawners.length; i++) {
				ItemSpawner spawner = itemSpawners[i];
				if (player.getX() - spawner.getX() <= 2000 && player.getX() - spawner.getX() >= -2000 && !spawner.hasSpawned()) {
					Item item = spawner.spawnItem();
					item.setPlayer(player);
					try {
						itemQueue.put(item);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(2500);
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

	public SpawnerThread(Player player, BlockingQueue<Enemy> enemyQueue, BlockingQueue<Item> itemQueue) {
		super();
		this.player = player;
		this.itemQueue = itemQueue;
		this.enemyQueue = enemyQueue;
		this.enemySpawners = LevelManager.getEnemySpawners();
		this.itemSpawners = LevelManager.getItemSpawners();
	}

}
