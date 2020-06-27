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
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.objects.Inventory;
import jdc.kings.objects.InventoryItem;
import jdc.kings.objects.Item;
import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.utils.BundleUtil;

public class ItemState extends GameState implements KeyState, MouseState {
	
	private static ItemState instance;
	private static final int ROWS = 8;
	private static final int COLS = 6;
	public static final int PAGES = 3;
	public static final int ITEMSPERPAGE = ROWS * COLS;
	
	private BufferedImage image;
	private BufferedImage arrow;
	private BufferedImage inverseArrow;
	private Font font;
	private Font quantityFont;
	private String title;
	
	private int page = 0;
	private int availablePages;
	
	public static ItemState getInstance() {
		if (instance == null) {
			instance = new ItemState();
		}
		return instance;
	}
	
	private ItemState() {
		try {
			font = new Font("Arial", Font.BOLD, 16);
			quantityFont = new Font("Arial", Font.PLAIN, 12);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-items.png"));
			
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionOne", locale);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		g.drawImage(arrow, 423, 440, arrow.getWidth(), arrow.getHeight(), null);
		g.drawImage(inverseArrow, 406, 440, inverseArrow.getWidth(), inverseArrow.getHeight(), null);
		
		g.setFont(font);
		g.drawString(title, 500, 95);
		
		Inventory invetory = player.getInventory();
		List<InventoryItem> items = invetory.getItems();
		
		int count = items.size();
		availablePages = (int) Math.floor(count / ITEMSPERPAGE);
		
		int col = 0;
		int row = 0;
		for (int i = 0; i < count; i++) {
			if (i == ITEMSPERPAGE) {
				continue;
			}
			
			int index = i + (page * ITEMSPERPAGE);
			InventoryItem inventoryItem = items.get(index);
			Item item = inventoryItem.getItem();
			
			g.drawImage(item.getImage(), 408 + (col * 38), 136 + (row * 36), null);
			g.setFont(quantityFont);
			g.drawString(String.valueOf(inventoryItem.getQuantity()), 433 + (col * 38), 146 + (row * 36));
			
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
		// TODO Auto-generated method stub
		
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
			
			if (point.getX() >= 423 && point.getX() <= 439 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page < availablePages - 1) {
					page++;
				}
			}
			
			if (point.getX() >= 406 && point.getX() <= 422 && point.getY() >= 440 && point.getY() <= 456) {
				audioPlayer.play("click");
				if (page > 0) {
					page--;
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

}
