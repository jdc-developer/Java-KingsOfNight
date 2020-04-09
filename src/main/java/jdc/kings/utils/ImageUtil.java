package jdc.kings.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	private static ImageUtil instance;
	private BufferedImage sprite;
	
	private ImageUtil() {};
	
	public static ImageUtil getInstance() {
		if (instance == null) {
			instance = new ImageUtil();
		}
		return instance;
	}
	
	public BufferedImage loadImage(String path) {
		try {
			sprite = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprite;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height) {
		return sprite.getSubimage((row * 64) - 64, (col * 64) - 64, width, height);
	}

}