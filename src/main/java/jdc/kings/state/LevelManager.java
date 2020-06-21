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
		player.setPosition(6500, 250);
		
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
		
		SkeletonKnight skeleton2 = new SkeletonKnight(tileMap);
		skeleton2.setPosition(1500, 650);
		skeleton2.setPlayer(player);
		
		SkeletonKnight skeleton3 = new SkeletonKnight(tileMap);
		skeleton3.setPosition(1200, 650);
		skeleton3.setPlayer(player);
		
		HellHound hellhound = new HellHound(tileMap);
		hellhound.setPlayer(player);
		hellhound.setPosition(5500, 80);
		
		HellHound hellhound2 = new HellHound(tileMap);
		hellhound2.setPlayer(player);
		hellhound2.setPosition(5100, 80);
		
		SkeletonArcher skeleton4 = new SkeletonArcher(tileMap);
		skeleton4.setPosition(4900, 80);
		skeleton4.setPlayer(player);
		
		HellHound hellhound3 = new HellHound(tileMap);
		hellhound3.setPlayer(player);
		hellhound3.setPosition(2500, 250);
		
		SkeletonKnight skeleton5 = new SkeletonKnight(tileMap);
		skeleton5.setPosition(2800, 250);
		skeleton5.setPlayer(player);
		
		SkeletonKnight skeleton6 = new SkeletonKnight(tileMap);
		skeleton6.setPosition(6300, 150);
		skeleton6.setPlayer(player);
		
		SkeletonKnight skeleton7 = new SkeletonKnight(tileMap);
		skeleton7.setPosition(6600, 650);
		skeleton7.setPlayer(player);
		
		SkeletonArcher skeleton8 = new SkeletonArcher(tileMap);
		skeleton8.setPosition(6900, 200);
		skeleton8.setPlayer(player);
		
		currentLevel.getEnemies().addAll(Arrays.asList(hellhound, hellhound2, hellhound3, skeleton, skeleton1, skeleton2,
				skeleton3, skeleton4, skeleton5, skeleton6, skeleton7, skeleton8));
		return currentLevel;
	}

}
