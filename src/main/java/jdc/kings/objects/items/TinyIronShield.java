package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class TinyIronShield extends Item {

	public TinyIronShield(TileMap tm) {
		super(tm);
		try {
			name = "tinyIronShieldName";
			description = "tinyIronShieldDescription";
			type = SHIELD;
			
			id = 7;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("iron-shield") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/iron-shield.png"));
				spriteLoader.loadSprites("iron-shield", sprites);
			}
			
			image = spriteLoader.getAction("iron-shield", 0)[0];
			animator = new Animator(spriteLoader.getAction("iron-shield", 0));
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
		player.addShieldBonus(3.3f);
	}

	@Override
	public void unequip() {
		player.removeShieldBonus(3.3f);
	}

}
