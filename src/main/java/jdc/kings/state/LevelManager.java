package jdc.kings.state;

import java.util.Locale;

import jdc.kings.Game;
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
	
	public static LevelState loadLevelOne() {
		TileMap tileMap = new TileMap(32, 20);
		int[] normal = {7, 37, 31, 25, 1};
		
		tileMap.loadTiles("/tilesets/dawn-of-the-gods-1.png", normal);
		tileMap.loadMap("/maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		Player player = new Player(tileMap);
		player.setPosition(100, 650);
		
		SpiderBoss spiderBoss = new SpiderBoss(tileMap);
		spiderBoss.setPlayer(player);
		spiderBoss.setPosition(19500,  650);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String bossOne = BundleUtil.getMessageResourceString("bossOne", locale);
		BossState bossState = new BossState(spiderBoss, "/music/epic-battle.mp3", bossOne, 18500, 200);
		
		currentLevel = new LevelState(tileMap, player, "/backgrounds/level1-bg.gif", "/music/lurker-of-the-depths.mp3");
		currentLevel.getBossStates().add(bossState);
		currentLevel.setSpawners(new EnemySpawner[18]);
		EnemySpawner[] spawners = currentLevel.getSpawners();
		
		spawners[0] = new EnemySpawner(SkeletonArcher.class, 9400, 450, tileMap);
		spawners[1] = new EnemySpawner(HellHound.class, 9300, 450, tileMap);
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
		spawners[16] = new EnemySpawner(SkeletonArcher.class, 700, 650, tileMap);
		spawners[17] = new EnemySpawner(SkeletonKnight.class, 500, 650, tileMap);
		
		return currentLevel;
	}

}
