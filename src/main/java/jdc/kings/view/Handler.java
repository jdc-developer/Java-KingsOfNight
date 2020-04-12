package jdc.kings.view;

import java.awt.Graphics;
import java.util.LinkedList;

import jdc.kings.objects.GameObject;

public class Handler {

	private LinkedList<GameObject> objects = new LinkedList<GameObject>();
	private static Handler instance;
	private TileMap tileMap;
	
	private Handler() {};
	
	public static Handler getInstance() {
		if (instance == null) {
			instance = new Handler();
		}
		return instance;
	}
	
	public void tick() {
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
	
}
