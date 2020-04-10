package jdc.kings.utils;

public class AttackUtil {
	
	private static AttackUtil instance;
	
	private boolean finish = false;
	private boolean attacking = false;
	private int count;
	
	private AttackUtil() {};
	
	public static AttackUtil getInstance() {
		if (instance == null) {
			instance = new AttackUtil();
		}
		return instance;
	}
	
	public void setAttack() {
		finish = false;
		attacking = true;
		count = 0;
	}
	
	public void attack() {
		count++;
		
		if (count == 60) {
			finish = true;
			attacking = false;
		}
	}

	public boolean isFinish() {
		return finish;
	}

	public boolean isAttacking() {
		return attacking;
	}

}
