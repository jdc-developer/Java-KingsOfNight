package jdc.kings.state.options;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.objects.Item;
import jdc.kings.objects.PlayerStatus;
import jdc.kings.state.GameState;
import jdc.kings.state.LevelManager;
import jdc.kings.state.interfaces.KeyState;
import jdc.kings.state.interfaces.MouseState;
import jdc.kings.state.objects.Option;
import jdc.kings.utils.BundleUtil;

public class SkillState extends GameState implements KeyState, MouseState {
	
	private static final int ROWS = 5;
	private static final int COLS = 4;
	
	private BufferedImage image;
	private BufferedImage selectedItemImage;
	
	private PlayerStatus inventory;
	private ActionState actionState;
	private DescriptionState descriptionState;
	
	private List<Item> skills;
	private LinkedList<Option> options = new LinkedList<>();
	private Option[] actionOptions = new Option[2];
	
	private String title;
	private String equip;
	private String unequip;
	private String description;
	
	private Font font;
	private int selectedItem;
	private int lastItemsSize;
	
	public SkillState() {
		try {
			font = new Font("Arial", Font.BOLD, 16);
			image = ImageIO.read(getClass().getResourceAsStream("/game/menu-skills.png"));
			selectedItemImage = ImageIO.read(getClass().getResourceAsStream("/game/glass.png"));
			inventory = player.getStatus();
			
			for (int i = 0; i < actionOptions.length; i++) {
				actionOptions[i] = new Option(null, 120, 18);
			}
			
			skills = player.getStatus().getSkills();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		Locale locale = Game.getInstance().getPreferences().getLocale();
		title = BundleUtil.getMessageResourceString("menuOptionTwo", locale);
		
		equip = BundleUtil.getMessageResourceString("itemOptionThree", locale);
		description = BundleUtil.getMessageResourceString("itemOptionTwo", locale);
		unequip = BundleUtil.getMessageResourceString("itemOptionFive", locale);
		
		if (skills.size() != lastItemsSize) {
			getContent();
		}
		
		if (descriptionState != null) {
			descriptionState.tick();
		}
		
		if (descriptionState != null || actionState != null) {
			LevelManager.getCurrentLevel().setEscEnabled(false);
		} else {
			LevelManager.getCurrentLevel().setEscEnabled(true);
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, 380, 70, image.getWidth(), image.getHeight(), null);
		
		Locale locale = Game.getInstance().getPreferences().getLocale();
		g.setFont(font);
		if (locale.getCountry().equals("US") && locale.getLanguage().equals("en")) {
			g.drawString(title, 497, 95);
		} else {
			g.drawString(title, 475, 95);
		}
		
		for (int i = 0; i < options.size(); i++) {
			Option option = options.get(i);
			Item item = option.getItem();
			g.drawImage(item.getImage(), (int)option.getX(), (int)option.getY(), Item.SIZE, Item.SIZE, null);
			
			if (i == selectedItem) {
				g.drawImage(selectedItemImage, (int)option.getX() + 1, (int)option.getY() + 1, 29, 29, null);
			}
		}
		
		if (actionState != null) {
			actionState.render(g);
		}
		
		if (descriptionState != null) {
			descriptionState.render(g);
		}
	}
	
	private void getContent() {
		int count = skills.size();
		lastItemsSize = count;
		
		int col = 0;
		int row = 0;
		for (int i = 0; i < count; i++) {
			Item skill = skills.get(i);
			Option option = new Option(null, 32, 32);
			
			int x = 408 + (col * 60);
			int y = 152 + (row * 56);
			
			option.setItem(skill);
			option.setX(x);
			option.setY(y);
			options.add(option);
			
			col++;
			if (col == COLS) {
				row++;
				col = 0;
				if (row == ROWS) {
					row = 0;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (descriptionState != null) {
			descriptionState.keyPressed(e);
		} else if (actionState != null) {
			actionState.keyPressed(e);
		} else {
			activeKeyPressed(e);
		}
	}
	
	private void activeKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_RIGHT) {
			audioPlayer.play("switch");
			selectedItem++;
			
			if (selectedItem >= options.size()) {
				selectedItem = 0;
			}
		}
		
		if (key == KeyEvent.VK_LEFT) {
			audioPlayer.play("switch");
			selectedItem--;
			
			if (selectedItem < 0) {
				selectedItem = options.size() - 1;
 			}
		}
		
		if (key == KeyEvent.VK_UP) {
			audioPlayer.play("switch");
			selectedItem -= COLS;
			
			if (selectedItem < 0) {
				int availableCols = (int) Math.ceil(options.size() / COLS);
				selectedItem += COLS + (availableCols * COLS);
				
				if (selectedItem >= options.size()) {
					selectedItem -= COLS;
				}
			}
		}
		
		if (key == KeyEvent.VK_DOWN) {
			audioPlayer.play("switch");
			selectedItem += COLS;
			
			if (selectedItem >= options.size()) {
				int availableCols = (int) Math.ceil(options.size() / COLS);
				selectedItem -= COLS + (availableCols * COLS);
				
				if (selectedItem < 0) {
					selectedItem += COLS;
				}
			}
		}
		
		if (key == KeyEvent.VK_ENTER) {
			Option option = options.get(selectedItem);
			audioPlayer.play("click");
			Item skill = inventory.getSkill();
			if (skill != null && skill.getId() == option.getItem().getId()) {
				ItemActionManager.createSkillActionState(this, option, equip, unequip, description, false);
			} else {
				ItemActionManager.createSkillActionState(this, option, equip, unequip, description, true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (descriptionState != null) {
			descriptionState.mousePressed(e);
		} else {
			activeMousePressed(e);
		}
	}
	
	private void activeMousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			
			if (actionState != null) {
				if ((point.getX() < actionState.getX() || (point.getY() < actionState.getY() ||
						point.getY() > actionState.getY() + actionState.getImage().getHeight())) ||
						(point.getX() > actionState.getX() + actionState.getImage().getWidth() || (point.getY() < actionState.getY() ||
								point.getY() > actionState.getY() + actionState.getImage().getHeight()))) {
					actionState = null;
				}
			}
			
			for (int i = 0; i < options.size(); i++) {
				Option option = options.get(i);
				if (point.getX() >= option.getX() && point.getX() <= (option.getX() + option.getWidth())
						&& point.getY() >= option.getY() && point.getY() <= (option.getY() + option.getHeight())) {
						
						if (actionState != null) {
							if ((point.getX() < actionState.getX() || (point.getY() < actionState.getY() ||
									point.getY() > actionState.getY() + actionState.getImage().getHeight())) ||
									(point.getX() > actionState.getX() + actionState.getImage().getWidth() || (point.getY() < actionState.getY() ||
											point.getY() > actionState.getY() + actionState.getImage().getHeight()))) {
								selectedItem = i;
								audioPlayer.play("click");
								
								Item skill = inventory.getSkill();
								if (skill != null && skill.getId() == option.getItem().getId()) {
									ItemActionManager.createSkillActionState(this, option, equip, unequip, description, false);
								} else {
									ItemActionManager.createSkillActionState(this, option, equip, unequip, description, true);
								}
							}
						} else {
							selectedItem = i;
							audioPlayer.play("click");

							Item skill = inventory.getSkill();
							if (skill != null && skill.getId() == option.getItem().getId()) {
								ItemActionManager.createSkillActionState(this, option, equip, unequip, description, false);
							} else {
								ItemActionManager.createSkillActionState(this, option, equip, unequip, description, true);
							}
						}
					}
			}
		}
		
		if (actionState != null) {
			actionState.mousePressed(e);
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
		if (actionState != null) {
			actionState.mouseMoved(e);
		}
	}

	public void setActionState(ActionState actionState) {
		this.actionState = actionState;
	}

	public void setDescriptionState(DescriptionState descriptionState) {
		this.descriptionState = descriptionState;
	}
	
	public Option[] getActionOptions() {
		return actionOptions;
	}

}
