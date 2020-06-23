package jdc.kings.state.objects;

import java.lang.reflect.InvocationTargetException;

import jdc.kings.objects.Enemy;
import jdc.kings.view.TileMap;

public class EnemySpawner {
	
	private Class<? extends Enemy> enemy;
	private float x;
	private float y;
	private TileMap tileMap;
	private boolean spawned;
	
	public Class<? extends Enemy> getEnemy() {
		return enemy;
	}
	
	public void setEnemy(Class<? extends Enemy> enemy) {
		this.enemy = enemy;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public TileMap getTileMap() {
		return tileMap;
	}
	
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	public boolean hasSpawned() {
		return spawned;
	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public EnemySpawner(Class<? extends Enemy> enemy, float x, float y, TileMap tileMap) {
		super();
		this.enemy = enemy;
		this.x = x;
		this.y = y;
		this.tileMap = tileMap;
	}

	public EnemySpawner() {
		super();
	}
	
	public Enemy spawnEnemy() {
		Enemy instance = null;
		try {
			instance = enemy.getConstructor(TileMap.class).newInstance(tileMap);
			instance.setPosition(x, y);
			spawned = true;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}

}
