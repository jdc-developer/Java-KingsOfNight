package jdc.kings.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import jdc.kings.state.MapState;
import jdc.kings.state.StateManager;

public class MouseInput extends MouseAdapter {
	
	private static MouseInput instance;
	private StateManager manager;
	
	private MouseInput() {
		manager = StateManager.getInstance();
	}
	
	public static MouseInput getInstance() {
		if (instance == null) {
			instance = new MouseInput();
		}
		return instance;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (manager.getCurrentState() == StateManager.MAP) {
			MapState map = (MapState) manager.getState();
			map.mousePressed(e);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (manager.getCurrentState() == StateManager.MAP) {
			MapState map = (MapState) manager.getState();
			map.mouseDragged(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (manager.getCurrentState() == StateManager.MAP) {
			MapState map = (MapState) manager.getState();
			map.mouseReleased(e);
		}
	}

}
