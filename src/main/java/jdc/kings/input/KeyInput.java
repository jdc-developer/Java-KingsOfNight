package jdc.kings.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdc.kings.input.enums.KeyAction;
import jdc.kings.objects.Player;
import jdc.kings.utils.Constants;

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
		Key key6 = new Key(KeyEvent.VK_J, KeyAction.ATTACK, false);
		
		keys.addAll(Arrays.asList(key1, key2, key3, key4, key5, key6));
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
	
	public void keyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(true);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setVelX(Constants.PLAYER_SPEED);
				player.setRight(true);
			}
			if (action == KeyAction.LEFT) {
				player.setVelX(-Constants.PLAYER_SPEED);
				player.setLeft(true);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(true);
			}
			if (action == KeyAction.ATTACK) {
				player.setStabbing(true);
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		Key key = findKey(keyPressed);
		
		if (key != null) {
			key.setPressed(true);
			KeyAction action = key.getAction();
			if (action == KeyAction.RIGHT) {
				player.setVelX(0);
				player.setRight(false);
			}
			if (action == KeyAction.LEFT) {
				player.setVelX(0);
				player.setLeft(false);
			}
			if (action == KeyAction.JUMP) {
				player.setJumping(false);
			}
		}
	}

}
