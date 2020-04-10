package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.ImageUtil;
import jdc.kings.utils.JumpUtil;
import jdc.kings.view.Animator;

public class Player extends GameObject {

	public Player(int x, int y, ObjectType type) {
		super(x, y, type);
		bufferAnimations(ObjectAction.IDLE, "/player/idle.png", 130, 130, 15);
		bufferAnimations(ObjectAction.RUN, "/player/run.png", 225, 150, 8);
		bufferAnimations(ObjectAction.JUMP, "/player/jump.png", 361, 150, 14);
	}
	
	private void bufferAnimations(ObjectAction action, String path, int width, int height,
			int frames) {
		ImageUtil imageLoader = ImageUtil.getInstance();
		List<BufferedImage> images = new ArrayList<>();
		imageLoader.loadImage(path);
		for (int i = 1; i <= frames; i++) {
			BufferedImage image = imageLoader.grabImage(1, i, width, height);
			images.add(image);
		}
		Animator jumpAnimator = new Animator(images);
		jumpAnimator.setSpeed(100);
		actionAnimations.put(action, jumpAnimator);
	}

	public void tick() {
		x += velX;
		y += velY;
		
		if (action == ObjectAction.JUMP) {
			JumpUtil jumpUtil = JumpUtil.getInstance();
			if (!jumpUtil.isJumping()) {
				jumpUtil.setJump(this);
			}
			jumpUtil.jump(this);
			if (jumpUtil.isFinish() && !jumpUtil.isJumping()) {
				if (velX > 0) {
					changeAction(ObjectAction.RUN);
				} else {
					changeAction(ObjectAction.IDLE);
				}
			}
		} else if (velX == 0 && velY == 0) {
			changeAction(ObjectAction.IDLE);
		} else if (velX != 0) {
			changeAction(ObjectAction.RUN);
		}
	}

	public void render(Graphics g) {

	}

	public Rectangle getBounds() {
		return null;
	}

}
