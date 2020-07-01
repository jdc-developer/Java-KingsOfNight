package jdc.kings.state.objects;

import jdc.kings.objects.InventoryItem;
import jdc.kings.objects.Item;
import jdc.kings.state.options.ActionState;

public class Option {
	
	private InventoryItem inventoryItem;
	private Item item;
	private int type;
	private Action<?, ?> action;
	private ActionState prompt;
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Action<?, ?> getAction() {
		return action;
	}

	public void setAction(Action<?, ?> action) {
		this.action = action;
	}

	public ActionState getPrompt() {
		return prompt;
	}

	public void setPrompt(ActionState prompt) {
		this.prompt = prompt;
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
