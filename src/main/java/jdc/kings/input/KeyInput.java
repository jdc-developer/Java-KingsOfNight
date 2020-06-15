package jdc.kings.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.Player;
import jdc.kings.state.MenuState;
import jdc.kings.state.StateManager;

public class KeyInput extends KeyAdapter {
	
	private static KeyInput instance;
	private StateManager manager;
	private Player player;	
	private List<Key> keys = new ArrayList<>();
	
	private KeyInput() {
		manager = StateManager.getInstance();
		
		Key key1 = new Key(KeyEvent.VK_D, KeyAction.RIGHT, false);
		Key key2 = new Key(KeyEvent.VK_A, KeyAction.LEFT, false);
		Key key3 = new Key(KeyEvent.VK_U, KeyAction.ROLLING, false);
		Key key4 = new Key(KeyEvent.VK_I, KeyAction.SHIELD, false);
		Key key5 = new Key(KeyEvent.VK_H, KeyAction.SLICING, false);
		Key key6 = new Key(KeyEvent.VK_J, KeyAction.CUTTING, false);
		Key key7 = new Key(KeyEvent.VK_K, KeyAction.STABBING, false);
		Key key8 = new Key(KeyEvent.VK_SPACE, KeyAction.JUMP, false);
		
		keys.addAll(Arrays.asList(key1, key2, key3, key4, key5, key6, key7, key8));
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
	
	public List<Key> getKeys() {
		return keys;
	}
	
	public void keyPressed(KeyEvent e) {
		if (manager.getCurrentState() == StateManager.MENU) {
			MenuState menu = (MenuState) manager.getState();
			menu.keyPressed(e);
		} else {
			inGameKeyPressed(e);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (manager.getCurrentState() != StateManager.MENU) {
			inGameKeyReleased(e);
		}
	}
	
	public void inGameKeyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null && !player.isDead()) {
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
	
	public void inGameKeyReleased(KeyEvent e) {
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
