package jdc.kings.view;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import jdc.kings.Game;
import jdc.kings.utils.Constants;

public abstract class Window extends Canvas {
	
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	public static void createWindow() {
		frame = new JFrame(Constants.TITLE);
		
		Game game = Game.getInstance();
		Dimension dimension = new Dimension(Constants.WIDTH, Constants.HEIGHT);
		
		frame.setPreferredSize(dimension);
		frame.setMaximumSize(dimension);
		frame.setMinimumSize(dimension);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}

	public static JFrame getFrame() {
		return frame;
	}

}
