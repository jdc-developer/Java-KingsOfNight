package jdc.kings.state;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jdc.kings.Game;
import jdc.kings.objects.Enemy;
import jdc.kings.objects.Item;
import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.SkeletonKnight;
import jdc.kings.objects.enemies.bosses.SpiderBoss;
import jdc.kings.objects.items.HealthPotion;
import jdc.kings.objects.items.StaminaPotion;
import jdc.kings.state.objects.EnemySpawner;
import jdc.kings.state.objects.ItemSpawner;
import jdc.kings.utils.BundleUtil;
import jdc.kings.view.TileMap;

public abstract class LevelManager {
	
	private static LevelState currentLevel;
	private static EnemySpawner[] enemySpawners;
	private static ItemSpawner[] itemSpawners;
	
	public static LevelState loadLevelOne() {
		TileMap tileMap = new TileMap(32, 20);
		int[] normal = {7, 37, 31, 25, 1};
		
		tileMap.loadTiles("/tilesets/dawn-of-the-gods.png", normal);
		tileMap.loadMap("/maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		Player player = new Player(tileMap);
		player.setPosition(100, 650);
		
		enemySpawners = new EnemySpawner[1];
		
		enemySpawners[0] = new EnemySpawner(SkeletonKnight.class, 500, 650, tileMap);
		/*enemySpawners[1] = new EnemySpawner(SkeletonArcher.class, 700, 650, tileMap);
		enemySpawners[2] = new EnemySpawner(Shadow.class, 9500, 450, tileMap);
		enemySpawners[3] = new EnemySpawner(SkeletonKnight.class, 8200, 500, tileMap);
		enemySpawners[4] = new EnemySpawner(SkeletonArcher.class, 8300, 500, tileMap);
		enemySpawners[5] = new EnemySpawner(Shadow.class, 8400, 500, tileMap);
		enemySpawners[6] = new EnemySpawner(SkeletonArcher.class, 6900, 200, tileMap);
		enemySpawners[7] = new EnemySpawner(SkeletonKnight.class, 6600, 650, tileMap);
		enemySpawners[8] = new EnemySpawner(SkeletonKnight.class, 6300, 150, tileMap);
		enemySpawners[9] = new EnemySpawner(SkeletonKnight.class, 2800, 250, tileMap);
		enemySpawners[10] = new EnemySpawner(HellHound.class, 2500, 250, tileMap);
		enemySpawners[11] = new EnemySpawner(SkeletonArcher.class, 4900, 80, tileMap);
		enemySpawners[12] = new EnemySpawner(HellHound.class, 5100, 80, tileMap);
		enemySpawners[13] = new EnemySpawner(HellHound.class, 5500, 80, tileMap);
		enemySpawners[14] = new EnemySpawner(SkeletonKnight.class, 1200, 650, tileMap);
		enemySpawners[15] = new EnemySpawner(SkeletonKnight.class, 1500, 650, tileMap);
		enemySpawners[16] = new EnemySpawner(SkeletonArcher.class, 9400, 450, tileMap);
		enemySpawners[17] = new EnemySpawner(HellHound.class, 9300, 450, tileMap);
		enemySpawners[18] = new EnemySpawner(SkeletonArcher.class, 4700, 710, tileMap);
		enemySpawners[19] = new EnemySpawner(HellHound.class, 4600, 710, tileMap);
		enemySpawners[20] = new EnemySpawner(SkeletonKnight.class, 10800, 50, tileMap);
		enemySpawners[21] = new EnemySpawner(HellHound.class, 12500, 710, tileMap);
		enemySpawners[22] = new EnemySpawner(HellHound.class, 12700, 710, tileMap);
		enemySpawners[23] = new EnemySpawner(HellHound.class, 12900, 710, tileMap);
		enemySpawners[24] = new EnemySpawner(Shadow.class, 14000, 650, tileMap);
		enemySpawners[25] = new EnemySpawner(Shadow.class, 14200, 650, tileMap);
		enemySpawners[26] = new EnemySpawner(Shadow.class, 14400, 650, tileMap);
		enemySpawners[27] = new EnemySpawner(SkeletonKnight.class, 14600, 650, tileMap);
		enemySpawners[28] = new EnemySpawner(SkeletonArcher.class, 14700, 650, tileMap);
		enemySpawners[29] = new EnemySpawner(Shadow.class, 14900, 650, tileMap);
		enemySpawners[30] = new EnemySpawner(Shadow.class, 15000, 650, tileMap);
		enemySpawners[31] = new EnemySpawner(SkeletonArcher.class, 15200, 50, tileMap);
		enemySpawners[32] = new EnemySpawner(SkeletonKnight.class, 15100, 50, tileMap);
		enemySpawners[33] = new EnemySpawner(SkeletonKnight.class, 15000, 50, tileMap);
		enemySpawners[34] = new EnemySpawner(SkeletonKnight.class, 14700, 50, tileMap);
		enemySpawners[35] = new EnemySpawner(SkeletonKnight.class, 14500, 50, tileMap);
		enemySpawners[36] = new EnemySpawner(Shadow.class, 14300, 150, tileMap);
		enemySpawners[37] = new EnemySpawner(SkeletonArcher.class, 14200, 150, tileMap);
		enemySpawners[38] = new EnemySpawner(Shadow.class, 14000, 150, tileMap);
		enemySpawners[39] = new EnemySpawner(SkeletonArcher.class, 18500, 50, tileMap);
		enemySpawners[40] = new EnemySpawner(SkeletonArcher.class, 18600, 50, tileMap);*/
		
		itemSpawners = new ItemSpawner[10];
		
		for (int i = 0; i < 5; i++) {
			itemSpawners[i] = new ItemSpawner(HealthPotion.class, 200, 650, tileMap);
		}
		
		for (int i = 5; i < 10; i++) {
			itemSpawners[i] = new ItemSpawner(StaminaPotion.class, 200, 650, tileMap);
		}
		
		BlockingQueue<Enemy> enemyQueue = new ArrayBlockingQueue<Enemy>(100);
		BlockingQueue<Item> itemQueue = new ArrayBlockingQueue<Item>(100);
		SpawnerThread spawnerThread = new SpawnerThread(player, enemyQueue, itemQueue);
		
		ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(spawnerThread);
        
        SpiderBoss spiderBoss = new SpiderBoss(tileMap);
		spiderBoss.setPlayer(player);
		spiderBoss.setPosition(19500,  650);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String bossOne = BundleUtil.getMessageResourceString("bossOne", locale);
		BossState bossState = new BossState(spiderBoss, "/music/epic-battle.mp3", bossOne, 18500, 200);
		
		currentLevel = new LevelState(tileMap, player, "/backgrounds/level1-bg.gif", "/music/lurker-of-the-depths.mp3");
		currentLevel.getBossStates().add(bossState);
		currentLevel.setEnemyQueue(enemyQueue);
		currentLevel.setItemQueue(itemQueue);
		currentLevel.setSpawnerThread(spawnerThread);
		return currentLevel;
	}

	public static LevelState getCurrentLevel() {
		return currentLevel;
	}

	public static EnemySpawner[] getEnemySpawners() {
		return enemySpawners;
	}

	public static ItemSpawner[] getItemSpawners() {
		return itemSpawners;
	}
	
}
