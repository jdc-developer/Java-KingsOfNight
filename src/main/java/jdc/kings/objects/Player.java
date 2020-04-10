package jdc.kings.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.AttackUtil;
import jdc.kings.utils.ImageUtil;
import jdc.kings.utils.JumpUtil;
import jdc.kings.view.Handler;

public class Player extends GameObject {
	
	private boolean falling = true;

	public Player(int x, int y, ObjectType type) {
		super(x, y, type);
		ImageUtil imageUtil = ImageUtil.getInstance();
		
		imageUtil.bufferAnimations(this, ObjectAction.IDLE_FRONT, "/player/idle.png", 210, 140, 15, 100);
		imageUtil.bufferAnimations(this, ObjectAction.RUN_FRONT, "/player/run.png", 225, 150, 8, 100);
		imageUtil.bufferAnimations(this, ObjectAction.JUMP_FRONT, "/player/jump.png", 361, 150, 14, 190);
		imageUtil.bufferAnimations(this, ObjectAction.JUMP_BACK, "/player/jump_back.png", 361, 150, 14, 190);
		imageUtil.bufferAnimations(this, ObjectAction.ATTACK_FRONT, "/player/attack.png", 249, 140, 9, 100);
		imageUtil.bufferAnimations(this, ObjectAction.ATTACK_BACK, "/player/attack_back.png", 249, 140, 9, 100);
		imageUtil.bufferAnimations(this, ObjectAction.AIR_ATTACK_FRONT, "/player/attack.png", 249, 140, 9, 100);
		imageUtil.bufferAnimations(this, ObjectAction.AIR_ATTACK_BACK, "/player/attack_back.png", 249, 140, 9, 100);
		
		imageUtil.mirrorAnimations(this, ObjectAction.IDLE_FRONT, ObjectAction.IDLE_BACK, 100);
		imageUtil.mirrorAnimations(this, ObjectAction.RUN_FRONT, ObjectAction.RUN_BACK, 100);
		imageUtil.mirrorAnimations(this, ObjectAction.JUMP_BACK, ObjectAction.JUMP_BACK, 190);
		imageUtil.mirrorAnimations(this, ObjectAction.ATTACK_BACK, ObjectAction.ATTACK_BACK, 100);
		imageUtil.mirrorAnimations(this, ObjectAction.AIR_ATTACK_BACK, ObjectAction.ATTACK_BACK, 100);
	
		animated = true;
	}

	public void tick() {
		x += velX;
		y += velY;
		
		JumpUtil jumpUtil = JumpUtil.getInstance();
		AttackUtil attackUtil = AttackUtil.getInstance();
		
		if (action == ObjectAction.AIR_ATTACK_FRONT || action == ObjectAction.AIR_ATTACK_BACK) {
			jumpUtil.jump(this);
			attackUtil.attack();
			
			if (jumpUtil.isFinish() && !jumpUtil.isJumping()) {
				perspectiveAction();
			}
			if (attackUtil.isFinish()) {
				perspectiveAction();
			}
		} else if (action == ObjectAction.ATTACK_FRONT || action == ObjectAction.ATTACK_BACK) {
			if (jumpUtil.isJumping()) {
				if (perspective == 0) {
					changeAction(ObjectAction.AIR_ATTACK_FRONT);
				} else {
					changeAction(ObjectAction.AIR_ATTACK_BACK);
				}
			}
			
			if (!attackUtil.isAttacking()) {
				attackUtil.setAttack();
			}
			attackUtil.attack();
			if (attackUtil.isFinish()) {
				perspectiveAction();
			}
			
		} else if (action == ObjectAction.JUMP_FRONT || action == ObjectAction.JUMP_BACK) {
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
		
		if (falling) {
			velY = 5;
		} else if (!jumpUtil.isJumping()) {
			velY = 0;
		}
		
		collision();
	}
	
	private void collision() {
		Handler handler = Handler.getInstance();
		for (int i = 0; i < handler.getObjects().size(); i++) {
			GameObject tempObject = handler.getObjects().get(i);
			if (tempObject.getType() == ObjectType.BLOCK) {
				if (getBounds().intersects(tempObject.getBounds())) {
					falling = false;
				}
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.cyan);
		g.drawRect((int)x + 89, (int)y + 24, 46, 70);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x + 89, (int)y + 24, 46, 70);
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

}
