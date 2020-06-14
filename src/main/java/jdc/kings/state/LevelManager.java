package jdc.kings.state;

import jdc.kings.objects.Player;
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
		
		return new LevelState(tileMap, player, "/backgrounds/level1-bg.gif");
	}

}
