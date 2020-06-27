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
import jdc.kings.utils.Constants;

public class StatusState extends GameState implements KeyState {
	
	private static StatusState instance;
	
	private BufferedImage image;
	private BufferedImage edronot;
	private Font titleFont;
	private Font subtitleFont;
	private Font font;
	private String title;
	
	private String[] statusInfo = new String[5];
	
	public static StatusState getInstance() {
		if (instance == null) {
			instance = new StatusState();
		}
		return instance;
	}
	
	private StatusState() {
		try {
			titleFont = new Font("Arial", Font.BOLD, 16);
			subtitleFont = new Font("Arial", Font.BOLD, 14);
			font = new Font("Arial", Font.PLAIN, 13);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-options.png"));
			edronot = ImageIO.read(getClass().getResourceAsStream("/game/edronot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionFour", locale);
		
		statusInfo[0] = BundleUtil.getMessageResourceString("StatusInfoOne", locale);
		statusInfo[1] = BundleUtil.getMessageResourceString("StatusInfoTwo", locale);
		statusInfo[2] = BundleUtil.getMessageResourceString("StatusInfoThree", locale);
		statusInfo[3] = BundleUtil.getMessageResourceString("StatusInfoFour", locale);
		statusInfo[4] = BundleUtil.getMessageResourceString("StatusInfoFive", locale);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		g.drawImage(edronot, 405, 140, edronot.getWidth() + 40, edronot.getHeight() + 40, null);
		
		g.setFont(titleFont);
		g.drawString(title, 492, 95);
		
		g.drawString(Constants.NAME, 502, 155);
		for (int i = 0; i < statusInfo.length; i++) {
			String status = statusInfo[i];
			g.setFont(subtitleFont);
			g.drawString(status + ":", 405, 250 + (i * 20));
			
			g.setFont(font);
			switch (i) {
			case 0:
				Integer health = (int) player.getHealth();
				Integer maxHealth = (int) player.getMaxHealth();
				g.drawString(health + " / " + maxHealth, 465, 250 + (i * 20));
				break;
			case 1:
				Integer stamina = (int) player.getStamina();
				Integer maxStamina = (int) player.getMaxStamina();
				g.drawString(stamina + " / " + maxStamina, 475, 250 + (i * 20));
				break;
			}
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
