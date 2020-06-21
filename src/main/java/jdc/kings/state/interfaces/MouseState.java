package jdc.kings.state.interfaces;

import java.awt.event.MouseEvent;

public interface MouseState {
	
	public void mousePressed(MouseEvent e);
	public void mouseDragged(MouseEvent e);
	public void mouseReleased(MouseEvent e);
	public void mouseMoved(MouseEvent e);

}
