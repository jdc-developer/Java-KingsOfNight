package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdc.kings.objects.Item;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class LeatherGloves extends Item {

	public LeatherGloves(TileMap tm) {
		super(tm);
		try {
			name = "leatherGlovesName";
			description = "leatherGlovesDescription";
			type = GAUNTLETS;
			
			id = 5;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("leather-gloves") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/leather-gloves.png"));
				spriteLoader.loadSprites("leather-gloves", sprites);
			}
			
			image = spriteLoader.getAction("leather-gloves", 0)[0];
			animator = new Animator(spriteLoader.getAction("leather-gloves", 0));
			animator.setSpeed(120);
			animator.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getNextPosition() {
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (falling) {
			velY += fallSpeed;
			
			if (velY > 0) jumping = false;
			if (velY < 0 && !jumping) velY += stopJumpSpeed;
			
			if (velY > maxFallSpeed) velY = maxFallSpeed;
		}
	}
	
	@Override
	public void tick() {
		getNextPosition();
		super.tick();
	}

	@Override
	public void use() {
	}

	@Override
	public void equip() {
		player.addArmorBonus(0.8f);
	}

	@Override
	public void unequip() {
		player.removeArmorBonus(0.8f);
	}

}
