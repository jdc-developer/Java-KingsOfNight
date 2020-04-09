package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import jdc.kings.objects.enums.ObjectType;
import jdc.kings.view.Animator;

public class Player extends GameObject {

	public Player(int x, int y, ObjectType type, Animator animator) {
		super(x, y, type, animator);
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		if (animator != null) {
			animator.update(System.currentTimeMillis());
			g.drawImage(animator.getSprite(), (int)x, (int)y, null);
		}
	}

	public Rectangle getBounds() {
		return null;
	}

}
