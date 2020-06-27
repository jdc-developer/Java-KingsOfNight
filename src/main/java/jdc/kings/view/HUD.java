package jdc.kings.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import jdc.kings.objects.Player;

public class HUD {

	private Player player;
	private BufferedImage hud;
	private BufferedImage healthBar;
	private BufferedImage staminaBar;
	
	private static final float MAXBARWIDTH = 150;
	private static final float BARWIDTHBASIS = MAXBARWIDTH / 100;
	private static float barImageWidth;
	private static float barImageWidthBasis;
	
	private static float maxHealth;
	private static float maxStamina;
	private static float healthBasis;
	private static float staminaBasis;
	
	public HUD(Player player) {
		this.player = player;
		try {
			hud = ImageIO.read(getClass().getResourceAsStream("/game/hud.png"));
			healthBar = ImageIO.read(getClass().getResourceAsStream("/game/health-bar.png"));
			staminaBar = ImageIO.read(getClass().getResourceAsStream("/game/stamina-bar.png"));
			
			barImageWidth = healthBar.getWidth();
			barImageWidthBasis = barImageWidth / 100;
			
			maxHealth = player.getMaxHealth();
			maxStamina = player.getMaxStamina();
			healthBasis = maxHealth / 100;
			staminaBasis = maxStamina / 100;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g) {
		float healthTotal = player.getHealth() / healthBasis;
		float staminaTotal = player.getStamina() / staminaBasis;
		
		float healthImageWidth = healthTotal * barImageWidthBasis;
		float healthWidth = healthTotal * BARWIDTHBASIS;

		float staminaImageWidth = staminaTotal * barImageWidthBasis;
		float staminaWidth = staminaTotal * BARWIDTHBASIS;
		
		BufferedImage healthToDraw = new BufferedImage((int)barImageWidth, healthBar.getHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage staminaToDraw = new BufferedImage((int)barImageWidth, staminaBar.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < healthBar.getWidth(); x++) {
			for (int y = 0; y < healthBar.getHeight(); y++) {
				if (x <= healthImageWidth) {
					healthToDraw.setRGB(x, y, healthBar.getRGB(x, y));
				}
			}
		}
		
		for (int x = 0; x < staminaBar.getWidth(); x++) {
			for (int y = 0; y < staminaBar.getHeight(); y++) {
				if (x <= staminaImageWidth) {
					staminaToDraw.setRGB(x, y, staminaBar.getRGB(x, y));
				}
			}
		}
		
		g.drawImage(hud, 15, 15, 270, 90, null);
		g.drawImage(healthToDraw, 115, 28, (int)healthWidth, 13, null);
		g.drawImage(staminaToDraw, 115, 43, (int)staminaWidth, 13, null);
	}
	
}
