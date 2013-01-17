package group150.helper;

import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Random;

import group150.controller.GameManager;

import group150.model.Skin;

public class ImageLoader {
	private static ImageLoader loader = null;
	
	public static ImageLoader getInstance() {
		if (ImageLoader.loader == null)
			ImageLoader.loader = new ImageLoader();
		
		return ImageLoader.loader;
	}
	
	private ImageLoader() {
		this.reset();
	}
	
	private HashMap<Skin, HashMap<String, GameImage>> skinMap;
	private Skin skin = Skin.GREENLANDS;
	
	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	
	public GameImage loadGameImage(String dir, String name) {
		if (skinMap.get(this.skin) == null) {
			skinMap.put(this.skin, new HashMap<String, GameImage>());
		}
		
		HashMap<String, GameImage> imageMap = skinMap.get(this.skin);
		
		GameImage image;
		
		if (imageMap.get(name) == null) {
			image = new GameImage(String.format("group150/resources/images/%s/%s", this.skin.getDirectory(), dir), name);
			imageMap.put(name, image);
		}
		else {
			image = imageMap.get(name);
		}
		
		return image;
	}
	
	public void reset() {
		this.skinMap = new HashMap<Skin, HashMap<String, GameImage>>();
	}
}