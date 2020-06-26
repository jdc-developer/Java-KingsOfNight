package jdc.kings.state;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Locale;

import jdc.kings.Game;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.utils.BundleUtil;

public class DeathState extends GameState implements KeyState {
	
	private StateManager manager;
	private float alpha = 0.0f;
	
	private String[] options = new String[2];
	private int currentChoice = 0;
	private String deathMessage;
	
	private Font titleFont;
	private Font font;
	private Color color = new Color(205, 0, 0);
	
	public DeathState() {
		manager = StateManager.getInstance();
		titleFont = new Font("Century Gothic", Font.PLAIN, 68);
		font = new Font("Arial", Font.PLAIN, 25);
		
		bgMusic = "deathMusic";
		audioPlayer.loadAudio(bgMusic, "/music/cold-as-ice.mp3");
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		String deathOptionOne = BundleUtil.getMessageResourceString("deathOptionOne", locale);
		String deathOptionTwo = BundleUtil.getMessageResourceString("deathOptionTwo", locale);
		deathMessage = BundleUtil.getMessageResourceString("deathMessage", locale);
		options[0] = deathOptionOne;
		options[1] = deathOptionTwo;
		
		audioPlayer.loadAudio("switch", "/sfx/menu/switch.mp3");
		audioPlayer.loadAudio("click", "/sfx/menu/click.mp3");
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, alpha));
		
		g2d.setFont(titleFont);
		g2d.setColor(color);
		g2d.drawString(deathMessage, 240, 170);
		
		g2d.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.white);
			} else {
				g.setColor(color);
			}
			
			g.drawString(options[i], 245, 220 + i * 30);
		}
		 
		alpha += 0.003f;
	    if (alpha >= 1.0f) {
	        alpha = 1.0f;
	    }
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (alpha >= 0.8f) {
			deathAction(key);
		}
	}
	
	private void deathAction(int key) {
		if (key == KeyEvent.VK_ENTER) {
			audioPlayer.play("click");
			manager.getState().closeMusic();
			audioPlayer.stop(bgMusic);
			switch(currentChoice) {
				case 0:
					manager.showLoader(true);
					manager.setState(manager.getCurrentState());
					break;
				case 1:
					manager.setNextState(StateManager.MENU);
					manager.setState(StateManager.CLEAR);
					break;
			}
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	public void loopMusic() {
		audioPlayer.loop(bgMusic);
	}

}
