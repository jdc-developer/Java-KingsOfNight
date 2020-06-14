package jdc.kings.state;

import java.awt.Graphics;
import java.util.LinkedList;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.interactions.Blood;
import jdc.kings.view.Background;
import jdc.kings.view.HUD;
import jdc.kings.view.TileMap;

public abstract class GameState {
	
	protected TileMap tileMap;
	protected Background background;
	protected Player player;
	
	protected LinkedList<Enemy> enemies = new LinkedList<>();
	protected LinkedList<Blood> bloodLosses = new LinkedList<>();
	protected HUD hud;
	
	public abstract void tick();
	public abstract void render(Graphics g);

}
