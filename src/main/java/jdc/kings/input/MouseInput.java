package jdc.kings.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import jdc.kings.state.MapState;
import jdc.kings.state.StateManager;
import jdc.kings.state.interfaces.MouseState;

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
		try {
			MouseState state = (MouseState) manager.getState();
			state.mouseDragged(e);
		}catch (java.lang.ClassCastException ex) {
			// TODO: handle exception
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		try {
			MouseState state = (MouseState) manager.getState();
			state.mouseMoved(e);
		}catch (ClassCastException ex) {
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
