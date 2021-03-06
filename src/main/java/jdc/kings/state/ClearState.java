package jdc.kings.state;

import java.awt.Graphics2D;

import jdc.kings.utils.SpriteLoader;

public class ClearState extends GameState {
	
	private StateManager manager;
	private int nextState;
	private boolean clear = false;
	private boolean running = false;
	
	public ClearState(int nextState) {
		this.nextState = nextState;
		manager = StateManager.getInstance();
	}

	@Override
	public void tick() {
		if (!running) {
			running = true;
			manager.showLoader(true);
			Runnable clearAudioThread = new Runnable() {
				
				@Override
				public void run() {
					SpriteLoader.getInstance().clear();
					audioPlayer.clear();
					clear = true;
				}
			};
			new Thread(clearAudioThread).start();
		}
		
		if (clear) {
			manager.showLoader(false);
			manager.setState(nextState);
		}
		
	}

	@Override
	public void render(Graphics2D g) {
	}

}
