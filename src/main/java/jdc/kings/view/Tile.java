package jdc.kings.view;

import java.awt.image.BufferedImage;

/**
 * 
 * @author ForeignGuyMike
 * {@link https://www.youtube.com/watch?v=qJpdRFvSj1A}
 *
 */
public class Tile {
	
	private BufferedImage image;
	private int type;
	
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}

}
