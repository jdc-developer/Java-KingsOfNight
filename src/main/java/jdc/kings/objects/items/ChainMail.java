package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class ChainMail extends Item {

	public ChainMail(TileMap tm) {
		super(tm);
		try {
			name = "chainMailName";
			description = "chainMailDescription";
			type = ARMOR;
			
			id = 3;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("chain-mail") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/chain-mail.png"));
				spriteLoader.loadSprites("chain-mail", sprites);
			}
			
			image = spriteLoader.getAction("chain-mail", 0)[0];
			animator = new Animator(spriteLoader.getAction("chain-mail", 0));
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
		player.addArmorBonus(5f);
	}

	@Override
	public void unequip() {
		player.removeArmorBonus(5f);
	}

}
