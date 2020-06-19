package jdc.kings.state;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private BossState runningBoss;
	
	private Map<String, AudioPlayer> sfx = new HashMap<>();
	private List<BossState> bossStates = new ArrayList<>();

	public LevelState(TileMap tm, Player player, String background, String music) {
		this.player = player;
		this.tileMap = tm;
		this.background = new Background(background, 0.1f);
		hud = new HUD(this.player);
		deathState = new DeathState();
		
		bgMusic = new AudioPlayer(music);
		bgMusic.loop();
		sfx.put("blood-explosion", new AudioPlayer("/sfx/enemies/blood-explosion.mp3"));
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
			if (e.isDead() && e.getHealth() <= 0) {
				if (e.bleeds()) {
					bloodLosses.add(
							new Blood((int)e.getX(), (int)e.getY(), 132, 78, 2, !player.isFacingRight()));
					enemies.remove(i);
					sfx.get("blood-explosion").play();
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
		
		for (int i = 0; i < bossStates.size(); i++) {
			BossState bossState = bossStates.get(i);
			if (player.getX() >= bossState.getX() && player.getY() >= bossState.getY() && !bossState.isRunning()) {
				bossState.start();
				runningBoss = bossState;
				enemies.add(bossState.getBoss());
				bgMusic.close();
			}
		}
		
		if (runningBoss != null) {
			runningBoss.tick();
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
		
		if (runningBoss != null) {
			runningBoss.render(g2d);
		}
		
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
	
	public List<BossState> getBossStates() {
		return bossStates;
	}

	public DeathState getDeathState() {
		return deathState;
	}
	
	@Override
	public void closeMusic() {
		super.closeMusic();
		if (runningBoss != null) {
			runningBoss.closeMusic();
		}
	}
	
}
