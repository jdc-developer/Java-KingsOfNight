package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class StaminaPotion extends Item {

	public StaminaPotion(TileMap tm) {
		super(tm);
		try {
			name = "staminaPotionName";
			description = "staminaPotionDescription";
			type = USABLE;
			
			id = 2;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("stamina-potion") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/stamina-potion.png"));
				spriteLoader.loadSprites("stamina-potion", sprites);
			}
			
			image = spriteLoader.getAction("stamina-potion", 0)[0];
			animator = new Animator(spriteLoader.getAction("stamina-potion", 0));
			animator.setSpeed(120);
			animator.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void use() {
		player.setVigorBonus(0.3f);
		player.setShining(true);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				player.setVigorBonus(0);
				player.setShining(false);
			}
		};
		new Thread(runnable).start();
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
