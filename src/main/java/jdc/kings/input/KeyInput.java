package jdc.kings.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.GameObject;
import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.objects.enums.ObjectType;
import jdc.kings.utils.Constants;
import jdc.kings.view.Handler;

public class KeyInput extends KeyAdapter {
	
	private static KeyInput instance;
	private List<Key> keys = new ArrayList<>();
	
	private KeyInput() {
		Key key1 = new Key(KeyEvent.VK_W, KeyAction.UP, false);
		Key key2 = new Key(KeyEvent.VK_S, KeyAction.DOWN, false);
		Key key3 = new Key(KeyEvent.VK_A, KeyAction.LEFT, false);
		Key key4 = new Key(KeyEvent.VK_D, KeyAction.RIGHT, false);
		Key key5 = new Key(KeyEvent.VK_SPACE, KeyAction.JUMP, false);
		
		keys.addAll(Arrays.asList(key1, key2, key3, key4, key5));
	}
	
	public static KeyInput getInstance() {
		if (instance == null) {
			instance = new KeyInput();
		}
		return instance;
	}
	
	private Key findKey(int keyPressed) {
		for (Key key : keys) {
			if (key.getMapping() == keyPressed) {
				return key;
			}
		}
		return null;
	}
	
	public void keyPressed(KeyEvent e) {
		Handler handler = Handler.getInstance();
		List<GameObject> objects = handler.getObjects();
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(true);
			
			for (int i = 0; i < objects.size(); i++) {
				GameObject tempObject = objects.get(i);
				if (tempObject.getType() == ObjectType.PLAYER) {
					KeyAction action = key.getAction();
					
					if (action == KeyAction.UP) {
						tempObject.setVelY(-Constants.PLAYER_SPEED);
					}
					if (action == KeyAction.DOWN) {
						tempObject.setVelY(Constants.PLAYER_SPEED);
					}
					if (action == KeyAction.RIGHT) {
						tempObject.setVelX(Constants.PLAYER_SPEED);
					}
					if (action == KeyAction.LEFT) {
						tempObject.setVelX(-Constants.PLAYER_SPEED);
					}
					if (action == KeyAction.JUMP) {
						tempObject.changeAction(ObjectAction.JUMP);
					}
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		Handler handler = Handler.getInstance();
		List<GameObject> objects = handler.getObjects();
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(false);
			
			for (int i = 0; i < objects.size(); i++) {
				GameObject tempObject = objects.get(i);
				if (tempObject.getType() == ObjectType.PLAYER) {
					KeyAction action = key.getAction();
					
					if (action == KeyAction.UP || action == KeyAction.DOWN) {
						tempObject.setVelY(0);
					}
					if (action == KeyAction.RIGHT || action == KeyAction.LEFT) {
						tempObject.setVelX(0);
					}
				}
			}
		}
	}

}
