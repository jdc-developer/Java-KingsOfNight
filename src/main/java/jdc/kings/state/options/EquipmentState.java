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
	
	private static final int ROWS = 5;
	private static final int COLS = 2;
	public static final int ITEMS = ROWS * COLS;
	
	private BufferedImage image;
	private BufferedImage selectedItemImage;
	
	private Inventory inventory;
	private ActionState actionState;
	private DescriptionState descriptionState;
	private PromptState promptState;
	
	private String title;
	private String equip;
	private String unequip;
	private String description;
	
	private Option[] options = new Option[10];
	
	private Font font;
	private int selectedItem;
	
	public EquipmentState(Item item) {
		try {
			font = new Font("Arial", Font.BOLD, 14);
			selectedItemImage = ImageIO.read(getClass().getResourceAsStream("/game/glass.png"));
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
				Locale locale = Game.getInstance().getPreferences().getLocale();
				if (item.isEquipped()) {
					String equipped = BundleUtil.getMessageResourceString("currentlyEquipped", locale);
					promptState = new PromptState(equipped);
				} else {
					if (item.getType() == Item.USABLE || item.getType() == Item.RING) {
						String slotDescription = BundleUtil.getMessageResourceString("equipOption", locale);
						ItemActionManager.createSlotEquipActionState(this, item, slotDescription);
					} else {
						inventory.equipItem(item, null);
					}
				}
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
		
		description = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		equip = BundleUtil.getMessageResourceString("itemOptionThree", locale);
		unequip = BundleUtil.getMessageResourceString("itemOptionFive", locale);
		
		if (descriptionState != null) {
			descriptionState.tick();
		}
		
		if (descriptionState != null || actionState != null || promptState != null) {
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
			
			if (i == selectedItem) {
				g.drawImage(selectedItemImage, (int)option.getX() + 2, (int)option.getY() + 2, null);
			}
			
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
		
		if (promptState != null) {
			promptState.render(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (descriptionState != null) {
			descriptionState.keyPressed(e);
		} else if (actionState != null) {
			actionState.keyPressed(e);
		} else if (promptState != null) {
			promptState = null;
		} else {
			activeKeyPressed(e);
		}
	}
	
	private void activeKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_RIGHT) {
			audioPlayer.play("switch");
			
			if (selectedItem == 9) {
				selectedItem = 0;
			} else {
				selectedItem += ROWS;
			}
			
			if (selectedItem >= ITEMS) {
				int availableRows = (int) Math.ceil(ITEMS / ROWS);
				selectedItem -= ROWS + (availableRows * ROWS) - (ROWS + 1);
			}
		}
		
		if (key == KeyEvent.VK_LEFT) {
			audioPlayer.play("switch");
			selectedItem -= ROWS;
			
			if (selectedItem < 0) {
				int availableRows = (int) Math.ceil(ITEMS / ROWS);
				selectedItem += ROWS + (availableRows * ROWS) - 1;
				
				if (selectedItem >= ITEMS) {
					selectedItem -= ROWS;
				}
			}
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			selectedItem--;
			
			if (selectedItem < 0) {
				selectedItem = 9;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			selectedItem++;
			
			if (selectedItem >= 10) {
				selectedItem = 0;
			}
		}
		
		if (key == KeyEvent.VK_ENTER) {
			Option option = options[selectedItem];
			audioPlayer.play("click");
			if (option.getItem() != null) {
				Integer slot = null;
				if (selectedItem == 4 || selectedItem == 7) slot = 1;
				if (selectedItem == 8 || selectedItem == 9) slot = 2;
				
				ItemActionManager.createUnequipActionState(this, option, description, unequip, slot);
			} else {
				ItemActionManager.createEquipActionState(this, option, equip);
			}
		}
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
					
					if (actionState != null) {
						if ((point.getX() < actionState.getX() || (point.getY() < actionState.getY() ||
								point.getY() > actionState.getY() + actionState.getImage().getHeight())) ||
								(point.getX() > actionState.getX() + actionState.getImage().getWidth() || (point.getY() < actionState.getY() ||
										point.getY() > actionState.getY() + actionState.getImage().getHeight()))) {
							
							selectedItem = i;
							audioPlayer.play("click");
							if (option.getItem() != null) {
								Integer slot = null;
								if (i == 4 || i == 7) slot = 1;
								if (i == 8 || i == 9) slot = 2;
								
								ItemActionManager.createUnequipActionState(this, option, description, unequip, slot);
							} else {
								ItemActionManager.createEquipActionState(this, option, equip);
							}
						}
					} else {
						selectedItem = i;
						audioPlayer.play("click");
						if (option.getItem() != null) {
							Integer slot = null;
							if (i == 4 || i == 7) slot = 1;
							if (i == 8 || i == 9) slot = 2;
							
							ItemActionManager.createUnequipActionState(this, option, description, unequip, slot);
						} else {
							ItemActionManager.createEquipActionState(this, option, equip);
						}
					}
				}
			}
		}
		
		if (actionState != null) {
			actionState.mousePressed(e);
		}
		
		if (promptState != null) {
			promptState = null;
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
	
	public void equipSlotOne(Item item) {
		inventory.equipItem(item, 1);
	}
	
	public void equipSlotTwo(Item item) {
		inventory.equipItem(item, 2);
	}
	
	public void unequip(Item item) {
		inventory.unequipItem(item, null);
	}
	
	public void unequipSlotOne(Item item) {
		inventory.unequipItem(item, 1);
	}
	
	public void unequipSlotTwo(Item item) {
		inventory.unequipItem(item, 2);
	}

	public void setDescriptionState(DescriptionState descriptionState) {
		this.descriptionState = descriptionState;
	}

	public void setActionState(ActionState actionState) {
		this.actionState = actionState;
	}

}
