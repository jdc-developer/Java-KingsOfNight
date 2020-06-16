package jdc.kings;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Locale;

import jdc.kings.input.KeyInput;
import jdc.kings.options.Preferences;
import jdc.kings.options.PreferencesLoader;
import jdc.kings.state.StateManager;
import jdc.kings.utils.Constants;
import jdc.kings.view.Window;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private boolean running = false;
	private Thread thread;
	private static Game instance;
	private static StateManager manager;
	
	private static Preferences preferences;
	
	private Game() {};
	
	public static Game getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		instance = new Game();
		preferences = PreferencesLoader.loadPreferences();
		if (preferences == null) {
			preferences = new Preferences();
			preferences.setLocale(new Locale("pt", "BR"));
		} else if (preferences.getKeys() != null) {
			KeyInput.getInstance().setKeys(preferences.getKeys());
		}
		manager = StateManager.getInstance();
		instance.addKeyListener(KeyInput.getInstance());
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
		
		manager.render(g);
		g.dispose();
		bs.show();
	}

	private void tick() {
		manager.tick();
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

	public Preferences getPreferences() {
		return preferences;
	}

	public void setLocale(String language, String country) {
		preferences.setLocale(new Locale(language, country));
		PreferencesLoader.savePreferences(preferences);
	}

}
