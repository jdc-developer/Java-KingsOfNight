package jdc.kings.state;

import java.util.Arrays;

import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.SkeletonArcher;
import jdc.kings.objects.enemies.SkeletonKnight;
import jdc.kings.view.TileMap;

public abstract class LevelManager {
	
	public static LevelState loadLevelOne() {
		TileMap tileMap = new TileMap(32, 20);
		int[] normal = {7, 37, 31, 25, 1};
		
		tileMap.loadTiles("/tilesets/dawn-of-the-gods-1.png", normal);
		tileMap.loadMap("/maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		Player player = new Player(tileMap);
		player.setPosition(100, 650);
		
		LevelState levelOne = new LevelState(tileMap, player, "/backgrounds/level1-bg.gif");
		
		SkeletonKnight skeleton = new SkeletonKnight(tileMap);
		skeleton.setPosition(500, 650);
		skeleton.setPlayer(player);
		
		SkeletonArcher skeleton1 = new SkeletonArcher(tileMap);
		skeleton1.setPosition(700, 650);
		skeleton1.setPlayer(player);
		
		levelOne.getEnemies().addAll(Arrays.asList(skeleton, skeleton1));
		return levelOne;
	}

}
