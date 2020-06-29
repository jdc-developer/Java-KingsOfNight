package jdc.kings.state.objects;

import jdc.kings.objects.InventoryItem;

public class Option {
	
	private InventoryItem inventoryItem;
	private Action<?, ?> action;
	private String description;
	private float x;
	private float y;
	private float width;
	private float height;
	
	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public Action<?, ?> getAction() {
		return action;
	}

	public void setAction(Action<?, ?> action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Option(String description, float width, float height) {
		super();
		this.description = description;
		this.width = width;
		this.height = height;
	}

	public Option() {
		super();
	}

}
