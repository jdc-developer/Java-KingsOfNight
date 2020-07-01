package jdc.kings.objects;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
	
	private Item helmet;
	private Item gauntlets;
	private Item armor;
	private Item greaves;
	private Item sword;
	private Item shield;
	private Item fastUsableOne;
	private Item fastUsableTwo;
	private Item ringOne;
	private Item ringTwo;
	
	private List<InventoryItem> items = new ArrayList<>();
	
	public InventoryItem findItem(Integer id) {
		InventoryItem item = null;
		for (int i = 0; i < items.size(); i++) {
			InventoryItem searchItem = items.get(i);
			if (searchItem.getItem().getId() == id) {
				item = searchItem;
			}
		}
		return item;
	}
	
	public void removeItem(Integer id) {
		InventoryItem item = findItem(id);
		items.remove(item);
	}
	
	public void equipItem(Item item, Integer slot) {
		switch (item.getType()) {
			case Item.USABLE:
				if (slot == 1) {
					unequipItem(fastUsableOne);
					fastUsableOne = item;
					fastUsableOne.setEquipped(true);
				} else if (slot == 2) {
					unequipItem(fastUsableTwo);
					fastUsableTwo = item;
					fastUsableTwo.setEquipped(true);
				}
				break;

			case Item.HELMET:
				unequipItem(helmet);
				helmet = item;
				helmet.setEquipped(true);
				break;
			case Item.ARMOR:
				unequipItem(armor);
				armor = item;
				armor.setEquipped(true);
				break;
			case Item.GAUNTLETS:
				unequipItem(gauntlets);
				gauntlets = item;
				gauntlets.setEquipped(true);
				break;
			case Item.GREAVES:
				unequipItem(greaves);
				greaves = item;
				greaves.setEquipped(true);
				break;
			case Item.SWORD:
				unequipItem(sword);
				sword = item;
				sword.setEquipped(true);
				break;
			case Item.SHIELD:
				unequipItem(shield);
				shield = item;
				shield.setEquipped(true);
				break;
			case Item.RING:
				if (slot == 1) {
					unequipItem(ringOne);
					ringOne = item;
					ringOne.setEquipped(true);
				} else if (slot == 2) {
					unequipItem(ringTwo);
					ringTwo = item;
					ringTwo.setEquipped(true);
				}
				break;
		}
	}
	
	public Item getEquippedItem(Integer type, Integer slot) {
		Item item = null;
		switch (type) {
			case Item.USABLE:
				if (slot == 1) {
					item = fastUsableOne;
				} else if (slot == 2) {
					item = fastUsableTwo;
				}
				break;
	
			case Item.HELMET:
				item = helmet;
				break;
			case Item.ARMOR:
				item = armor;
				break;
			case Item.GAUNTLETS:
				item = gauntlets;
				break;
			case Item.GREAVES:
				item = greaves;
				break;
			case Item.SWORD:
				item = sword;
				break;
			case Item.SHIELD:
				item = shield;
				break;
			case Item.RING:
				if (slot == 1) {
					item = ringOne;
				} else if (slot == 2) {
					item = ringTwo;
				}
				break;
		}
		
		if (item != null) {
			item = item.isEquipped() ? item : null;
		}
		
		return item;
	}
	
	public void unequipItem(Item item) {
		if (item != null) {
			item.setEquipped(false);
		}
	}

	public List<InventoryItem> getItems() {
		return items;
	}

	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}

	public Inventory() {
		super();
	}
	
}
