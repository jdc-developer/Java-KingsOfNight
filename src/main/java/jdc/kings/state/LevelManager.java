package jdc.kings.state;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jdc.kings.Game;
import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.HellHound;
import jdc.kings.objects.enemies.Shadow;
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
		player.setPosition(17900, 50);
		
		SpiderBoss spiderBoss = new SpiderBoss(tileMap);
		spiderBoss.setPlayer(player);
		spiderBoss.setPosition(19500,  650);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String bossOne = BundleUtil.getMessageResourceString("bossOne", locale);
		BossState bossState = new BossState(spiderBoss, "/music/epic-battle.mp3", bossOne, 18500, 200);
		
		currentLevel = new LevelState(tileMap, player, "/backgrounds/level1-bg.gif", "/music/lurker-of-the-depths.mp3");
		currentLevel.getBossStates().add(bossState);
		List<Enemy> enemies = currentLevel.getEnemies();
		
		SkeletonKnight enemy1 = new SkeletonKnight(tileMap, 500, 650, player);
		SkeletonArcher enemy2 = new SkeletonArcher(tileMap, 700, 650, player);
		SkeletonKnight enemy3 = new SkeletonKnight(tileMap, 1500, 650, player);
		SkeletonKnight enemy4 = new SkeletonKnight(tileMap, 1200, 650, player);
		HellHound enemy5 = new HellHound(tileMap, 5500, 80, player);
		HellHound enemy6 = new HellHound(tileMap, 5100, 80, player);
		SkeletonArcher enemy7 = new SkeletonArcher(tileMap, 4900, 80, player);
		HellHound enemy8 = new HellHound(tileMap, 2500, 250, player);
		SkeletonKnight enemy9 = new SkeletonKnight(tileMap, 2800, 250, player);
		SkeletonKnight enemy10 = new SkeletonKnight(tileMap, 6300, 150, player);
		SkeletonKnight enemy11 = new SkeletonKnight(tileMap, 6600, 650, player);
		SkeletonArcher enemy12 = new SkeletonArcher(tileMap, 6900, 200, player);
		Shadow enemy13 = new Shadow(tileMap, 8000, 500, player);
		SkeletonArcher enemy14 = new SkeletonArcher(tileMap, 8300, 500, player);
		SkeletonKnight enemy15 = new SkeletonKnight(tileMap, 8200, 500, player);
		Shadow enemy16 = new Shadow(tileMap, 9500, 450, player);
		HellHound enemy17 = new HellHound(tileMap, 9300, 450, player);
		SkeletonArcher enemy18 = new SkeletonArcher(tileMap, 9400, 450, player);
		SkeletonArcher enemy19 = new SkeletonArcher(tileMap, 4700, 710, player);
		HellHound enemy20 = new HellHound(tileMap, 4600, 710, player);
		SkeletonKnight enemy21 = new SkeletonKnight(tileMap, 10800, 50, player);
		HellHound enemy22 = new HellHound(tileMap, 12500, 710, player);
		HellHound enemy23 = new HellHound(tileMap, 12700, 710, player);
		HellHound enemy24 = new HellHound(tileMap, 12900, 710, player);
		Shadow enemy25 = new Shadow(tileMap, 14000, 650, player);
		Shadow enemy26 = new Shadow(tileMap, 14200, 650, player);
		Shadow enemy27 = new Shadow(tileMap, 14400, 650, player);
		SkeletonKnight enemy28 = new SkeletonKnight(tileMap, 14600, 650, player);
		SkeletonArcher enemy29 = new SkeletonArcher(tileMap, 14700, 650, player);
		Shadow enemy30 = new Shadow(tileMap, 14900, 650, player);
		Shadow enemy31 = new Shadow(tileMap, 15000, 650, player);
		SkeletonArcher enemy32 = new SkeletonArcher(tileMap, 15200, 650, player);
		SkeletonKnight enemy33 = new SkeletonKnight(tileMap, 15100, 50, player);
		SkeletonKnight enemy34 = new SkeletonKnight(tileMap, 15000, 50, player);
		SkeletonKnight enemy35 = new SkeletonKnight(tileMap, 14700, 50, player);
		SkeletonKnight enemy36 = new SkeletonKnight(tileMap, 14500, 50, player);
		Shadow enemy37 = new Shadow(tileMap, 14300, 150, player);
		SkeletonArcher enemy38 = new SkeletonArcher(tileMap, 14200, 150, player);
		Shadow enemy39 = new Shadow(tileMap, 14000, 150, player);
		SkeletonArcher enemy40 = new SkeletonArcher(tileMap, 18500, 50, player);
		SkeletonArcher enemy41 = new SkeletonArcher(tileMap, 18600, 50, player);
		
		enemies.addAll(Arrays.asList(enemy1, enemy2, enemy3, enemy4, enemy5, enemy6, enemy7, enemy8, enemy9, enemy10,
				enemy11, enemy12, enemy13, enemy14, enemy15, enemy16, enemy17, enemy18, enemy19, enemy20, enemy21, enemy22,
				enemy23, enemy24, enemy25, enemy26, enemy27, enemy28, enemy29, enemy30, enemy31, enemy32, enemy33, enemy34,
				enemy35, enemy36, enemy37, enemy38, enemy39, enemy40, enemy41));
		
		return currentLevel;
	}

}
