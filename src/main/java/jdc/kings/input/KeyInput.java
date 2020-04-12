package jdc.kings.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.Player;

public class KeyInput extends KeyAdapter {
	
	private static KeyInput instance;
	private Player player;
	private List<Key> keys = new ArrayList<>();
	
	private KeyInput() {
		Key key1 = new Key(KeyEvent.VK_W, KeyAction.UP, false);
		Key key2 = new Key(KeyEvent.VK_S, KeyAction.DOWN, false);
		Key key3 = new Key(KeyEvent.VK_A, KeyAction.LEFT, false);
		Key key4 = new Key(KeyEvent.VK_D, KeyAction.RIGHT, false);
		Key key5 = new Key(KeyEvent.VK_SPACE, KeyAction.JUMP, false);
		Key key6 = new Key(KeyEvent.VK_K, KeyAction.STABBING, false);
		Key key7 = new Key(KeyEvent.VK_J, KeyAction.CUTTING, false);
		Key key8 = new Key(KeyEvent.VK_H, KeyAction.SLICING, false);
		Key key9 = new Key(KeyEvent.VK_U, KeyAction.ROLLING, false);
		Key key10 = new Key(KeyEvent.VK_I, KeyAction.SHIELD, false);
		
		keys.addAll(Arrays.asList(key1, key2, key3, key4, key5, key6, key7, key8, key9, key10));
	}
	
	public static KeyInput getInstance() {
		if (instance == null) {
			instance = new KeyInput();
		}
		return instance;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	private Key findKey(int keyPressed) {
		for (Key key : keys) {
			if (key.getMapping() == keyPressed) {
				return key;
			}
		}
		return null;
	}
	
	public Key findKey(KeyAction action) {
		for (Key key : keys) {
			if (key.getAction() == action) {
				return key;
			}
		}
		return null;
	}
	
	public void keyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(true);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setRight(true);
			}
			if (action == KeyAction.LEFT) {
				player.setLeft(true);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(true);
			}
			if (action == KeyAction.STABBING) {
				player.setStabbing(true);
			}
			if (action == KeyAction.CUTTING) {
				player.setCutting(true);
			}
			if (action == KeyAction.SLICING) {
				player.setSlicing(true);
			}
			if (action == KeyAction.ROLLING) {
				player.setRolling(true);
			}
			if (action == KeyAction.SHIELD) {
				player.setShield(true);
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(false);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setRight(false);
			}
			if (action == KeyAction.LEFT) {
				player.setLeft(false);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(false);
			}
			if (action == KeyAction.SHIELD) {
				player.setShield(false);
			}
		}
	}

}
