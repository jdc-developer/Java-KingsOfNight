package jdc.kings.state.options;

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
import jdc.kings.objects.Inventory;
import jdc.kings.objects.Item;
import jdc.kings.state.GameState;
import jdc.kings.state.LevelManager;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;
import jdc.kings.utils.BundleUtil;

public class EquipmentState extends GameState implements KeyState, MouseState {
	
	private BufferedImage image;
	private Font font;
	private String title;
	
	private ActionState actionState;
	private DescriptionState descriptionState;
	
	private Inventory inventory;
	private String equip;
	private String unequip;
	private String description;
	
	private Option[] options = new Option[10];
	
	public EquipmentState(Item item) {
		try {
			font = new Font("Arial", Font.BOLD, 14);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-equipment.png"));
			inventory = player.getInventory();
			
			int yOffset = 0;
			for (int i = 0; i < 5; i++) {
				options[i] = new Option(null, 34, 34);
				options[i].setX(453);
				options[i].setY(138 + yOffset);
				if (i == 3) {
					yOffset += 110;
				} else {
					yOffset += 60;
				}
			}
			
			yOffset = 0;
			for (int i = 5; i < 10; i++) {
				options[i] = new Option(null, 34, 34);
				options[i].setX(583);
				options[i].setY(138 + yOffset);
				if (i == 8) {
					yOffset += 110;
				} else {
					yOffset += 60;
				}
			}
			
			if (item != null) {
				inventory.equipItem(item, 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionThree", locale);
		
		options[0].setItem(inventory.getEquippedItem(Item.HELMET, null));
		options[1].setItem(inventory.getEquippedItem(Item.ARMOR, null));
		options[2].setItem(inventory.getEquippedItem(Item.GAUNTLETS, null));
		options[3].setItem(inventory.getEquippedItem(Item.GREAVES, null));
		options[4].setItem(inventory.getEquippedItem(Item.RING, 1));
		options[5].setItem(inventory.getEquippedItem(Item.SWORD, null));
		options[6].setItem(inventory.getEquippedItem(Item.SHIELD, null));
		options[7].setItem(inventory.getEquippedItem(Item.USABLE, 1));
		options[8].setItem(inventory.getEquippedItem(Item.USABLE, 2));
		options[9].setItem(inventory.getEquippedItem(Item.RING, 2));
		
		options[0].setType(Item.HELMET);
		options[1].setType(Item.ARMOR);
		options[2].setType(Item.GAUNTLETS);
		options[3].setType(Item.GREAVES);
		options[4].setType(Item.RING);
		options[5].setType(Item.SWORD);
		options[6].setType(Item.SHIELD);
		options[7].setType(Item.USABLE);
		options[8].setType(Item.USABLE);
		options[9].setType(Item.RING);
		
		String itemOptionTwo = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		String itemOptionThree = BundleUtil.getMessageResourceString("itemOptionThree", locale);
		String itemOptionFive = BundleUtil.getMessageResourceString("itemOptionFive", locale);
		
		equip = itemOptionThree;
		unequip = itemOptionFive;
		description = itemOptionTwo;
		
		if (descriptionState != null) {
			descriptionState.tick();
		}
		
		if (descriptionState != null || actionState != null) {
			LevelManager.getCurrentLevel().setEscEnabled(false);
		} else {
			LevelManager.getCurrentLevel().setEscEnabled(true);
		}
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
		
		for (int i = 0; i < options.length; i++) {
			Option option = options[i];
			Item item = option.getItem();
			
			if (item != null) {
				g.drawImage(item.getImage(), (int)option.getX() + 2, (int)option.getY(), item.getImage().getWidth(),
						item.getImage().getHeight(), null);
			}
		}
		
		if (actionState != null) {
			actionState.render(g);
		}
		
		if (descriptionState != null) {
			descriptionState.render(g);
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

	@Override
	public void mousePressed(MouseEvent e) {
		if (descriptionState != null) {
			descriptionState.mousePressed(e);
		} else {
			activeMousePressed(e);
		}
	}
	
	private void activeMousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			if (actionState != null) {
				if ((point.getX() < actionState.getX() || (point.getY() < actionState.getY() ||
						point.getY() > actionState.getY() + actionState.getImage().getHeight())) ||
						(point.getX() > actionState.getX() + actionState.getImage().getWidth() || (point.getY() < actionState.getY() ||
								point.getY() > actionState.getY() + actionState.getImage().getHeight()))) {
					actionState = null;
				}
			}
			
			for (int i = 0; i < options.length; i++) {
				Option option = options[i];
				if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
						&& (point.getY() >= option.getY() - 20 && point.getY() <= option.getY() + option.getHeight())) {
					if (option.getItem() != null) {
						ItemActionManager.createUnequipActionState(this, option, description, unequip);
					} else {
						ItemActionManager.createEquipActionState(this, option, equip);
					}
				}
			}
		}
		
		if (actionState != null) {
			actionState.mousePressed(e);
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
		if (actionState != null) {
			actionState.mouseMoved(e);
		}
	}

	public void setDescriptionState(DescriptionState descriptionState) {
		this.descriptionState = descriptionState;
	}

	public void setActionState(ActionState actionState) {
		this.actionState = actionState;
	}

}
