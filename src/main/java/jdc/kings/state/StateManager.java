package jdc.kings.state;

import java.awt.Graphics;

public class StateManager {
	
	private static StateManager instance;
	
	private GameState[] gameStates;
	private int currentState;
	private boolean showLoader;
	
	public static final int STATES = 2;
	public static final int MENU = 0;
	public static final int LEVELONE = 1;
	
	private StateManager() {
		gameStates = new GameState[STATES];
		currentState = MENU;
		loadState(currentState);
	}
	
	public static StateManager getInstance() {
		if (instance == null) {
			instance = new StateManager();
		}
		return instance;
	}
	
	private void loadState(int state) {
		switch (state) {
		case MENU:
			gameStates[state] = new MenuState(this);
			break;
		case LEVELONE:
			gameStates[state] = LevelManager.loadLevelOne();
			break;
		default:
			gameStates[state] = new MenuState(this);
			break;
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public GameState getState() {
		return gameStates[currentState];
	}
	
	public void tick() {
		try {
			gameStates[currentState].tick();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void render(Graphics g) {
		try {
			gameStates[currentState].render(g);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void showLoader(boolean showLoader) {
		this.showLoader = showLoader;
	}
	
	public boolean showLoader() {
		return showLoader;
	}

}
