package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class IliSword extends Item {

	public IliSword(TileMap tm) {
		super(tm);
		try {
			name = "iliSwordName";
			description = "iliSwordDescription";
			type = SWORD;
			
			id = 8;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("ili-sword") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/ili-sword.png"));
				spriteLoader.loadSprites("ili-sword", sprites);
			}
			
			image = spriteLoader.getAction("ili-sword", 0)[0];
			animator = new Animator(spriteLoader.getAction("ili-sword", 0));
			animator.setSpeed(120);
			animator.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void use() {
	}

	@Override
	public void equip() {
		player.addAttackBonus(4.2f);
	}

	@Override
	public void unequip() {
		player.removeAttackBonus(4.2f);
	}

}
