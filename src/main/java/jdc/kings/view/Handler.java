package jdc.kings.view;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.GameObject;
import jdc.kings.objects.Player;
import jdc.kings.utils.Constants;

public class Handler {

	private LinkedList<GameObject> objects = new LinkedList<GameObject>();
	private static Handler instance;
	private TileMap tileMap;
	private Player player;
	private List<Enemy> enemies = new ArrayList<>();
	
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
		for (int i = 0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) {
		tileMap.render(g);
		for (int i = 0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.render(g);
		}
	}
	
	public void addObject(GameObject object) {
		this.objects.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.objects.remove(object);
	}

	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}
	
}
