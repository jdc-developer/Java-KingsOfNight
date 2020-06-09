package jdc.kings.view;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.interactions.Blood;
import jdc.kings.utils.Constants;

public class Handler {

	private static Handler instance;
	private TileMap tileMap;
	private Background background;
	private Player player;
	
	private LinkedList<Enemy> enemies = new LinkedList<>();
	private LinkedList<Blood> bloodLosses = new LinkedList<>();
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
		
		background.setPosition(tileMap.getX(), tileMap.getY());
		
		player.checkAttack(enemies);
		player.tick();
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.isDead()) {
				if (e.bleeds()) {
					bloodLosses.add(
							new Blood((int)e.getX(), (int)e.getY(), 132, 78, 2, !player.isFacingRight()));
					enemies.remove(i);
					i--;
				}
			} else {
				e.tick();
			}
		}
		
		for(int i = 0; i < bloodLosses.size(); i++) {
			bloodLosses.get(i).tick();
			if(bloodLosses.get(i).shouldRemove()) {
				bloodLosses.remove(i);
				i--;
			}
		}
	}
	
	public void render(Graphics g) {
		background.render(g);
		tileMap.render(g);
		player.render(g);
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bloodLosses.size(); i++) {
			bloodLosses.get(i).setMapPosition(
				(int)tileMap.getX(), (int)tileMap.getY());
			bloodLosses.get(i).render(g);
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

	public void setBackground(Background background) {
		this.background = background;
	}
	
}
