package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.ImageUtil;
import jdc.kings.utils.JumpUtil;
import jdc.kings.view.Handler;

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
		collision();
	}
	
	private void collision() {
		Handler handler = Handler.getInstance();
		for (int i = 0; i < handler.getObjects().size(); i++) {
			GameObject tempObject = handler.getObjects().get(i);
			if (tempObject.getType() == ObjectType.PLAYER) {
				JumpUtil jumpUtil = JumpUtil.getInstance();
				Player player = (Player) tempObject;
				if (getBounds().intersects(tempObject.getBounds())) {
					player.setFalling(false);
				}
				
				if (player.isFalling()) {
					player.setVelY(5);
				} else if (!jumpUtil.isJumping()) {
					player.setVelY(0);
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, (int)x, (int)y, null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 17, 16);
	}

}
