package jdc.kings.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import jdc.kings.utils.AudioPlayer;
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
	
	private Map<String, AudioPlayer> sfx;

	public MenuState(StateManager manager) {
		this.manager = manager;
		try {
			background = new Background("/backgrounds/menu-bg.jpg", 1);
			
			titleFont = new Font("Century Gothic", Font.PLAIN, 68);
			font = new Font("Arial", Font.PLAIN, 32);
			
			bgMusic = new AudioPlayer("/music/blood-ritual.mp3");
			bgMusic.loop();
			
			sfx = new HashMap<>();
			sfx.put("switch", new AudioPlayer("/sfx/switch.mp3"));
			sfx.put("click", new AudioPlayer("/sfx/click.mp3"));
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
			sfx.get("click").play();
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
			sfx.get("switch").play();
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			sfx.get("switch").play();
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
}
