package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.view.Animator;

public abstract class GameObject {
	
	protected float x, y;
	protected ObjectType type;
	protected float velX, velY;
	protected int perspective = 0;
	protected boolean animated = false;
	protected ObjectAction action;
	protected ObjectAction previousAction;
	protected Map<ObjectAction, Animator> actionAnimations = new HashMap<ObjectAction, Animator>();
	
	public GameObject(float x, float y, ObjectType type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
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

	public Map<ObjectAction, Animator> getActionAnimations() {
		return actionAnimations;
	}

	public void setActionAnimations(Map<ObjectAction, Animator> actionAnimations) {
		this.actionAnimations = actionAnimations;
	}
	
	public void changeAction(ObjectAction action) {
		if (this.action != action && this.action != null) {
			previousAction = this.action;
		}
		this.action = action;
	}
	
	protected void perspectiveAction() {
		if (perspective == 0) {
			changeAction(ObjectAction.IDLE_FRONT);
		} else {
			changeAction(ObjectAction.IDLE_BACK);
		}
	}

	public ObjectAction getAction() {
		return action;
	}

	public ObjectAction getPreviousAction() {
		return previousAction;
	}

	public int getPerspective() {
		return perspective;
	}

	public void setPerspective(int perspective) {
		this.perspective = perspective;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

}
