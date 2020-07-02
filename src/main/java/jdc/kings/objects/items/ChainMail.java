package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.objects.Item;
import jdc.kings.utils.BundleUtil;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class ChainMail extends Item {

	public ChainMail(TileMap tm) {
		super(tm);
		try {
			Locale locale = Game.getInstance().getPreferences().getLocale();
			name = BundleUtil.getMessageResourceString("chainMailName", locale);
			description = BundleUtil.getMessageResourceString("chainMailDescription", locale);
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

}
