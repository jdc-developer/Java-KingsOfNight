package jdc.kings.utils;

import jdc.kings.objects.Player;

public class JumpUtil {
	
	private static JumpUtil instance;
	
	private float position;
	private boolean jumping = false;
	private boolean finish = false;
	
	private JumpUtil() {};
	
	public static JumpUtil getInstance() {
		if (instance == null) {
			instance = new JumpUtil();
		}
		return instance;
	}
	
	public void setJump(Player player) {
		this.position = player.getY();
		player.setVelY(-5);
		jumping = true;
		finish = false;
	}
	
	public void jump(Player player) {
		if (player.getY() == position - 120) {
			player.setVelY(5);
			finish = true;
		}
		if (finish) {
			if (player.getY() == position) {
				player.setVelY(0);
				jumping = false;
			}
		}
	}

	public boolean isJumping() {
		return jumping;
	}

	public boolean isFinish() {
		return finish;
	}

}
