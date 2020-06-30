package jdc.kings;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Locale;

import javax.swing.JPanel;

import jdc.kings.input.KeyInput;
import jdc.kings.input.MouseInput;
import jdc.kings.settings.Preferences;
import jdc.kings.settings.PreferencesLoader;
import jdc.kings.state.LoadingState;
import jdc.kings.state.StateManager;
import jdc.kings.utils.Constants;
import jdc.kings.view.Window;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private boolean running;
	private Thread thread;
	private static Game instance;
	private static StateManager manager;
	private static Preferences preferences;
	
	private BufferedImage image;
	private Graphics2D g;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private Game() {
		super();
		setFocusable(true);
		requestFocus();
	}
	
	public static Game getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		instance = new Game();
		preferences = PreferencesLoader.loadPreferences();
		if (preferences == null) {
			preferences = new Preferences();
			preferences.setLocale(Locale.getDefault());
		} else if (preferences.getKeys() != null) {
			KeyInput.getInstance().setKeys(preferences.getKeys());
		}
		manager = StateManager.getInstance();
		
		instance.addMouseListener(MouseInput.getInstance());
		instance.addMouseMotionListener(MouseInput.getInstance());
		instance.addKeyListener(KeyInput.getInstance());
		Window.createWindow();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void init() {
		image = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
	}

	@Override
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		while(running) {
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			
			if (wait < 0) wait = 5;
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, Constants.WIDTH, Constants.HEIGHT, null);
		g2.dispose();
		
	}

	private void update() {
		manager.tick();
	}
	
	private void draw() {
		manager.render(g);
		if (manager.showLoader()) {
			LoadingState.getInstance().render(g);
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
