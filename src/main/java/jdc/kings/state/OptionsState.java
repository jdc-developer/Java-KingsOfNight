package jdc.kings.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.utils.BundleUtil;

public class OptionsState extends GameState implements KeyState {
	
	private BufferedImage image;
	
	private Font font;
	private String[] options = new String[7];
	
	public OptionsState() {
		try {
			Locale locale = Game.getInstance().getPreferences().getLocale();
			String menuOptionOne = BundleUtil.getMessageResourceString("menuOptionOne", locale);
			String menuOptionTwo = BundleUtil.getMessageResourceString("menuOptionTwo", locale);
			String menuOptionThree = BundleUtil.getMessageResourceString("menuOptionThree", locale);
			String menuOptionFour = BundleUtil.getMessageResourceString("menuOptionFour", locale);
			String menuOptionFive = BundleUtil.getMessageResourceString("menuOptionFive", locale);
			String menuOptionSix = BundleUtil.getMessageResourceString("menuOptionSix", locale);
			String menuOptionSeven = BundleUtil.getMessageResourceString("menuOptionSeven", locale);
			options[0] = menuOptionOne;
			options[1] = menuOptionTwo;
			options[2] = menuOptionThree;
			options[3] = menuOptionFour;
			options[4] = menuOptionFive;
			options[5] = menuOptionSix;
			options[6] = menuOptionSeven;
			
			font = new Font("Arial", Font.PLAIN, 20);
			image = ImageIO.read(getClass().getResourceAsStream("/game/options.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, 30, 300, image.getWidth(), image.getHeight(), null);
		
		g.setFont(font);
		g.setColor(Color.white);
		for (int i = 0; i < options.length; i ++) {
			g.drawString(options[i], 90, 330 + (36 * i));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
