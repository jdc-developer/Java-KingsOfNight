package jdc.kings.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.ImageUtil;

public class Block extends GameObject {
	
	private BufferedImage image;

	public Block(float x, float y, ObjectType type) {
		super(x, y, type);
		ImageUtil imageUtil = ImageUtil.getInstance();
		imageUtil.loadImage("/level/tileset.png");
		image = imageUtil.grabImage(1, 1, 17, 16);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawRect((int)x, (int)y, 17, 16);
		g.drawImage(image, (int)x, (int)y, null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 17, 16);
	}

}
