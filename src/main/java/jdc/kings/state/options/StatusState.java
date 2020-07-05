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
	
	private BufferedImage image;
	private BufferedImage edronot;
	private Font titleFont;
	private Font subtitleFont;
	private Font font;
	private String title;
	private String level;
	
	private String[] statusInfo = new String[6];
	
	public StatusState() {
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
		
		level = BundleUtil.getMessageResourceString("statusInfoOne", locale);
		statusInfo[0] = BundleUtil.getMessageResourceString("statusInfoTwo", locale);
		statusInfo[1] = BundleUtil.getMessageResourceString("statusInfoThree", locale);
		statusInfo[2] = BundleUtil.getMessageResourceString("statusInfoFour", locale);
		statusInfo[3] = BundleUtil.getMessageResourceString("statusInfoFive", locale);
		statusInfo[4] = BundleUtil.getMessageResourceString("statusInfoSix", locale);
		statusInfo[5] = BundleUtil.getMessageResourceString("menuOptionOne", locale);
	}

	@Override
	public void render(Graphics2D g) {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		g.drawImage(edronot, 405, 140, edronot.getWidth() + 40, edronot.getHeight() + 40, null);
		
		g.setFont(titleFont);
		g.drawString(title, 492, 95);
		g.drawString(Constants.NAME, 502, 155);
		
		g.setFont(subtitleFont);
		g.drawString(level + ":", 502, 175);
		g.setFont(font);
		
		int level = player.getStatus().getLevel();
		g.drawString(String.valueOf(level), 547, 175);
		
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
				case 2:
					Integer attack = (int) player.getAttackBonus();
					g.drawString(attack.toString(), 530, 250 + (i * 20));
					break;
				case 3:
					Integer armor = (int) player.getArmorBonus();
					if (locale.getCountry().equals("US") && locale.getLanguage().equals("en")) {
						g.drawString(armor.toString(), 460, 250 + (i * 20));
					} else {
						g.drawString(armor.toString(), 485, 250 + (i * 20));
					}
					break;
				case 4:
					Integer shield = (int) player.getShieldBonus();
					g.drawString(shield.toString(), 467, 250 + (i * 20));
					break;
				case 5:
					Integer items = player.getStatus().getItems().size();
					Integer storage = ItemState.ITEMSPERPAGE * ItemState.PAGES;
					g.drawString(items + " / " + storage, 488, 250 + (i * 20));
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
