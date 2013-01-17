package group150.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;

import java.util.Random;

import group150.helper.GameImage;
import group150.helper.ImageLoader;

public abstract class Sprite {	
	public Sprite(String imgName, Point p) {
		this.imgName = imgName;
		this.position = new Point(p.x * 100, p.y * 100);
		
		this.loadImage();
	}

	private BufferedImage img;
	protected String imgName;
	
	private int frameWidth;
	private int frameHeight;
	
	protected Point position;
	protected int angle = 0;

	private int cnt = 0;
	private int frameNr = 0;
	private int frameCnt;
	private int frameStep;
	
	public abstract Color getDefaultColor();
	
	public Point getCoordinates() {
		int x = this.position.x / 100;
		int y = this.position.y / 100;
		return new Point(x, y);
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public Rectangle getRect(int fieldWidth, int fieldHeight) {
		int w = fieldWidth;
		int h = fieldHeight;
		
		if (angle == 90 || angle == 270) {
			if (frameWidth < frameHeight) {
				h = (int)((double)fieldWidth / ((double)frameHeight / (double)frameWidth));
			}
			if (frameWidth > frameHeight) {
				w = (int)((double)fieldHeight / ((double)frameWidth / (double)frameHeight));
			}
		}
		else {
			if (frameWidth < frameHeight)
				w = (int)((double)fieldWidth / ((double)frameHeight / (double)frameWidth));
			if (frameWidth > frameHeight)
				h = (int)((double)fieldHeight / ((double)frameWidth / (double)frameHeight));
		}
		
		double widthRatio = (double)fieldWidth / 100;
		double heightRatio = (double)fieldHeight / 100;
		
		int x = (int)(widthRatio * this.position.x) + (int)((fieldWidth - w) / 2);
		int y = (int)(heightRatio * this.position.y) + (int)((fieldHeight - h) / 2);
		
		return new Rectangle(x, y, w, h);
	}
	
	public void draw(Graphics g, int fieldWidth, int fieldHeight) {
		frameNr = updateFrame();
		
		BufferedImage frameImg;
		
		if (img == null) {
			frameImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			Graphics gg = frameImg.createGraphics();
			gg.setColor(this.getDefaultColor());
			gg.fillOval(0, 0, 100, 100);
			gg.dispose();
		}
		else {
			int frameWidth = (int)(img.getWidth(null) / frameCnt);
			int frameHeight = img.getHeight(null);
			frameImg = img.getSubimage(frameWidth * frameNr, 0, frameWidth, frameHeight);
		}
		
		Rectangle spriteRect = this.getRect(fieldWidth, fieldHeight);
		
		BufferedImage scaledImage = new BufferedImage(spriteRect.width, spriteRect.height, BufferedImage.TYPE_INT_ARGB);
						
		Graphics2D g2d = scaledImage.createGraphics();

		g2d.translate((int)(spriteRect.width/2), (int)(spriteRect.height/2));
		g2d.rotate(Math.toRadians(angle));
		
		if (angle == 90 || angle == 270) {
			g2d.translate((int)(spriteRect.height/-2), (int)(spriteRect.width/-2));
			g2d.drawImage(frameImg, 0, 0, spriteRect.height, spriteRect.width, null);
		}
		else {
			g2d.translate((int)(spriteRect.width/-2), (int)(spriteRect.height/-2));
			g2d.drawImage(frameImg, 0, 0, spriteRect.width, spriteRect.height, null);
		}
		
		g2d.dispose();
		
		g.drawImage(scaledImage, spriteRect.x , spriteRect.y, null);
	}
	
	private int updateFrame() {
		if (cnt >= frameStep) {
			cnt = 0;
			frameNr++;
			if (frameNr > frameCnt - 1) {
				frameNr = 0;
			}
		}
		cnt++;
		
		return frameNr;
	}
	
	public void loadImage() {
		GameImage image = ImageLoader.getInstance().loadGameImage("sprites", this.imgName);
		
		if (image.getNumberOfImages() > 0) {
			int imageIndex = new Random().nextInt(image.getNumberOfImages());
			
			this.img = image.getImage(imageIndex);
			this.frameCnt = image.getFrameCnt(imageIndex);
			this.frameStep = image.getFrameStep(imageIndex);
		}
		else {
			this.img = null;
			this.frameCnt = 1;
			this.frameStep = 5;
		}
	}
}