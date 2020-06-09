package jdc.kings;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import jdc.kings.input.KeyInput;
import jdc.kings.objects.Player;
import jdc.kings.objects.enemies.SkeletonArcher;
import jdc.kings.utils.Constants;
import jdc.kings.view.Background;
import jdc.kings.view.Handler;
import jdc.kings.view.TileMap;
import jdc.kings.view.Window;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private boolean running = false;
	private Thread thread;
	private static Game instance;
	private static Handler handler;
	
	private Game() {};
	
	public static Game getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		instance = new Game();
		
		TileMap tileMap = new TileMap(32, 20);
		int[] normal = {7, 37, 31, 25, 1};
		
		tileMap.loadTiles("/tilesets/dawn-of-the-gods-1.png", normal);
		tileMap.loadMap("/maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		Player player = new Player(tileMap);
		player.setPosition(100, 650);
		
		SkeletonArcher skeleton = new SkeletonArcher(tileMap);
		skeleton.setPosition(800, 600);
		skeleton.setPlayer(player);
		
		Background background = new Background("/backgrounds/level1-bg.gif", 0.1f);
		
		handler = Handler.getInstance();
		handler.setBackground(background);
		handler.setTileMap(tileMap);
		
		handler.setPlayer(player);
		handler.getEnemies().add(skeleton);
		
		KeyInput keyInput = KeyInput.getInstance();
		keyInput.setPlayer(player);
		instance.addKeyListener(keyInput);
		Window.createWindow();
	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
           long now = System.nanoTime();
           delta += (now - lastTime) / ns;
           lastTime = now;
           while(delta >=1) {
               tick();
               delta--;
           }
           if(running)
               render();
           frames++;
            
           if(System.currentTimeMillis() - timer > 1000) {
               timer += 1000;
               //System.out.println("FPS: "+ frames);
               frames = 0;
           }
        }
        stop();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		handler.render(g);
		g.dispose();
		bs.show();
	}

	private void tick() {
		handler.tick();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	private void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
