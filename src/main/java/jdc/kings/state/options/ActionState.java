package jdc.kings.state.options;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import jdc.kings.state.GameState;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;

public class ActionState extends GameState implements KeyState, MouseState {
	
	private BufferedImage image;
	private Option[] itemOptions;
	private int selected;
	
	private Font font;
	private Color selectedBackground = new Color(61, 11, 11);
	private ActionState subActionState;
	private GameState parent;
	
	private String title;
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
		g.setFont(font);
		
		int addY = 16;
		if (title != null) {
			g.drawString(title, x + 15, y + addY + 15);
			addY += 26;
		}
		
		for (int i = 0; i < itemOptions.length; i++) {
			Option itemOption = itemOptions[i];
			
			if (i == selected) {
				g.setComposite(AlphaComposite.getInstance(
			            AlphaComposite.SRC_OVER, 0.6f));
				g.setColor(selectedBackground);
				g.fillRect((int)x + 15, (int)y + addY + (20 * i), (int)itemOption.getWidth(),
						(int)itemOption.getHeight());
			}
			
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, 1f));
			
			itemOption.setX(x + 20);
			itemOption.setY(y + addY + 14 + (20 * i));
			g.drawString(itemOption.getDescription(), itemOption.getX(),itemOption.getY());
		}
		
		if (subActionState != null) {
			subActionState.render(g);
		}
	}

	private void selectOption(Option option) {
		audioPlayer.play("click");
		if (option.getPrompt() != null) {
			subActionState = option.getPrompt();
		} else {
			option.getAction().callAction();
			closeActionMenu();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (subActionState != null) {
			subActionState.mousePressed(e);
		} else {
			activeMousePressed(e);
		}
	}
	
	private void activeMousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			for (int i = 0; i < itemOptions.length; i++) {
				Option option = itemOptions[i];
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
					&& point.getY() >= option.getY() - 15 && point.getY() <= (option.getY() + option.getHeight() - 15)) {
					selectOption(option);
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
		if (subActionState != null) {
			subActionState.mouseMoved(e);
		} else {
			activeMouseMoved(e);
		}
	}
	
	private void activeMouseMoved(MouseEvent e) {
		Point point = e.getPoint();
		
		for (int i = 0; i < itemOptions.length; i++) {
			Option option = itemOptions[i];
			if ((point.getX() >= option.getX() && point.getX() <= option.getX() + option.getWidth())
					&& (point.getY() >= option.getY() - 15 && point.getY() <= option.getY() + option.getHeight())) {
				selected = i;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (subActionState != null) {
			subActionState.keyPressed(e);
		} else {
			activeKeyPressed(e);
		}
	}
	
	private void activeKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			selected--;
			if (selected < 0) {
				selected = 0;
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			selected++;
			if (selected > itemOptions.length - 1) {
				selected = itemOptions.length - 1;
			}
		}
		
		if (key == KeyEvent.VK_ENTER) {
			Option option = itemOptions[selected];
			selectOption(option);
		}
		
		if (key == KeyEvent.VK_ESCAPE) {
			closeActionMenu();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void closeActionMenu() {
		try {
			Method method = parent.getClass().getMethod("setActionState", ActionState.class);
			ActionState actionState = null;
			method.invoke(parent, actionState);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public void setActionState(ActionState subActionState) {
		this.subActionState = subActionState;
	}

	public void setParent(GameState parent) {
		this.parent = parent;
	}

}
