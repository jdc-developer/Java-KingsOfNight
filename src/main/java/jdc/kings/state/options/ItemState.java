package jdc.kings.state.options;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.objects.InventoryItem;
import jdc.kings.objects.Item;
import jdc.kings.state.GameState;
import jdc.kings.state.LevelManager;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;
import jdc.kings.utils.BundleUtil;

public class ItemState extends GameState implements KeyState, MouseState {
	
	private static final int ROWS = 8;
	private static final int COLS = 6;
	public static final int PAGES = 3;
	public static final int ITEMSPERPAGE = ROWS * COLS;
	
	private List<InventoryItem> items;
	private LinkedList<Option> options = new LinkedList<>();
	private Option[] actionOptions = new Option[4];
	
	private ActionState actionState;
	private DescriptionState descriptionState;
	
	private BufferedImage image;
	private BufferedImage arrow;
	private BufferedImage inverseArrow;
	private BufferedImage selectedItemImage;
	
	private String title;
	private Font font;
	private Font quantityFont;
	
	private int lastItemsSize;
	private int page = 0;
	private int availablePages;
	private int selectedItem;
	private Integer typeFilter;
	
	public ItemState(Integer typeFilter) {
		try {
			this.typeFilter = typeFilter;
			font = new Font("Arial", Font.BOLD, 16);
			quantityFont = new Font("Arial", Font.PLAIN, 12);
			
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-items.png"));
			selectedItemImage = ImageIO.read(getClass().getResourceAsStream("/game/glass.png"));
			arrow = ImageIO.read(getClass().getResourceAsStream("/game/arrow.png"));
			
			double rads = Math.toRadians(180);
			double sin = Math.abs(Math.sin(rads));
			double cos = Math.abs(Math.cos(rads));
			int w = (int) Math.floor(arrow.getWidth() * cos + arrow.getHeight() * sin);
			int h = (int) Math.floor(arrow.getHeight() * cos + arrow.getWidth() * sin);
			inverseArrow = new BufferedImage(w, h, arrow.getType());
			AffineTransform at = new AffineTransform();
			at.translate(w / 2, h / 2);
			at.rotate(rads,0, 0);
			at.translate(-arrow.getWidth() / 2, -arrow.getHeight() / 2);
			final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			rotateOp.filter(arrow, inverseArrow);
			
			for (int i = 0; i < actionOptions.length; i++) {
				actionOptions[i] = new Option(null, 120, 18);
			}
			
			items = player.getInventory().getItems();
			getPageContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionOne", locale);
		
		String itemOptionOne = BundleUtil.getMessageResourceString("itemOptionOne", locale);
		String itemOptionTwo = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		String itemOptionThree = BundleUtil.getMessageResourceString("itemOptionThree", locale);
		String itemOptionFour = BundleUtil.getMessageResourceString("itemOptionFour", locale);
		
		actionOptions[0].setDescription(itemOptionOne);
		actionOptions[1].setDescription(itemOptionTwo);
		actionOptions[2].setDescription(itemOptionThree);
		actionOptions[3].setDescription(itemOptionFour);
		
		if (items.size() != lastItemsSize) {
			getPageContent();
		}
		
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
		g.drawImage(arrow, 423, 440, arrow.getWidth(), arrow.getHeight(), null);
		g.drawImage(inverseArrow, 406, 440, inverseArrow.getWidth(), inverseArrow.getHeight(), null);
		
		g.setFont(font);
		g.drawString(title, 483, 95);
		
		for (int i = 0; i < options.size(); i++) {
			Option option = options.get(i);
			InventoryItem inventoryItem = option.getInventoryItem();
			Item item = inventoryItem.getItem();
			
			if (i == selectedItem) {
				g.drawImage(selectedItemImage, (int)option.getX(), (int)option.getY(), null);
			}
			
			g.drawImage(item.getImage(), (int)option.getX(), (int)option.getY(), null);
			g.setFont(quantityFont);
			g.drawString(String.valueOf(inventoryItem.getQuantity()), (int)option.getX() + 25, (int)option.getY() + 10);
		}
		
		if (actionState != null) {
			actionState.render(g);
		}
		
		if (descriptionState != null) {
			descriptionState.render(g);
		}
	}
	
	private void getPageContent() {
		options.clear();
		actionState = null;
		
		int count = items.size();
		int startIndex = ITEMSPERPAGE * page;
		availablePages = (int) Math.floor(count / ITEMSPERPAGE);
		lastItemsSize = count;
		
		int col = 0;
		int row = 0;
		for (int i = startIndex; i < count; i++) {
			if (i >= ITEMSPERPAGE * (page + 1)) continue;
			
			InventoryItem inventoryItem = items.get(i);
			Option option = new Option(null, 32, 32);
			
			int x = 408 + (col * 38);
			int y = 136 + (row * 36);
			
			option.setInventoryItem(inventoryItem);
			option.setX(x);
			option.setY(y);
			if (typeFilter == null) {
				options.add(option);
			} else if (typeFilter == option.getInventoryItem().getItem().getType()) {
				options.add(option);
			}
			
			col++;
			if (col == COLS) {
				row++;
				col = 0;
				if (row == ROWS) {
					row = 0;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (descriptionState != null) {
			descriptionState.keyPressed(e);
		} else if (actionState != null) {
			actionState.keyPressed(e);
		} else {
			activeKeyPressed(e);
		}
	}
	
	private void activeKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_RIGHT) {
			audioPlayer.play("switch");
			selectedItem++;
			
			if (selectedItem >= options.size()) {
				selectedItem = 0;
			}
		}
		
		if (key == KeyEvent.VK_LEFT) {
			audioPlayer.play("switch");
			selectedItem--;
			
			if (selectedItem < 0) {
				selectedItem = options.size() - 1;
 			}
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			selectedItem -= COLS;
			
			if (selectedItem < 0) {
				int availableCols = (int) Math.ceil(options.size() / COLS);
				selectedItem += COLS + (availableCols * COLS);
				
				if (selectedItem >= options.size()) {
					selectedItem -= COLS;
				}
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			selectedItem += COLS;
			
			if (selectedItem >= options.size()) {
				int availableCols = (int) Math.ceil(options.size() / COLS);
				selectedItem -= COLS + (availableCols * COLS);
				
				if (selectedItem < 0) {
					selectedItem += COLS;
				}
			}
		}
		
		if (key == KeyEvent.VK_ENTER) {
			Option option = options.get(selectedItem);
			audioPlayer.play("click");
			if (typeFilter == null) {
				ItemActionManager.createActionState(this, option);
			} else {
				OptionsState.getInstance().setSubState(new EquipmentState(option.getInventoryItem().getItem()));
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
			
			if (point.getX() >= 423 && point.getX() <= 439 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page < availablePages) {
					page++;
					selectedItem = 0;
					getPageContent();
				}
			}
			
			if (point.getX() >= 406 && point.getX() <= 422 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page > 0) {
					page--;
					selectedItem = 0;
					getPageContent();
				}
			}
			
			for (int i = 0; i < options.size(); i++) {
				Option option = options.get(i);
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
						&& point.getY() >= option.getY() && point.getY() <= (option.getY() + option.getHeight())) {
						
						if (actionState != null) {
							if ((point.getX() < actionState.getX() || (point.getY() < actionState.getY() ||
									point.getY() > actionState.getY() + actionState.getImage().getHeight())) ||
									(point.getX() > actionState.getX() + actionState.getImage().getWidth() || (point.getY() < actionState.getY() ||
											point.getY() > actionState.getY() + actionState.getImage().getHeight()))) {
								selectedItem = i;
								audioPlayer.play("click");
								if (typeFilter == null) {
									ItemActionManager.createActionState(this, option);
								} else {
									OptionsState.getInstance().setSubState(new EquipmentState(option.getInventoryItem().getItem()));
								}
								
							}
						} else {
							selectedItem = i;
							audioPlayer.play("click");
							if (typeFilter == null) {
								ItemActionManager.createActionState(this, option);
							} else {
								OptionsState.getInstance().setSubState(new EquipmentState(option.getInventoryItem().getItem()));
							}
							
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

	public Option[] getActionOptions() {
		return actionOptions;
	}
	
}
