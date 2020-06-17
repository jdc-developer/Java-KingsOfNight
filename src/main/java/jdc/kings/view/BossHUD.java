package jdc.kings.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import jdc.kings.objects.Enemy;

public class BossHUD {

	private Enemy boss;
	private BufferedImage image;
	private String bossName;
	private Font font;
	private static final Color HEALTH = new Color(87, 11, 10);
	
	public BossHUD(Enemy boss, String bossName) {
		this.boss = boss;
		this.bossName = bossName;
		this.font = new Font("Arial", Font.PLAIN, 17);
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/hud/boss-hud.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(bossName, 200, 490);
		g.setColor(HEALTH);
		g.fillRect(200, 503, boss.getHealth() * 3, 25);
		g.drawImage(image, 200, 480, 600, 80, null);
	}
	
}
