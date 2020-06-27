package jdc.kings.state.options;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.state.GameState;
import jdc.kings.state.LevelManager;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;
import jdc.kings.utils.BundleUtil;

public class OptionsState extends GameState implements KeyState, MouseState {
	
	private static OptionsState instance;
	private BufferedImage image;
	
	private Font font;
	private int currentChoice = 0;
	private Option[] options = new Option[7];
	private Color selectedBackground = new Color(41, 41, 41);
	
	private GameState subState;
	
	public static OptionsState getInstance() {
		if (instance == null) {
			instance = new OptionsState();
		}
		return instance;
	}
	
	private OptionsState() {
		try {
			options[0] = new Option(null, 180, 30);
			options[1] = new Option(null, 180, 30);
			options[2] = new Option(null, 180, 30);
			options[3] = new Option(null, 180, 30);
			options[4] = new Option(null, 180, 30);
			options[5] = new Option(null, 180, 30);
			options[6] = new Option(null, 180, 30);
			
			audioPlayer.loadAudio("switch", "/sfx/menu/switch.mp3");
			audioPlayer.loadAudio("click", "/sfx/menu/click.mp3");
			
			font = new Font("Arial", Font.PLAIN, 20);
			image = ImageIO.read(getClass().getResourceAsStream("/game/options.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String menuItemTwo = BundleUtil.getMessageResourceString("menuItemTwo", locale);
		String menuOptionOne = BundleUtil.getMessageResourceString("menuOptionOne", locale);
		String menuOptionTwo = BundleUtil.getMessageResourceString("menuOptionTwo", locale);
		String menuOptionThree = BundleUtil.getMessageResourceString("menuOptionThree", locale);
		String menuOptionFour = BundleUtil.getMessageResourceString("menuOptionFour", locale);
		String menuOptionFive = BundleUtil.getMessageResourceString("menuOptionFive", locale);
		String menuOptionSix = BundleUtil.getMessageResourceString("menuOptionSix", locale);
		
		options[0].setDescription(menuOptionOne);
		options[1].setDescription(menuOptionTwo);
		options[2].setDescription(menuOptionThree);
		options[3].setDescription(menuOptionFour);
		options[4].setDescription(menuItemTwo);
		options[5].setDescription(menuOptionFive);
		options[6].setDescription(menuOptionSix);
		
		if (subState != null) {
			subState.tick();
		}
	}

	@Override
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, 10, 290, image.getWidth(), image.getHeight(), null);
		
		g2d.setFont(font);
		for (int i = 0; i < options.length; i ++) {
			Option option = options[i];
			
			if (i == currentChoice) {
				g2d.setComposite(AlphaComposite.getInstance(
			            AlphaComposite.SRC_OVER, 0.6f));
				g2d.setColor(selectedBackground);
				g2d.fillRect(60, 298 + (36 * i), 185, 30);
			}
			g2d.setColor(Color.white);
			g2d.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, 1f));
			
			option.setX(65);
			option.setY(320 + (36 * i));
			g2d.drawString(option.getDescription(), option.getX(), option.getY());
		}
		
		if (subState != null) {
			subState.render(g2d);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (subState == null) {
			optionsAction(e);
		} else {
			KeyState state = (KeyState) subState;
			state.keyPressed(e);
		}
	}
	
	private void optionsAction(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ENTER) {
			changeState();
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	private void changeState() {
		audioPlayer.play("click");
		
		switch (currentChoice) {
			case 0:
				subState = ItemState.getInstance();
				break;
			case 1:
				subState = SkillState.getInstance();
				break;
			case 2:
				subState = EquipmentState.getInstance();
				break;
			case 3:
				subState = StatusState.getInstance();
				break;
			case 4:
				subState = SettingsState.getInstance();
				break;
			case 6:
				LevelManager.getCurrentLevel().destroy();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			for (int i = 0; i < options.length; i++) {
				Option option = options[i];
				if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
						&& (point.getY() >= option.getY() - 20 && point.getY() <= option.getY() + option.getHeight())) {
					changeState();
				}
			}
			
			if (point.getX() >= 625 && point.getX() <= 645 && point.getY() >= 73 && point.getY() <= 93) {
				audioPlayer.play("click");
				subState = null;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = e.getPoint();
		
		for (int i = 0; i < options.length; i++) {
			Option option = options[i];
			if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
					&& (point.getY() >= option.getY() - 20 && point.getY() <= option.getY() + option.getHeight())) {
				currentChoice = i;
			}
		}
	}

	public void setSubState(GameState subState) {
		this.subState = subState;
	}

	public GameState getSubState() {
		return subState;
	}

}
