package jdc.kings.state;

import java.util.Arrays;
import java.util.Locale;

import jdc.kings.Game;
import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.HellHound;
import jdc.kings.objects.enemies.SkeletonArcher;
import jdc.kings.objects.enemies.SkeletonKnight;
import jdc.kings.objects.enemies.bosses.SpiderBoss;
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
		
		SkeletonKnight skeleton = new SkeletonKnight(tileMap);
		skeleton.setPosition(500, 650);
		skeleton.setPlayer(player);
		
		SkeletonArcher skeleton1 = new SkeletonArcher(tileMap);
		skeleton1.setPosition(700, 650);
		skeleton1.setPlayer(player);
		
		HellHound hellhound = new HellHound(tileMap);
		hellhound.setPlayer(player);
		hellhound.setPosition(1000, 650);
		
		currentLevel.getEnemies().addAll(Arrays.asList(hellhound, skeleton, skeleton1));
		return currentLevel;
	}

}
