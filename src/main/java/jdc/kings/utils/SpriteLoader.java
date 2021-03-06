package jdc.kings.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import jdc.kings.objects.GameObject;

public class SpriteLoader {
	
	private static SpriteLoader instance;
	private HashMap<String, BufferedImage[][]> spritesArray = new HashMap<>();
	
	private SpriteLoader() {};
	
	public static SpriteLoader getInstance() {
		if (instance == null) {
			instance = new SpriteLoader();
		}
		return instance;
	}
	
	public void loadSprites(String key, BufferedImage[][] spritesToAdd) {
		BufferedImage[][] sprites = spritesArray.get(key);
		
		if (sprites == null) {
			spritesArray.put(key, spritesToAdd);
		}
	}
	
	public BufferedImage[][] getSprites(String key) {
		return spritesArray.get(key);
	}
	
	public BufferedImage[] getAction(String key, int actionKey) {
		BufferedImage[][] sprites = spritesArray.get(key);
		return sprites[actionKey];
	}
	
	public BufferedImage[] loadAction(String path, GameObject object, int startFrom, int goTo, int startRow, int endRow,
			int gap1, int gap2, int frameWidth, int frameHeight, int plusWidth, int plusHeight) {
		BufferedImage[] bi = null;
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass()
					.getResourceAsStream(path));
			bi = new BufferedImage[(goTo - startFrom) * (endRow - startRow)];
			
			int count = 0;
			for (int i = startRow; i < endRow; i++) {
				for (int j = startFrom; j < goTo; j++) {
					int gap = gap1;
					 if (j == 1) {
						gap += + gap2;
					} else if (j > 1) {
						gap = gap2 * j + gap1;
					}
					BufferedImage temp = spriteSheet.getSubimage(
							j * frameWidth + gap, 
							i * frameHeight,
							frameWidth,
							frameHeight);
					if (object != null) {
						bi[count] = resize(temp, object.getWidth() + plusWidth, object.getHeight() + plusHeight);
					} else {
						bi[count] = resize(temp, frameWidth + plusWidth, frameHeight + plusHeight);
					}
					
					count++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bi;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	public void clear() {
		spritesArray.clear();
	}

}