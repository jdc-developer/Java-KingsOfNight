package jdc.kings.state.options;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.utils.BundleUtil;

public class EquipmentState extends GameState implements KeyState {
	
	private static EquipmentState instance;
	
	private BufferedImage image;
	private Font font;
	private String title;
	
	public static EquipmentState getInstance() {
		if (instance == null) {
			instance = new EquipmentState();
		}
		return instance;
	}
	
	private EquipmentState() {
		try {
			font = new Font("Arial", Font.BOLD, 14);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-equipment.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionThree", locale);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		g.setFont(font);
		if (locale.getCountry().equals("US") && locale.getLanguage().equals("en")) {
			g.drawString(title, 482, 95);
		} else {
			g.drawString(title, 475, 95);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
