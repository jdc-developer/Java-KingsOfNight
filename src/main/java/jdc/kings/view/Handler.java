package jdc.kings.view;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.utils.Constants;

public class Handler {

	private static Handler instance;
	private TileMap tileMap;
	private Player player;
	private LinkedList<Enemy> enemies = new LinkedList<>();
	private HUD hud;
	
	private Handler() {};
	
	public static Handler getInstance() {
		if (instance == null) {
			instance = new Handler();
		}
		return instance;
	}
	
	public void tick() {
		tileMap.setPosition(
				Constants.WIDTH / Constants.SCALE - player.getX(),
				Constants.HEIGHT / Constants.SCALE - player.getY());
		
		player.checkAttack(enemies);
		player.tick();
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.tick();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
			}
		}
	}
	
	public void render(Graphics g) {
		tileMap.render(g);
		player.render(g);
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.render(g);
		}
		
		hud.render(g);
	}

	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	public void setPlayer(Player player) {
		this.player = player;
		hud = new HUD(this.player);
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}
	
}
