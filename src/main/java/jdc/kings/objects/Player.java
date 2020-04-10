package jdc.kings.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.AttackUtil;
import jdc.kings.utils.ImageUtil;
import jdc.kings.utils.JumpUtil;
import jdc.kings.view.Animator;

public class Player extends GameObject {

	public Player(int x, int y, ObjectType type) {
		super(x, y, type);
		bufferAnimations(ObjectAction.IDLE_FRONT, "/player/idle.png", 210, 140, 15, 100);
		bufferAnimations(ObjectAction.RUN_FRONT, "/player/run.png", 225, 150, 8, 100);
		bufferAnimations(ObjectAction.JUMP_FRONT, "/player/jump.png", 361, 150, 14, 190);
		bufferAnimations(ObjectAction.JUMP_BACK, "/player/jump_back.png", 361, 150, 14, 190);
		bufferAnimations(ObjectAction.ATTACK, "/player/attack.png", 249, 140, 9, 100);
		mirrorAnimation(ObjectAction.IDLE_FRONT, ObjectAction.IDLE_BACK, 100);
		mirrorAnimation(ObjectAction.RUN_FRONT, ObjectAction.RUN_BACK, 100);
		mirrorAnimation(ObjectAction.JUMP_BACK, ObjectAction.JUMP_BACK, 190);
	}
	
	private void mirrorAnimation(ObjectAction actionToMirror, ObjectAction newAction, int speed) {
		Animator animatorToMirror = actionAnimations.get(actionToMirror);
		List<BufferedImage> mirrorImages = new ArrayList<>();
		for (BufferedImage image : animatorToMirror.getFrames()) {
			int width = image.getWidth();
			int height = image.getHeight();
			
			BufferedImage mImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
	        for(int xx = width-1;xx>0;xx--){
	            for(int yy = 0;yy < height;yy++){
	            	mImg.setRGB(width-xx, yy, image.getRGB(xx, yy));
	            }
	        }
	        mirrorImages.add(mImg);
		}
		Animator mirroredAnimator = new Animator(mirrorImages);
		mirroredAnimator.setSpeed(speed);
		actionAnimations.put(newAction, mirroredAnimator);
	}
	
	private void bufferAnimations(ObjectAction action, String path, int width, int height,
			int frames, int speed) {
		ImageUtil imageLoader = ImageUtil.getInstance();
		List<BufferedImage> images = new ArrayList<>();
		imageLoader.loadImage(path);
		for (int i = 1; i <= frames; i++) {
			BufferedImage image = imageLoader.grabImage(1, i, width, height);
			images.add(image);
		}
		Animator animator = new Animator(images);
		animator.setSpeed(100);
		actionAnimations.put(action, animator);
	}

	public void tick() {
		x += velX;
		y += velY;
		
		if (action == ObjectAction.ATTACK) {
			AttackUtil attackUtil = AttackUtil.getInstance();
			if (!attackUtil.isAttacking()) {
				attackUtil.setAttack();
			}
			attackUtil.attack();
			if (attackUtil.isFinish()) {
				perspectiveAction();
			}
			
		} else if (action == ObjectAction.JUMP_FRONT || action == ObjectAction.JUMP_BACK) {
			JumpUtil jumpUtil = JumpUtil.getInstance();
			if (!jumpUtil.isJumping()) {
				jumpUtil.setJump(this);
			}
			jumpUtil.jump(this);
			if (jumpUtil.isFinish() && !jumpUtil.isJumping()) {
				perspectiveAction();
			}
			
		} else if (velX == 0 && velY == 0) {
			perspectiveAction();
		} else if (velX > 0) {
			changeAction(ObjectAction.RUN_FRONT);
		} else if (velX < 0) {
			changeAction(ObjectAction.RUN_BACK);
		}
	}

	public void render(Graphics g) {
	}

	public Rectangle getBounds() {
		return null;
	}

}
