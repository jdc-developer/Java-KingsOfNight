package jdc.kings;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.Player;
import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.Constants;
import jdc.kings.utils.ImageUtil;
import jdc.kings.view.Handler;
import jdc.kings.view.Window;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private boolean running = false;
	private Thread thread;
	private static Game instance;
	private static Handler handler;
	private static ImageUtil imageLoader;
	
	private Game() {};
	
	public static Game getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		instance = new Game();
		handler = Handler.getInstance();
		imageLoader = ImageUtil.getInstance();
		Window.createWindow();
		
		Player player = new Player(100, 100, ObjectType.PLAYER);
		imageLoader.loadImage("/player/attack.png");
		List<BufferedImage> images = new ArrayList<>();
		for (int i = 1; i <= 15; i++) {
			BufferedImage idle = imageLoader.grabImage(1, i, 64, 64);
			images.add(idle);
		}
		player.getActionImages().put(ObjectAction.IDLE, images);
		handler.addObject(player);
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
               System.out.println("FPS: "+ frames);
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
