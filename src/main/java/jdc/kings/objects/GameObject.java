package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.view.Animator;

public abstract class GameObject {
	
	protected float x, y;
	protected ObjectType type;
	protected float velX, velY;
	protected Map<ObjectAction, List<BufferedImage>> actionImages = new HashMap<ObjectAction, List<BufferedImage>>();
	protected Animator animator;
	
	public GameObject(float x, float y, ObjectType type, Animator animator) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
		this.animator = animator;
		animator.setSpeed(100);
		animator.start();
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();

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

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public Map<ObjectAction, List<BufferedImage>> getActionImages() {
		return actionImages;
	}

	public void setActionImages(Map<ObjectAction, List<BufferedImage>> actionImages) {
		this.actionImages = actionImages;
	}

	public Animator getAnimator() {
		return animator;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

}
