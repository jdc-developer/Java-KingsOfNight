package jdc.kings.input;

import java.io.Serializable;

public class Key implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int mapping;
	private int action;
	private boolean pressed;
	
	public int getMapping() {
		return mapping;
	}
	
	public void setMapping(int mapping) {
		this.mapping = mapping;
	}
	
	public int getAction() {
		return action;
	}
	
	public void setAction(int action) {
		this.action = action;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public Key(int mapping, int action, boolean pressed) {
		super();
		this.mapping = mapping;
		this.action = action;
		this.pressed = pressed;
	}

}
