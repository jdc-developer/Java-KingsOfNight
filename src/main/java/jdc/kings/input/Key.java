package jdc.kings.input;

import jdc.kings.input.enums.KeyAction;

public class Key {

	private int mapping;
	private KeyAction action;
	private boolean pressed;
	
	public int getMapping() {
		return mapping;
	}
	
	public void setMapping(int mapping) {
		this.mapping = mapping;
	}
	
	public KeyAction getAction() {
		return action;
	}
	
	public void setAction(KeyAction action) {
		this.action = action;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public Key(int mapping, KeyAction action, boolean pressed) {
		super();
		this.mapping = mapping;
		this.action = action;
		this.pressed = pressed;
	}

}
