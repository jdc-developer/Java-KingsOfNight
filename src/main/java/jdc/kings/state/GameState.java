package jdc.kings.state;

import java.awt.Graphics;

import jdc.kings.objects.Player;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.view.Background;
import jdc.kings.view.HUD;

public abstract class GameState {
	
	protected Background background;
	protected Player player;
	protected String bgMusic;
	protected HUD hud;
	protected AudioPlayer audioPlayer = AudioPlayer.getInstance();
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void closeMusic() {
		audioPlayer.close(bgMusic);
		audioPlayer.removeClip(bgMusic);
	}

}
