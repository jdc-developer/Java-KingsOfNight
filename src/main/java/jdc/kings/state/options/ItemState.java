package jdc.kings.state.options;

import java.awt.AlphaComposite;
import java.awt.Color;
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
	private Option[] itemOptions = new Option[5];
	
	private BufferedImage image;
	private BufferedImage arrow;
	private BufferedImage inverseArrow;
	private BufferedImage actionMenuImage;
	private BufferedImage selectedItemImage;
	
	private Font font;
	private Font quantityFont;
	private Font optionsFont;
	private Point actionPoint;
	private Color selectedBackground = new Color(61, 11, 11);
	private String title;
	
	private int lastItemsSize;
	private int page = 0;
	private int availablePages;
	private int selectedItem;
	private int selectedItemOption;
	
	public ItemState() {
		try {
			font = new Font("Arial", Font.BOLD, 16);
			quantityFont = new Font("Arial", Font.PLAIN, 12);
			optionsFont = new Font("Arial", Font.PLAIN, 14);
			
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-items.png"));
			actionMenuImage = ImageIO.read(getClass().getResourceAsStream("/game/action-menu.png"));
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
			
			for (int i = 0; i < itemOptions.length; i++) {
				itemOptions[i] = new Option(null, 120, 18);
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
		String itemOptionFive = BundleUtil.getMessageResourceString("itemOptionFive", locale);
		
		itemOptions[0].setDescription(itemOptionOne);
		itemOptions[1].setDescription(itemOptionTwo);
		itemOptions[2].setDescription(itemOptionThree);
		itemOptions[3].setDescription(itemOptionFour);
		itemOptions[4].setDescription(itemOptionFive);
		
		if (items.size() > lastItemsSize) {
			getPageContent();
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		g.drawImage(arrow, 423, 440, arrow.getWidth(), arrow.getHeight(), null);
		g.drawImage(inverseArrow, 406, 440, inverseArrow.getWidth(), inverseArrow.getHeight(), null);
		
		g.setFont(font);
		g.drawString(title, 485, 95);
		
		for (int i = 0; i < options.size(); i++) {
			Option option = options.get(i);
			InventoryItem inventoryItem = option.getItem();
			Item item = inventoryItem.getItem();
			
			if (i == selectedItem) {
				g.drawImage(selectedItemImage, (int)option.getX(), (int)option.getY(), null);
			}
			
			g.drawImage(item.getImage(), (int)option.getX(), (int)option.getY(), null);
			g.setFont(quantityFont);
			g.drawString(String.valueOf(inventoryItem.getQuantity()), (int)option.getX() + 25, (int)option.getY() + 10);
		}
		
		if (actionPoint != null) {
			int x = (int)actionPoint.getX();
			int y = (int)actionPoint.getY();
			
			g.drawImage(actionMenuImage, x, y, actionMenuImage.getWidth(),
					actionMenuImage.getHeight(), null);
			
			for (int i = 0; i < itemOptions.length; i++) {
				Option itemOption = itemOptions[i];
				g.setFont(optionsFont);
				
				if (i == selectedItemOption) {
					g.setComposite(AlphaComposite.getInstance(
				            AlphaComposite.SRC_OVER, 0.6f));
					g.setColor(selectedBackground);
					g.fillRect(x + 15, y + 16 + (20 * i), (int)itemOption.getWidth(),
							(int)itemOption.getHeight());
				}
				
				g.setColor(Color.white);
				g.setComposite(AlphaComposite.getInstance(
			            AlphaComposite.SRC_OVER, 1f));
				
				itemOption.setX(x + 20);
				itemOption.setY(y + 30 + (20 * i));
				g.drawString(itemOption.getDescription(), itemOption.getX(),itemOption.getY());
			}
		}
	}
	
	private void getPageContent() {
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
			
			option.setItem(inventoryItem);
			option.setX(x);
			option.setY(y);
			options.add(option);
			
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
			actionPoint = new Point((int)option.getX() + 22, (int)option.getY() + 22);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int key = e.getButton();
		if (key == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			if (actionPoint != null) {
				if ((point.getX() < actionPoint.getX() || (point.getY() < actionPoint.getY() ||
						point.getY() > actionPoint.getY() + actionMenuImage.getHeight())) ||
						(point.getX() > actionPoint.getX() + actionMenuImage.getWidth() || (point.getY() < actionPoint.getY() ||
								point.getY() > actionPoint.getY() + actionMenuImage.getHeight()))) {
					actionPoint = null;
				}
			}
			
			if (point.getX() >= 423 && point.getX() <= 439 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page < availablePages) {
					page++;
					selectedItem = 0;
					options.clear();
					getPageContent();
				}
			}
			
			if (point.getX() >= 406 && point.getX() <= 422 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page > 0) {
					page--;
					selectedItem = 0;
					options.clear();
					getPageContent();
				}
			}
			
			for (int i = 0; i < options.size(); i++) {
				Option option = options.get(i);
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
						&& point.getY() >= option.getY() && point.getY() <= (option.getY() + option.getHeight())) {
						
						if (actionPoint != null) {
							if ((point.getX() < actionPoint.getX() || (point.getY() < actionPoint.getY() ||
									point.getY() > actionPoint.getY() + actionMenuImage.getHeight())) ||
									(point.getX() > actionPoint.getX() + actionMenuImage.getWidth() || (point.getY() < actionPoint.getY() ||
											point.getY() > actionPoint.getY() + actionMenuImage.getHeight()))) {
								audioPlayer.play("click");
								selectedItem = i;
								actionPoint = new Point((int)option.getX() + 22, (int)option.getY() + 22);
							}
						} else {
							audioPlayer.play("click");
							selectedItem = i;
							actionPoint = new Point((int)option.getX() + 22, (int)option.getY() + 22);
						}
					}
			}
			
			for (int i = 0; i < itemOptions.length; i++) {
				Option option = itemOptions[i];
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
					&& point.getY() >= option.getY() - 15 && point.getY() <= (option.getY() + option.getHeight())) {
					audioPlayer.play("click");
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
		Point point = e.getPoint();
		
		for (int i = 0; i < itemOptions.length; i++) {
			Option option = itemOptions[i];
			if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
					&& (point.getY() >= option.getY() - 15 && point.getY() <= option.getY() + option.getHeight())) {
				selectedItemOption = i;
			}
		}
	}

}
