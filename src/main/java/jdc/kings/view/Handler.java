package jdc.kings.view;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Map;

import jdc.kings.objects.GameObject;
import jdc.kings.objects.enums.ObjectAction;

public class Handler {

	private LinkedList<GameObject> objects = new LinkedList<GameObject>();
	private static Handler instance;
	
	private Handler() {};
	
	public static Handler getInstance() {
		if (instance == null) {
			instance = new Handler();
		}
		return instance;
	}
	
	public void tick() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			ObjectAction action = tempObject.getAction();
			ObjectAction previousAction = tempObject.getPreviousAction();
			
			Map<ObjectAction, Animator> actionAnimations = tempObject.getActionAnimations();
			Animator animator = actionAnimations.get(tempObject.getAction());
			
			if (previousAction != null && previousAction != action) {
				Animator previousAnimator = actionAnimations.get(previousAction);
				if (previousAnimator.isRunning()) {
					previousAnimator.stop();
				}
			}
			
			if (animator != null) {
				if (!animator.isRunning()) {
					animator.start();
				}
				animator.update(System.currentTimeMillis());
				g.drawImage(animator.getSprite(), (int)tempObject.getX(), (int)tempObject.getY(), null);
			}
		}
	}
	
	public void addObject(GameObject object) {
		this.objects.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.objects.remove(object);
	}
	
	public LinkedList<GameObject> getObjects() {
		return objects;
	}
	
}
