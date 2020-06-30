package jdc.kings.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdc.kings.state.StateManager;
import jdc.kings.state.interfaces.KeyState;

public class KeyInput extends KeyAdapter {
	
	private static KeyInput instance;
	private StateManager manager;
	private List<Key> keys = new ArrayList<>();
	
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int ROLL = 2;
	public static final int SHIELD = 3;
	public static final int SLICE = 4;
	public static final int CUT = 5;
	public static final int STAB = 6;
	public static final int JUMP = 7;
	
	private KeyInput() {
		manager = StateManager.getInstance();
		defaultKeys();
	}
	
	public static KeyInput getInstance() {
		if (instance == null) {
			instance = new KeyInput();
		}
		return instance;
	}
	
	public void defaultKeys() {
		keys.clear();
		Key key1 = new Key(KeyEvent.VK_D, RIGHT, false);
		Key key2 = new Key(KeyEvent.VK_A, LEFT, false);
		Key key3 = new Key(KeyEvent.VK_U, ROLL, false);
		Key key4 = new Key(KeyEvent.VK_I, SHIELD, false);
		Key key5 = new Key(KeyEvent.VK_H, SLICE, false);
		Key key6 = new Key(KeyEvent.VK_J, CUT, false);
		Key key7 = new Key(KeyEvent.VK_K, STAB, false);
		Key key8 = new Key(KeyEvent.VK_SPACE, JUMP, false);
		
		keys.addAll(Arrays.asList(key1, key2, key3, key4, key5, key6, key7, key8));
	}

	public Key findByKey(int keyPressed) {
		for (Key key : keys) {
			if (key.getMapping() == keyPressed) {
				return key;
			}
		}
		return null;
	}
	
	public Key findByAction(int action) {
		for (Key key : keys) {
			if (key.getAction() == action) {
				return key;
			}
		}
		return null;
	}
	
	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	
	public List<Key> getKeys() {
		return keys;
	}
	
	public void keyPressed(KeyEvent e) {
		if (manager.getCurrentState() != StateManager.CLEAR) {
			KeyState state = (KeyState) manager.getState();
			state.keyPressed(e);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (manager.getCurrentState() != StateManager.CLEAR) {
			KeyState state = (KeyState) manager.getState();
			state.keyReleased(e);
		}
	}

}
