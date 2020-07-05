package jdc.kings.state.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	private GameState parent;
	
	private String title;
	private String itemName;
	private String itemDescription;
	private String type;
	private String back;
	
	private Font titleFont;
	private Font typeFont;
	private Font font;

	public DescriptionState(GameState parent, Item item) {
		try {
			this.parent = parent;
			this.item = item;
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-description.png"));
			titleFont = new Font("Arial", Font.BOLD, 15);
			typeFont = new Font("Arial", Font.BOLD, 13);
			font = new Font("Arial", Font.PLAIN, 13);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		itemName = BundleUtil.getMessageResourceString(item.getName(), locale);
		itemDescription = BundleUtil.getMessageResourceString(item.getDescription(), locale);
		back = BundleUtil.getMessageResourceString("back", locale);
		
		switch (item.getType()) {
			case Item.USABLE:
				type = BundleUtil.getMessageResourceString("itemTypeOne", locale);
				break;
			case Item.HELMET:
				type = BundleUtil.getMessageResourceString("itemTypeTwo", locale);
				break;
			case Item.ARMOR:
				type = BundleUtil.getMessageResourceString("itemTypeThree", locale);
				break;
			case Item.GAUNTLETS:
				type = BundleUtil.getMessageResourceString("itemTypeFour", locale);
				break;
			case Item.GREAVES:
				type = BundleUtil.getMessageResourceString("itemTypeFive", locale);
				break;
			case Item.SWORD:
				type = BundleUtil.getMessageResourceString("itemTypeSix", locale);
				break;
			case Item.SHIELD:
				type = BundleUtil.getMessageResourceString("itemTypeSeven", locale);
				break;
			case Item.RING:
				type = BundleUtil.getMessageResourceString("itemTypeEight", locale);
				break;
			case Item.KEY:
				type = BundleUtil.getMessageResourceString("itemTypeNine", locale);
				break;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		g.drawString(title, 482, 95);
		g.drawString(back, 415, 468);
		
		g.drawImage(item.getImage(), 405, 140, Item.SIZE * 2,  Item.SIZE * 2, null);
		
		g.setColor(Color.black);
		int count = 0;
		for (String line : itemName.split("\n")) {
			g.drawString(line, 480, 160 + (17 * count));
			count++;
		}
		
		g.setFont(typeFont);
		if (count == 1) count = 0;
		g.drawString(type, 480, 180 + (9 * count));
		
		g.setFont(font);
		count = 0;
		for (String line : itemDescription.split("\n")) {
			g.drawString(line, 405, 230 + (20 * count));
			count++;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			if (point.getX() >= 395 && point.getX() <= 475 && point.getY() >= 453 && point.getY() <= 477) {
				try {
					audioPlayer.play("click");
					Method method = parent.getClass().getMethod("setDescriptionState", DescriptionState.class);
					DescriptionState descriptionState = null;
					method.invoke(parent, descriptionState);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
			try {
				audioPlayer.play("click");
				Method method = parent.getClass().getMethod("setDescriptionState", DescriptionState.class);
				DescriptionState descriptionState = null;
				method.invoke(parent, descriptionState);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
