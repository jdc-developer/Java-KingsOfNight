package jdc.kings.objects;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
	
	private List<InventoryItem> items = new ArrayList<>();

	public List<InventoryItem> getItems() {
		return items;
	}

	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}
	
	public InventoryItem findItem(int id) {
		InventoryItem item = null;
		for (int i = 0; i < items.size(); i++) {
			InventoryItem searchItem = items.get(i);
			if (searchItem.getItem().getId() == id) {
				item = searchItem;
			}
		}
		return item;
	}

	public Inventory() {
		super();
	}
	
}
