package jdc.kings.objects;

import java.awt.image.BufferedImage;

import jdc.kings.state.options.ItemState;
import jdc.kings.view.TileMap;

public class Item extends GameObject {
	
	protected Integer id;
	protected String description;
	protected BufferedImage image;
	protected Player player;
	private boolean shouldRemove;

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
