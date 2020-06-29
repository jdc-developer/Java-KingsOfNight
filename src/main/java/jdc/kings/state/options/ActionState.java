package jdc.kings.state.options;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;

public class ActionState extends GameState implements MouseState {
	
	private BufferedImage image;
	private Option[] itemOptions;
	private int selected;
	
	private Font font;
	private Color selectedBackground = new Color(61, 11, 11);
	
	private float x;
	private float y;
	
	public ActionState(Option[] itemOptions, float x, float y) {
		try {
			this.itemOptions = itemOptions;
			this.x = x;
			this.y = y;
			
			image = ImageIO.read(getClass().getResourceAsStream("/game/action-menu.png"));
			font = new Font("Arial", Font.PLAIN, 14);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, image.getWidth(), image.getHeight(), null);
		
		for (int i = 0; i < itemOptions.length; i++) {
			Option itemOption = itemOptions[i];
			g.setFont(font);
			
			if (i == selected) {
				g.setComposite(AlphaComposite.getInstance(
			            AlphaComposite.SRC_OVER, 0.6f));
				g.setColor(selectedBackground);
				g.fillRect((int)x + 15, (int)y + 16 + (20 * i), (int)itemOption.getWidth(),
						(int)itemOption.getHeight());
			}
			
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, 1f));
			
			itemOption.setX(x + 20);
			itemOption.setY(y + 30 + (20 * i));
			g.drawString(itemOption.getDescription(), itemOption.getX(),itemOption.getY());
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			for (int i = 0; i < itemOptions.length; i++) {
				Option option = itemOptions[i];
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
					&& point.getY() >= option.getY() - 15 && point.getY() <= (option.getY() + option.getHeight())) {
					audioPlayer.play("click");
					option.getAction().callAction();
				}
			}
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = e.getPoint();
		
		for (int i = 0; i < itemOptions.length; i++) {
			Option option = itemOptions[i];
			if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
					&& (point.getY() >= option.getY() - 15 && point.getY() <= option.getY() + option.getHeight())) {
				selected = i;
			}
		}
	}

}
