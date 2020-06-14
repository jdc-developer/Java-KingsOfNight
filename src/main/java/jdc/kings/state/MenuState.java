package jdc.kings.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import jdc.kings.view.Background;

public class MenuState extends GameState {
	
	private StateManager manager;
	private int currentChoice = 0;
	private String[] options = {
			"Iniciar",
			"Opções",
			"Sair"
	};
	
	private Font titleFont;
	private Font font;

	public MenuState(StateManager manager) {
		this.manager = manager;
		try {
			background = new Background("/backgrounds/menu-bg.jpg", 1);
			
			titleFont = new Font("Century Gothic", Font.PLAIN, 68);
			font = new Font("Arial", Font.PLAIN, 32);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		background.tick();
	}

	@Override
	public void render(Graphics g) {
		background.render(g);
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		g.drawString("Kings of Night", 280, 170);
		
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.white);
			}
			
			g.drawString(options[i], 455, 240 + i * 45);
		}
	}
	
	private void select() {
		if (currentChoice == 0) {
			manager.setState(StateManager.LEVELONE);
		}
		if (currentChoice == 1) {
			
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ENTER) {
			select();
		}
		
		if (key == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
}
