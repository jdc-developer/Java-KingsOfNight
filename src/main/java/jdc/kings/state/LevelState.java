package jdc.kings.state;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import jdc.kings.input.Key;
import jdc.kings.input.KeyInput;
import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.Enemy;
import jdc.kings.objects.Player;
import jdc.kings.objects.interactions.Blood;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.utils.Constants;
import jdc.kings.view.Background;
import jdc.kings.view.HUD;
import jdc.kings.view.TileMap;

public class LevelState extends GameState implements KeyState, MouseState {
	
	private StateManager manager;
	private TileMap tileMap;
	
	private LinkedList<Enemy> enemies = new LinkedList<>();
	private LinkedList<Blood> bloodLosses = new LinkedList<>();
	private BlockingQueue<Enemy> enemyQueue;
	
	private List<BossState> bossStates = new ArrayList<>();
	private DeathState deathState = new DeathState();
	private OptionsState optionsState = new OptionsState();
	
	private BossState runningBoss;
	private SpawnerThread spawnerThread;
	
	private boolean death;
	private boolean options;
	private float alpha = 1.0f;
	private float reduceSound = 0;

	public LevelState(TileMap tm, Player player, String background, String music) {
		this.player = player;
		this.tileMap = tm;
		this.background = new Background(background, 0.1f);
		this.manager = StateManager.getInstance();
		hud = new HUD(this.player);
		
		bgMusic = "level-music";
		audioPlayer.loadAudio(bgMusic, music);
		audioPlayer.loop(bgMusic);
		
		audioPlayer.loadAudio("blood-explosion", "/sfx/enemies/blood-explosion.mp3");
		audioPlayer.loadAudio("click", "/sfx/menu/click.mp3");
	}
	
	public void tick() {
		manager.showLoader(false);
		tileMap.setPosition(
				Constants.WIDTH / Constants.SCALE - player.getX(),
				Constants.HEIGHT / Constants.SCALE - player.getY());
		
		background.setPosition(tileMap.getX(), tileMap.getY());
		
		player.checkAttack(enemies);
		player.tick();
		
		if (options) {
			optionsState.tick();
		}
		
		// Get enemies from the queue
		for(int i = 0; i < enemyQueue.size(); i++) {
			try {
				enemies.add(enemyQueue.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// update enemies actions
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.isDead() && e.getHealth() <= 0) {
				if (e.bleeds()) {
					bloodLosses.add(
							new Blood((int)e.getX(), (int)e.getY(), 132, 78, 2, !player.isFacingRight()));
					enemies.remove(i);
					audioPlayer.play("blood-explosion");
					i--;
				}
			} else if (player.getX() - e.getX() <= 1600 && player.getX() - e.getX() >= -1600) {
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
				audioPlayer.close(bgMusic);
			}
		}
		
		if (runningBoss != null) {
			runningBoss.tick();
		}
		
		if (player.isDead()) {
			if (!death) {
				death = true;
				try {
					spawnerThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Runnable deathStateThread = new Runnable() {
					
					@Override
					public void run() {
						deathState.loopMusic();
					}
				};
				new Thread(deathStateThread).run();
			} else {
				reduceSound += -0.06f;
				if (reduceSound <= -25) {
					audioPlayer.close(bgMusic);
				} else {
					audioPlayer.reduceSound(bgMusic, reduceSound);
				}
				
				deathState.tick();
			}
		}
	}
	
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, alpha));
		
		background.render(g);
		tileMap.render(g);
		player.render(g);
		
		// render enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (player.getX() - e.getX() <= 1600 && player.getX() - e.getX() >= -1600) {
				e.render(g);
			}
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
			options = false;
			if (death) {
				deathState.render(g);
				alpha -= 0.003f;
			    if (alpha <= 0) {
			        alpha = 0;
			    }
			}
		}
		
		if (options) {
			optionsState.render(g);
		}
	}

	@Override
	public void closeMusic() {
		super.closeMusic();
		if (runningBoss != null) {
			runningBoss.closeMusic();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (options) {
			optionsState.keyPressed(e);
		}
		if (player.isDead()) {
			deathState.keyPressed(e);
		} else {
			inGameKeyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (player.isDead()) {
			deathState.keyReleased(e);
		} else {
			inGameKeyReleased(e);
		}
	}
	
	private void inGameKeyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = KeyInput.getInstance().findKey(keyPressed);
		
		if (key != null && !player.isDead()) {
			key.setPressed(true);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setRight(true);
			}
			if (action == KeyAction.LEFT) {
				player.setLeft(true);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(true);
			}
			if (action == KeyAction.STABBING) {
				player.setStabbing(true);
			}
			if (action == KeyAction.CUTTING) {
				player.setCutting(true);
			}
			if (action == KeyAction.SLICING) {
				player.setSlicing(true);
			}
			if (action == KeyAction.ROLLING) {
				player.setRolling(true);
			}
			if (action == KeyAction.SHIELD) {
				player.setShield(true);
			}
		}
		
		if (keyPressed == KeyEvent.VK_ESCAPE) {
			if (options) {
				options = false;
			} else {
				audioPlayer.play("click");
				options = true;
			}
		}
	}
	
	private void inGameKeyReleased(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = KeyInput.getInstance().findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(false);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setRight(false);
			}
			if (action == KeyAction.LEFT) {
				player.setLeft(false);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(false);
			}
			if (action == KeyAction.SHIELD) {
				player.setShield(false);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (options) {
			optionsState.mouseMoved(e);
		}
	}
	
	public List<BossState> getBossStates() {
		return bossStates;
	}

	public void setEnemyQueue(BlockingQueue<Enemy> enemyQueue) {
		this.enemyQueue = enemyQueue;
	}

	public SpawnerThread getSpawnerThread() {
		return spawnerThread;
	}

	public void setSpawnerThread(SpawnerThread enemyThread) {
		this.spawnerThread = enemyThread;
	}
	
}
