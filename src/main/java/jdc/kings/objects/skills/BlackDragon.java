package jdc.kings.objects.skills;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class BlackDragon extends Item {

	public BlackDragon(TileMap tm) {
		super(tm);
		try {
			name = "blackDragonName";
			description = "blackDragonDescription";
			type = SKILL;
			
			id = 1;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("black-dragon") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				BufferedImage temp = ImageIO.read(getClass().getResourceAsStream("/sprites/skills/black-dragon/black-dragon.png"));
				sprites[0][0] = SpriteLoader.resize(temp, 32, 32);
				spriteLoader.loadSprites("black-dragon", sprites);
			}
			
			image = spriteLoader.getAction("black-dragon", 0)[0];
			menuImage = ImageIO.read(getClass().getResourceAsStream("/sprites/skills/black-dragon/black-dragon-menu.png"));
			animator = new Animator(spriteLoader.getAction("black-dragon", 0));
			animator.setSpeed(120);
			animator.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unequip() {
		// TODO Auto-generated method stub
		
	}

}
