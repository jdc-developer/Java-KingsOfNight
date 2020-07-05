package jdc.kings.objects;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatus {
	
	private int level;
	
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
	
	public void useItem(Integer id) {
		InventoryItem inventoryItem = findItem(id);
		Item item = inventoryItem.getItem();
		
		if (item.getType() == Item.USABLE) {
			item.use();
			inventoryItem.decreaseQuantity();
			if (inventoryItem.getQuantity() == 0) {
				removeItem(id);
			}
		}
	}
	
	public void removeItem(Integer id) {
		InventoryItem item = findItem(id);
		items.remove(item);
	}
	
	public void equipItem(Item item, Integer slot) {
		if (!item.isEquipped()) {
			switch (item.getType()) {
				case Item.USABLE:
					if (slot == 1) {
						unequipItem(fastUsableOne, slot);
						fastUsableOne = item;
						fastUsableOne.setEquipped(true);
					} else if (slot == 2) {
						unequipItem(fastUsableTwo, slot);
						fastUsableTwo = item;
						fastUsableTwo.setEquipped(true);
					}
					break;
	
				case Item.HELMET:
					unequipItem(helmet, slot);
					helmet = item;
					helmet.setEquipped(true);
					break;
				case Item.ARMOR:
					unequipItem(armor, slot);
					armor = item;
					armor.setEquipped(true);
					break;
				case Item.GAUNTLETS:
					unequipItem(gauntlets, slot);
					gauntlets = item;
					gauntlets.setEquipped(true);
					break;
				case Item.GREAVES:
					unequipItem(greaves, slot);
					greaves = item;
					greaves.setEquipped(true);
					break;
				case Item.SWORD:
					unequipItem(sword, slot);
					sword = item;
					sword.setEquipped(true);
					break;
				case Item.SHIELD:
					unequipItem(shield, slot);
					shield = item;
					shield.setEquipped(true);
					break;
				case Item.RING:
					if (slot == 1) {
						unequipItem(ringOne, slot);
						ringOne = item;
						ringOne.setEquipped(true);
					} else if (slot == 2) {
						unequipItem(ringTwo, slot);
						ringTwo = item;
						ringTwo.setEquipped(true);
					}
					break;
			}
			
			item.equip();
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
		
		return item;
	}
	
	public void unequipItem(Item item, Integer slot) {
		if (item != null) {
			item.unequip();
			item.setEquipped(false);
			
			switch (item.getType()) {
				case Item.USABLE:
					if (slot == 1) {
						fastUsableOne = null;
					} else if (slot == 2) {
						fastUsableTwo = null;
					}
					break;
		
				case Item.HELMET:
					helmet = null;
					break;
				case Item.ARMOR:
					armor = null;
					break;
				case Item.GAUNTLETS:
					gauntlets = null;
					break;
				case Item.GREAVES:
					greaves= null;
					break;
				case Item.SWORD:
					sword = null;
					break;
				case Item.SHIELD:
					shield = null;
					break;
				case Item.RING:
					if (slot == 1) {
						ringOne = null;
					} else if (slot == 2) {
						ringTwo = null;
					}
					break;
			}
		}
	}

	public List<InventoryItem> getItems() {
		return items;
	}

	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}

	public Item getHelmet() {
		return helmet;
	}

	public void setHelmet(Item helmet) {
		this.helmet = helmet;
	}

	public Item getGauntlets() {
		return gauntlets;
	}

	public void setGauntlets(Item gauntlets) {
		this.gauntlets = gauntlets;
	}

	public Item getArmor() {
		return armor;
	}

	public void setArmor(Item armor) {
		this.armor = armor;
	}

	public Item getGreaves() {
		return greaves;
	}

	public void setGreaves(Item greaves) {
		this.greaves = greaves;
	}

	public Item getSword() {
		return sword;
	}

	public void setSword(Item sword) {
		this.sword = sword;
	}

	public Item getShield() {
		return shield;
	}

	public void setShield(Item shield) {
		this.shield = shield;
	}

	public Item getFastUsableOne() {
		return fastUsableOne;
	}

	public void setFastUsableOne(Item fastUsableOne) {
		this.fastUsableOne = fastUsableOne;
	}

	public Item getFastUsableTwo() {
		return fastUsableTwo;
	}

	public void setFastUsableTwo(Item fastUsableTwo) {
		this.fastUsableTwo = fastUsableTwo;
	}

	public Item getRingOne() {
		return ringOne;
	}

	public void setRingOne(Item ringOne) {
		this.ringOne = ringOne;
	}

	public Item getRingTwo() {
		return ringTwo;
	}

	public void setRingTwo(Item ringTwo) {
		this.ringTwo = ringTwo;
	}

	public int getLevel() {
		return level;
	}

	public PlayerStatus() {
		super();
	}
	
}
