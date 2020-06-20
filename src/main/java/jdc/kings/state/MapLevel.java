package jdc.kings.state;

public class MapLevel {
	
	private float x;
	private float y;
	private boolean locked;
	
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
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public MapLevel(float x, float y, boolean locked) {
		super();
		this.x = x;
		this.y = y;
		this.locked = locked;
	}

	public MapLevel() {
		super();
	}

}
