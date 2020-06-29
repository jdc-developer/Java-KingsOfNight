package jdc.kings.state;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.MapLevel;
import jdc.kings.utils.Constants;

public class MapState extends GameState implements KeyState, MouseState {
	
	private BufferedImage image;
	private BufferedImage cursor;
	private BufferedImage star;
	private List<MapLevel> levels = new ArrayList<>();
	
	private float x;
	private float y;
	private float velX;
	private float velY;
	
	private float moveSpeed;
	private float maxSpeed;
	private float stopSpeed;
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	
	private Point mousePoint;
	private boolean mousePressed;
	private int selectedLevel;
	
	public MapState() {
		try {
			moveSpeed = 6f;
			maxSpeed = 7f;
			stopSpeed = 0.4f;
			
			levels.add(new MapLevel(930, 1145, false));
			levels.add(new MapLevel(880, 1245, false));
			selectedLevel = 0;
			
			image = ImageIO.read(getClass().getResourceAsStream("/backgrounds/map.jpg"));
			cursor = ImageIO.read(getClass().getResourceAsStream("/game/cursor.png"));
			star = ImageIO.read(getClass().getResourceAsStream("/game/star.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		StateManager.getInstance().showLoader(false);
		if (right) {
			velX -= moveSpeed;
			if (velX < -maxSpeed) {
				velX = -maxSpeed;
			}
		} else if (left ) {
			velX += moveSpeed;
			if (velX > maxSpeed) {
				velX = maxSpeed;
			}
		} else {
			if (velX > 0) {
				velX -= stopSpeed;
				if (velX < 0) {
					velX = 0;
				}
			} else if (velX < 0) {
				velX += stopSpeed;
				if (velX > 0) {
					velX = 0;
				}
			}
		}
		
		if (up) {
			velY += moveSpeed;
			if (velY > maxSpeed) {
				velY = maxSpeed;
			}
		} else if (down) {
			velY -= moveSpeed;
			if (velY < -maxSpeed) {
				velY = -maxSpeed;
			}
		} else {
			if (velY > 0) {
				velY -= stopSpeed;
				if (velY < 0) {
					velY = 0;
				}
			} else if (velY < 0) {
				velY += stopSpeed;
				if (velY > 0) {
					velY = 0;
				}
			}
		}
		
		x += velX;
		if (x > 0) {
			x = 0;
		}
		
		if (x + image.getWidth() <= Constants.WIDTH) {
			int value = image.getWidth() - Constants.WIDTH;
			x = -value;
		}
		
		y += velY;
		if (y > 0) {
			y = 0;
		}
		
		if (y + image.getHeight() <= Constants.HEIGHT) {
			int value = image.getHeight() - Constants.HEIGHT;
			y = -value;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, image.getWidth(), image.getHeight(), null);
		
		for (int i = 0;i < levels.size(); i++) {
			MapLevel level = levels.get(i);
			if (!level.isLocked()) {
				g.drawImage(star, (int)(level.getX() + x), (int)(level.getY() + y), star.getWidth(), star.getHeight(), null);
			}
			
			if (i == selectedLevel) {
				g.drawImage(cursor, (int)(level.getX() + 22 + x), (int)(level.getY() + 20 + y), 50, 60, null);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UP) {
			findClosestLevel(0);
		}
		
		if (key == KeyEvent.VK_DOWN) {
			findClosestLevel(2);
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			findClosestLevel(1);
		}
		
		if (key == KeyEvent.VK_LEFT) {
			findClosestLevel(3);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	private void findClosestLevel(int direction) {
		float minDisX = 0;
		float minDisY = 0;
		int closestLevel = 0;
		
		for (int i = 0;i < levels.size(); i++) {
			MapLevel level = levels.get(i);
			
			float disX = 0;
			float disY = 0;
			
			if (direction == 1) {
				disX = x - level.getX();
			} else if (direction == 3) {
				disX = x + level.getX();
			}
			
			if (direction == 0) {
				disY = y + level.getY();
			} else if (direction == 2) {
				disY = y - level.getY();
			}
			
			if (i == 0) {
				minDisX = disX;
				minDisY = disY;
				closestLevel = i;
			}
			
			if (disX < minDisX) {
				minDisX = disX;
				closestLevel = i;
			}
			
			if (disY < minDisY) {
				minDisY = disY;
				closestLevel = i;
			}
		}
		
		selectedLevel = closestLevel;
	}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			mousePoint = e.getPoint();
			mousePressed = true;
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		Point point = e.getPoint();
		
		if (mousePoint != null && mousePressed) {
			double x = mousePoint.getX();
			double y = mousePoint.getY();
			
			double actualX = point.getX();
			double actualY = point.getY();
			
			double disX = actualX - x;
			double disY = actualY - y;
			
			if (disX >= 2) {
				left = true;
				right = false;
			} else if (disX <= -2) {
				right = true;
				left = false;
			}
			
			if (disY >= 2) {
				down = false;
				up = true;
			} else if (disY <= -2) {
				up = false;
				down = true;
			}
			
			mousePoint = point;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			mousePressed = false;
			right = left = up = down = false;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
