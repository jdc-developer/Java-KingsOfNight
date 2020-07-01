package jdc.kings.state;

import java.awt.Graphics2D;

import jdc.kings.objects.Player;
import jdc.kings.utils.AudioPlayer;
import jdc.kings.view.Background;
import jdc.kings.view.HUD;

public abstract class GameState {
	
	protected Background background;
	protected static Player player;
	protected String bgMusic;
	protected HUD hud;
	protected static AudioPlayer audioPlayer = AudioPlayer.getInstance();
	
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
	public void closeMusic() {
		audioPlayer.close(bgMusic);
		audioPlayer.removeClip(bgMusic);
	}
	
	public static Player getPlayer() {
		return player;
	}

}
