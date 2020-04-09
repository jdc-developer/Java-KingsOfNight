package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import jdc.kings.objects.enums.ObjectType;

public class Player extends GameObject {

	public Player(int x, int y, ObjectType type) {
		super(x, y, type);
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
	}

	public Rectangle getBounds() {
		return null;
	}

}
