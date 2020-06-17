package jdc.kings.state;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import jdc.kings.input.KeyInput;
import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.interactions.Blood;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.utils.Constants;
import jdc.kings.view.Background;
import jdc.kings.view.HUD;
import jdc.kings.view.TileMap;

public class LevelState extends GameState {
	
	private boolean death;
	private DeathState deathState = null;
	private Runnable deathStateThread;
	private float alpha = 1.0f;
	private float reduceSound = 0;

	public LevelState(TileMap tm, Player player, String background, String music) {
		this.player = player;
		this.tileMap = tm;
		this.background = new Background(background, 0.1f);
		hud = new HUD(this.player);
		deathState = new DeathState();
		bgMusic = new AudioPlayer(music);
		bgMusic.loop();
		KeyInput.getInstance().setPlayer(player);
	}
	
	public void tick() {
		StateManager.getInstance().showLoader(false);
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
		
		if (player.isDead()) {
			if (!death) {
				death = true;
				deathStateThread = new Runnable() {
					
					@Override
					public void run() {
						deathState.loopMusic();
					}
				};
				new Thread(deathStateThread).run();
			} else {
				reduceSound += -0.06f;
				if (reduceSound <= -25) {
					bgMusic.close();
				} else {
					bgMusic.reduceSound(reduceSound);
				}
				
				deathState.tick();
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, alpha));
		
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
		
		if (player.isDead()) {
			if (death) {
				deathState.render(g);
				alpha -= 0.003f;
			    if (alpha <= 0) {
			        alpha = 0;
			    }
			}
		}
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}
	
	public DeathState getDeathState() {
		return deathState;
	}
	
}
