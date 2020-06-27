package jdc.kings.state.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.input.Key;
import jdc.kings.input.KeyInput;
import jdc.kings.options.Preferences;
import jdc.kings.options.PreferencesLoader;
import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.utils.BundleUtil;

public class SettingsState extends GameState implements KeyState {
	
	private static SettingsState instance;
	
	private BufferedImage image;
	private Font font;
	private String title;
	
	private String[] options = new String[10];
	private String[] languageOptions = new String[3];
	private String[] currentOptions;
	
	private int currentChoice = 0;
	private int currentState = 0;
	private int selectedKey = 100;
	
	public static SettingsState getInstance() {
		if (instance == null) {
			instance = new SettingsState();
		}
		return instance;
	}
	
	private SettingsState() {
		try {
			currentOptions = options;
			font = new Font("Arial", Font.BOLD, 16);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-options.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuItemTwo", locale);
		
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
		String menuItemThree = BundleUtil.getMessageResourceString("menuItemThree", locale);
		
		options[0] = controlOne;
		options[1] = controlTwo;
		options[2] = controlThree;
		options[3] = controlFour;
		options[4] = controlFive;
		options[5] = controlSix;
		options[6] = controlSeven;
		options[7] = controlEight;
		options[8] = restoreDefault;
		options[9] = menuItemThree;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		g.setFont(font);
		g.drawString(title, 487, 95);
		
		List<Key> keys = KeyInput.getInstance().getKeys();
		for (int i = 0; i < currentOptions.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.white);
			}
			
			g.drawString(currentOptions[i], 400, 170 + i * 30);
			if (currentState == 0 && i < 8) {
				Key key = keys.get(i);
				if (i == selectedKey) {
					g.setColor(Color.CYAN);
				}
				
				g.drawString(">", 550, 170 + i * 30);
				g.drawString(KeyEvent.getKeyText(key.getMapping()), 575, 170 + i * 30);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (selectedKey < 8) {
			changeKey(key);
		} else {
			menuAction(key);
		}
	}

	private void menuAction(int key) {
		if (key == KeyEvent.VK_ENTER) {
			audioPlayer.play("click");
			switch(currentState) {
				case 0:
					optionSelect();
					break;
				case 1:
					languageSelect();
					break;
					
			}
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = currentOptions.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			currentChoice++;
			if (currentChoice == currentOptions.length) {
				currentChoice = 0;
			}
		}
	}
	
	private void optionSelect() {
		audioPlayer.play("click");
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
				currentOptions = languageOptions;
				currentState = 1;
				currentChoice = 0;
				break;
		}
	}
	
	private void languageSelect() {
		audioPlayer.play("click");
		switch (currentChoice) {
			case 0:
				Game.getInstance().setLocale("pt", "BR");
				break;
			case 1:
				Game.getInstance().setLocale("en", "US");
				break;
			case 2:
				currentOptions = options;
				currentState = 0;
				currentChoice = 0;
				break;
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
