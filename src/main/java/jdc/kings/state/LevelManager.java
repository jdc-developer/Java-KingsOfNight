package jdc.kings.state;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jdc.kings.Game;
import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.HellHound;
import jdc.kings.objects.enemies.Shadow;
import jdc.kings.objects.enemies.SkeletonArcher;
import jdc.kings.objects.enemies.SkeletonKnight;
import jdc.kings.objects.enemies.bosses.SpiderBoss;
import jdc.kings.state.objects.EnemySpawner;
import jdc.kings.utils.BundleUtil;
import jdc.kings.view.TileMap;

public abstract class LevelManager {
	
	private static LevelState currentLevel;
	private static EnemySpawner[] spawners;
	
	public static LevelState loadLevelOne() {
		TileMap tileMap = new TileMap(32, 20);
		int[] normal = {7, 37, 31, 25, 1};
		
		tileMap.loadTiles("/tilesets/dawn-of-the-gods-1.png", normal);
		tileMap.loadMap("/maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		Player player = new Player(tileMap);
		player.setPosition(100, 650);
		
		spawners = new EnemySpawner[0];
		/*
		spawners[0] = new EnemySpawner(SkeletonKnight.class, 500, 650, tileMap);
		spawners[1] = new EnemySpawner(SkeletonArcher.class, 700, 650, tileMap);
		spawners[2] = new EnemySpawner(Shadow.class, 9500, 450, tileMap);
		spawners[3] = new EnemySpawner(SkeletonKnight.class, 8200, 500, tileMap);
		spawners[4] = new EnemySpawner(SkeletonArcher.class, 8300, 500, tileMap);
		spawners[5] = new EnemySpawner(Shadow.class, 8400, 500, tileMap);
		spawners[6] = new EnemySpawner(SkeletonArcher.class, 6900, 200, tileMap);
		spawners[7] = new EnemySpawner(SkeletonKnight.class, 6600, 650, tileMap);
		spawners[8] = new EnemySpawner(SkeletonKnight.class, 6300, 150, tileMap);
		spawners[9] = new EnemySpawner(SkeletonKnight.class, 2800, 250, tileMap);
		spawners[10] = new EnemySpawner(HellHound.class, 2500, 250, tileMap);
		spawners[11] = new EnemySpawner(SkeletonArcher.class, 4900, 80, tileMap);
		spawners[12] = new EnemySpawner(HellHound.class, 5100, 80, tileMap);
		spawners[13] = new EnemySpawner(HellHound.class, 5500, 80, tileMap);
		spawners[14] = new EnemySpawner(SkeletonKnight.class, 1200, 650, tileMap);
		spawners[15] = new EnemySpawner(SkeletonKnight.class, 1500, 650, tileMap);
		spawners[16] = new EnemySpawner(SkeletonArcher.class, 9400, 450, tileMap);
		spawners[17] = new EnemySpawner(HellHound.class, 9300, 450, tileMap);
		spawners[18] = new EnemySpawner(SkeletonArcher.class, 4700, 710, tileMap);
		spawners[19] = new EnemySpawner(HellHound.class, 4600, 710, tileMap);
		spawners[20] = new EnemySpawner(SkeletonKnight.class, 10800, 50, tileMap);
		spawners[21] = new EnemySpawner(HellHound.class, 12500, 710, tileMap);
		spawners[22] = new EnemySpawner(HellHound.class, 12700, 710, tileMap);
		spawners[23] = new EnemySpawner(HellHound.class, 12900, 710, tileMap);
		spawners[24] = new EnemySpawner(Shadow.class, 14000, 650, tileMap);
		spawners[25] = new EnemySpawner(Shadow.class, 14200, 650, tileMap);
		spawners[26] = new EnemySpawner(Shadow.class, 14400, 650, tileMap);
		spawners[27] = new EnemySpawner(SkeletonKnight.class, 14600, 650, tileMap);
		spawners[28] = new EnemySpawner(SkeletonArcher.class, 14700, 650, tileMap);
		spawners[29] = new EnemySpawner(Shadow.class, 14900, 650, tileMap);
		spawners[30] = new EnemySpawner(Shadow.class, 15000, 650, tileMap);
		spawners[31] = new EnemySpawner(SkeletonArcher.class, 15200, 50, tileMap);
		spawners[32] = new EnemySpawner(SkeletonKnight.class, 15100, 50, tileMap);
		spawners[33] = new EnemySpawner(SkeletonKnight.class, 15000, 50, tileMap);
		spawners[34] = new EnemySpawner(SkeletonKnight.class, 14700, 50, tileMap);
		spawners[35] = new EnemySpawner(SkeletonKnight.class, 14500, 50, tileMap);
		spawners[36] = new EnemySpawner(Shadow.class, 14300, 150, tileMap);
		spawners[37] = new EnemySpawner(SkeletonArcher.class, 14200, 150, tileMap);
		spawners[38] = new EnemySpawner(Shadow.class, 14000, 150, tileMap);
		spawners[39] = new EnemySpawner(SkeletonArcher.class, 18500, 50, tileMap);
		spawners[40] = new EnemySpawner(SkeletonArcher.class, 18600, 50, tileMap);*/
		
		BlockingQueue<Enemy> enemyQueue = new ArrayBlockingQueue<Enemy>(100);
		SpawnerThread spawnerThread = new SpawnerThread(player, enemyQueue);
		
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
		currentLevel.setSpawnerThread(spawnerThread);
		return currentLevel;
	}

	public static LevelState getCurrentLevel() {
		return currentLevel;
	}

	public static EnemySpawner[] getSpawners() {
		return spawners;
	}
	
}
