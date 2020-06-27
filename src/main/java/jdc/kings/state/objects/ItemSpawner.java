package jdc.kings.state.objects;

import java.lang.reflect.InvocationTargetException;

import jdc.kings.objects.Item;
import jdc.kings.view.TileMap;

public class ItemSpawner {
	
	private Class<? extends Item> item;
	private float x;
	private float y;
	private TileMap tileMap;
	private boolean spawned;

	public Class<? extends Item> getItem() {
		return item;
	}

	public void setItem(Class<? extends Item> item) {
		this.item = item;
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

	public ItemSpawner(Class<? extends Item> item, float x, float y, TileMap tileMap) {
		super();
		this.item = item;
		this.x = x;
		this.y = y;
		this.tileMap = tileMap;
	}

	public ItemSpawner() {
		super();
	}
	
	public Item spawnItem() {
		Item instance = null;
		try {
			instance = item.getConstructor(TileMap.class).newInstance(tileMap);
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
