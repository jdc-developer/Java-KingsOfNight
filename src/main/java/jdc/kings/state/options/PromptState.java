package jdc.kings.state.options;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.state.GameState;

public class PromptState extends GameState {
	
	private BufferedImage image;
	private String message;
	private Font font;
	
	public PromptState(String message) {
		try {
			this.message = message;
			font = new Font("Arial", Font.PLAIN, 14);
			image = ImageIO.read(getClass().getResourceAsStream("/game/prompt.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 417, 142, image.getWidth(), image.getHeight(), null);
		g.setFont(font);
		g.drawString(message, 440, 177);	
	}

}
