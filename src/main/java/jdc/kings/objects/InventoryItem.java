package jdc.kings.objects;

public class InventoryItem {
	
	private Item item;
	private int quantity;
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void increaseQuantity() {
		this.quantity++;
	}
	
	public void decreaseQuantity() {
		this.quantity--;
	}

	public InventoryItem(Item item, int quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}

	public InventoryItem() {
		super();
	}

}
