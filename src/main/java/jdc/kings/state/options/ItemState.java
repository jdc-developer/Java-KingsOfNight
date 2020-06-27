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

public class ItemState extends GameState implements KeyState {
	
	private static ItemState instance;
	
	private BufferedImage image;
	private Font font;
	private String title;
	
	public static ItemState getInstance() {
		if (instance == null) {
			instance = new ItemState();
		}
		return instance;
	}
	
	private ItemState() {
		try {
			Locale locale = Game.getInstance().getPreferences().getLocale();
			title = BundleUtil.getMessageResourceString("menuOptionOne", locale);
			
			font = new Font("Arial", Font.BOLD, 16);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-items.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		g.setFont(font);
		g.drawString(title, 500, 95);
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
