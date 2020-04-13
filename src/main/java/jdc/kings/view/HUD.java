package jdc.kings.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import jdc.kings.objects.Player;

public class HUD {

	private Player player;
	private BufferedImage image;
	private static final Color HEALTH = new Color(87, 11, 10);
	private static final Color STAMINA = new Color(1, 64, 31);
	
	public HUD(Player player) {
		this.player = player;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/hud/hud.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(HEALTH);
		g.fillRect(50, 45, player.getHealth() * 5, 20);
		
		g.setColor(STAMINA);
		g.fillRect(45, 65, (int)player.getStamina() * 13, 20);
		g.drawImage(image, 15, 15, 320, 100, null);
		
		
	}
	
}
