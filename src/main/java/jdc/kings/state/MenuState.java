package jdc.kings.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jdc.kings.Game;
import jdc.kings.input.Key;
import jdc.kings.input.KeyInput;
import jdc.kings.options.Preferences;
import jdc.kings.options.PreferencesLoader;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.utils.BundleUtil;
import jdc.kings.utils.Constants;
import jdc.kings.view.Background;

public class MenuState extends GameState implements KeyState {
	
	private StateManager manager;
	private int currentChoice = 0;
	private int currentState = 0;
	private int selectedKey = 100;
	
	private String[] mainOptions = new String[4];
	private String[] languageOptions = new String[3];
	private String[] options = new String[10];
	private String[] currentOptions;
	
	private Font titleFont;
	private Font font;
	
	private Map<String, AudioPlayer> sfx = new HashMap<>();

	public MenuState(StateManager manager) {
		this.manager = manager;
		try {
			background = new Background("/backgrounds/menu-bg.jpg", 1);
			
			titleFont = new Font("Century Gothic", Font.PLAIN, 68);
			font = new Font("Arial", Font.PLAIN, 25);
			
			currentOptions = mainOptions;
			bgMusic = new AudioPlayer("/music/blood-ritual.mp3");
			bgMusic.loop();
			
			sfx.put("switch", new AudioPlayer("/sfx/menu/switch.mp3"));
			sfx.put("click", new AudioPlayer("/sfx/menu/click.mp3"));
			sfx.put("start", new AudioPlayer("/sfx/menu/start.mp3"));
			sfx.put("slash", new AudioPlayer("/sfx/menu/slash.mp3"));
			manager.showLoader(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		background.tick();
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String menuItemOne = BundleUtil.getMessageResourceString("menuItemOne", locale);
		String menuItemTwo = BundleUtil.getMessageResourceString("menuItemTwo", locale);
		String menuItemThree = BundleUtil.getMessageResourceString("menuItemThree", locale);
		String menuItemFour = BundleUtil.getMessageResourceString("menuItemFour", locale);
		mainOptions[0] = menuItemOne;
		mainOptions[1] = menuItemTwo;
		mainOptions[2] = menuItemThree;
		mainOptions[3] = menuItemFour;
		
		String languageOne = BundleUtil.getMessageResourceString("languageOne", locale);
		String languageTwo = BundleUtil.getMessageResourceString("languageTwo", locale);
		String back = BundleUtil.getMessageResourceString("back", locale);
		languageOptions[0] = languageOne;
		languageOptions[1] = languageTwo;
		languageOptions[2] = back;
		
		String controlOne = BundleUtil.getMessageResourceString("controlOne", locale);
		String controlTwo = BundleUtil.getMessageResourceString("controlTwo", locale);
		String controlThree = BundleUtil.getMessageResourceString("controlThree", locale);
		String controlFour = BundleUtil.getMessageResourceString("controlFour", locale);
		String controlFive = BundleUtil.getMessageResourceString("controlFive", locale);
		String controlSix = BundleUtil.getMessageResourceString("controlSix", locale);
		String controlSeven = BundleUtil.getMessageResourceString("controlSeven", locale);
		String controlEight = BundleUtil.getMessageResourceString("controlEight", locale);
		String restoreDefault = BundleUtil.getMessageResourceString("restoreDefault", locale);
		options[0] = controlOne;
		options[1] = controlTwo;
		options[2] = controlThree;
		options[3] = controlFour;
		options[4] = controlFive;
		options[5] = controlSix;
		options[6] = controlSeven;
		options[7] = controlEight;
		options[8] = restoreDefault;
		options[9] = back;
	}

	@Override
	public void render(Graphics g) {
		background.render(g);
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		g.drawString(Constants.TITLE, 280, 140);
		
		g.setFont(font);
		List<Key> keys = KeyInput.getInstance().getKeys();
		for (int i = 0; i < currentOptions.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.white);
			}
			
			g.drawString(currentOptions[i], 285, 200 + i * 30);
			if (currentState == 1 && i < 8) {
				Key key = keys.get(i);
				if (i == selectedKey) {
					g.setColor(Color.CYAN);
				}
				
				g.drawString(">", 585, 200 + i * 30);
				g.drawString(KeyEvent.getKeyText(key.getMapping()), 625, 200 + i * 30);
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (selectedKey < 8) {
			changeKey(key);
		} else {
			menuAction(key);
		}
		
	}
	
	private void changeKey(int key) {
		List<Key> keys = KeyInput.getInstance().getKeys();
		Key inputKey = keys.get(selectedKey);
		
		for (Key searchKey : keys) {
			if (searchKey.getMapping() == key) {
				searchKey.setMapping(inputKey.getMapping());
			}
		}
		
		inputKey.setMapping(key);
		Preferences preferences = Game.getInstance().getPreferences();
		preferences.setKeys(keys);
		PreferencesLoader.savePreferences(preferences);
		selectedKey = 100;
	}
	
	private void menuAction(int key) {
		if (key == KeyEvent.VK_ENTER) {
			switch(currentState) {
				case 0:
					mainSelect();
					break;
				case 1:
					optionSelect();
					break;
				case 2:
					languageSelect();
					break;
					
			}
		}
		
		if (key == KeyEvent.VK_UP) {
			sfx.get("switch").play();
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = currentOptions.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			sfx.get("switch").play();
			currentChoice++;
			if (currentChoice == currentOptions.length) {
				currentChoice = 0;
			}
		}
	}
	
	private void mainSelect() {
		
		switch (currentChoice) {
			case 0:
				manager.showLoader(true);
				bgMusic.close();
				sfx.get("start").play();
				sfx.get("slash").play();
				manager.setState(StateManager.MAP);
				break;
			case 1:
				currentOptions = options;
				currentState = 1;
				currentChoice = 0;
				sfx.get("click").play();
				break;
			case 2:
				currentOptions = languageOptions;
				currentState = 2;
				currentChoice = 0;
				sfx.get("click").play();
				break;
			case 3:
				System.exit(0);
				break;
		}
	}
	
	private void optionSelect() {
		sfx.get("click").play();
		selectedKey = currentChoice;
		switch (currentChoice) {
			case 8:
				KeyInput.getInstance().defaultKeys();
				Preferences preferences = Game.getInstance().getPreferences();
				preferences.setKeys(KeyInput.getInstance().getKeys());
				PreferencesLoader.savePreferences(preferences);
				break;
			case 9:
				selectedKey = 100;
				currentOptions = mainOptions;
				currentState = 0;
				currentChoice = 0;
				break;
		}
	}
	
	private void languageSelect() {
		sfx.get("click").play();
		switch (currentChoice) {
			case 0:
				Game.getInstance().setLocale("pt", "BR");
				break;
			case 1:
				Game.getInstance().setLocale("en", "US");
				break;
			case 2:
				currentOptions = mainOptions;
				currentState = 0;
				currentChoice = 0;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}
