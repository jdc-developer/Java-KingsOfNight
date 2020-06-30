package jdc.kings.state.options;

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
import jdc.kings.objects.Item;
import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.utils.BundleUtil;

public class DescriptionState extends GameState implements MouseState, KeyState {
	
	private BufferedImage image;
	private Item item;
	private ItemState itemState;
	
	private String title;
	private String back;
	private Font titleFont;
	private Font font;

	public DescriptionState(ItemState itemState, Item item) {
		try {
			this.itemState = itemState;
			this.item = item;
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-description.png"));
			titleFont = new Font("Arial", Font.BOLD, 15);
			font = new Font("Arial", Font.PLAIN, 14);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		back = BundleUtil.getMessageResourceString("back", locale);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		g.drawString(title, 482, 95);
		g.drawString(back, 415, 468);
		
		g.drawImage(item.getImage(), 405, 140, 64,  64, null);
		
		g.setColor(Color.black);
		g.drawString(item.getName(), 480, 175);
		
		g.setFont(font);
		int count = 0;
		for (String line : item.getDescription().split("\n")) {
			g.drawString(line, 405, 225 + (20 * count));
			count++;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			if (point.getX() >= 395 && point.getX() <= 475 && point.getY() >= 453 && point.getY() <= 477) {
				audioPlayer.play("click");
				itemState.setDescriptionState(null);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ESCAPE) {
			audioPlayer.play("click");
			itemState.setDescriptionState(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
