package jdc.kings.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Locale;

import javax.swing.ImageIcon;

import jdc.kings.Game;
import jdc.kings.utils.BundleUtil;
import jdc.kings.utils.Constants;

public class LoadingState extends GameState {
	
	private static LoadingState instance;
	private Image image;
	
	private Font font;
	private String loading;
	
	private LoadingState() {
		try {
			image = new ImageIcon(getClass().getResource("/backgrounds/loader.gif")).getImage();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		font = new Font("Arial", Font.PLAIN, 25);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		loading = BundleUtil.getMessageResourceString("loading", locale);
	}
	
	public static LoadingState getInstance() {
		if (instance == null) {
			instance = new LoadingState();
		}
		return instance;
	}

	@Override
	public void tick() { }

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		g.drawImage(image, (int)30, (int)460, 50, 50, null);
		
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(loading, 30, 540);
	}

}
