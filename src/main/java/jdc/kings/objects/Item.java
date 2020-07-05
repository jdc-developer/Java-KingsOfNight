package jdc.kings.objects;

import java.awt.image.BufferedImage;

import jdc.kings.state.options.ItemState;
import jdc.kings.view.TileMap;

public abstract class Item extends GameObject {
	
	public static final int SIZE = 32;
	
	protected Integer id;
	protected String name;
	protected String description;
	protected int type;
	
	protected BufferedImage image;
	protected Player player;
	protected boolean shouldRemove;
	protected boolean equipped;
	
	public static final int USABLE = 0;
	public static final int HELMET = 1;
	public static final int ARMOR = 2;
	public static final int GAUNTLETS = 3;
	public static final int GREAVES = 4;
	public static final int SWORD = 5;
	public static final int SHIELD = 6;
	public static final int RING = 7;
	public static final int KEY = 8;
	public static final int SKILL = 9;

	public Item(TileMap tm) {
		super(tm);
		audioPlayer.loadAudio("get-item", "/sfx/menu/item.mp3");
	}
	
	private void getNextPosition() {
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (falling) {
			velY += fallSpeed;
			
			if (velY > 0) jumping = false;
			if (velY < 0 && !jumping) velY += stopJumpSpeed;
			
			if (velY > maxFallSpeed) velY = maxFallSpeed;
		}
	}
	
	@Override
	public void tick() {
		getNextPosition();
		super.tick();
		if (intersects(player) && !player.isDying() && !player.isDead()) {
			PlayerStatus inventory = player.getStatus();
			if (type == SKILL) {
				inventory.getSkills().add(this);
				shouldRemove = true;
			} else {
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
	}
	
	public abstract void use();
	public abstract void equip();
	public abstract void unequip();

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

	public int getType() {
		return type;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}
	
}
