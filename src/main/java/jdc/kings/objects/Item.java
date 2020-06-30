package jdc.kings.objects;

import java.awt.image.BufferedImage;

import jdc.kings.state.options.ItemState;
import jdc.kings.view.TileMap;

public class Item extends GameObject {
	
	protected Integer id;
	protected String name;
	protected String description;
	protected int type;
	
	protected BufferedImage image;
	protected Player player;
	private boolean shouldRemove;
	
	public static final int USABLE = 0;
	public static final int HELMET = 1;
	public static final int ARMOR = 2;
	public static final int GAUNTLETS = 3;
	public static final int GREAVES = 4;
	public static final int SWORD = 5;
	public static final int SHIELD = 6;
	public static final int RING = 7;
	public static final int KEY = 8;

	public Item(TileMap tm) {
		super(tm);
		audioPlayer.loadAudio("get-item", "/sfx/menu/item.mp3");
	}
	
	@Override
	public void tick() {
		super.tick();
		if (intersects(player) && !player.isDying() && !player.isDead()) {
			Inventory inventory = player.getInventory();
			InventoryItem searchItem = inventory.findItem(id);
			
			if (inventory.getItems().size() < ItemState.ITEMSPERPAGE * ItemState.PAGES) {
				if (searchItem == null) {
					audioPlayer.play("get-item");
					InventoryItem inventoryItem = new InventoryItem(this, 1);
					inventory.getItems().add(inventoryItem);
					shouldRemove = true;
				} else if (searchItem.getQuantity() < 99) {
					searchItem.increaseQuantity();
					shouldRemove = true;
				}
			}
		}
	}

	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
