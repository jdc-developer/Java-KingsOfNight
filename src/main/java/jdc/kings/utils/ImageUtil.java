package jdc.kings.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import jdc.kings.objects.GameObject;
import jdc.kings.objects.enums.ObjectAction;
import jdc.kings.view.Animator;

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
		return sprite.getSubimage((row * width) - width, (col * height) - height, width, height);
	}
	
	public void mirrorAnimations(GameObject object, ObjectAction actionToMirror, ObjectAction newAction, int speed) {
		Map<ObjectAction, Animator> actionAnimations = object.getActionAnimations();
		Animator animatorToMirror = actionAnimations.get(actionToMirror);
		List<BufferedImage> mirrorImages = new ArrayList<>();
		for (BufferedImage image : animatorToMirror.getFrames()) {
			int width = image.getWidth();
			int height = image.getHeight();
			
			BufferedImage mImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
	        for(int xx = width-1;xx>0;xx--){
	            for(int yy = 0;yy < height;yy++){
	            	mImg.setRGB(width-xx, yy, image.getRGB(xx, yy));
	            }
	        }
	        mirrorImages.add(mImg);
		}
		Animator mirroredAnimator = new Animator(mirrorImages);
		mirroredAnimator.setSpeed(speed);
		object.getActionAnimations().put(newAction, mirroredAnimator);
	}
	
	public void bufferAnimations(GameObject object, ObjectAction action, String path, int width, int height,
			int frames, int speed) {
		ImageUtil imageLoader = ImageUtil.getInstance();
		List<BufferedImage> images = new ArrayList<>();
		imageLoader.loadImage(path);
		for (int i = 1; i <= frames; i++) {
			BufferedImage image = imageLoader.grabImage(1, i, width, height);
			images.add(image);
		}
		Animator animator = new Animator(images);
		animator.setSpeed(100);
		object.getActionAnimations().put(action, animator);
	}

}