package group150.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.Rectangle;
import java.awt.Point;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.RadialGradientPaint;
import java.awt.Shape;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Tower extends Sprite {
	public static final String towerInfoGerman ="%sPreis: %d $#Schaden: %d #Radius: %d#Verz\u00F6gerung: %d#Upgradekosten: %d $##%s";
	public static final String towerInfoSpanish ="%sprecio: %d $#da\u00F1o: %d #radio: %d#retrasar: %d#Actualiza los costos de: %d $##%s";
	public static final String towerInfoFrench ="%sprix: %d $#nuire: %d #rayon: %d#retarder: %d#Mise \u00E0 jour des co\u00FBts: %d $##%s";
	public static final String towerInfoLeet ="%sPr31s: %d $#Sch4d3n: %d #R4d1us: %d#V3rz03g3rung: %d#Upgr4d3k0st3n: %d $##%s";
	public static final String towerInfoEnglish = "%sPrice: %d $#Damage: %d #Radius: %d#Delay: %d#Upgradecosts: %d $##%s";
	
	public Tower(String imgName, Point p, int price, int upgradeCosts, int radius, int fireDelay, int maxLevel) {
		super(imgName, p);
		
		this.price = price;
		this.upgradeCosts = upgradeCosts;
		this.radius = radius;
		this.fireDelay = fireDelay;
		
		this.maxLevel = maxLevel;
		
		this.delayCounter = this.fireDelay;
	}
	
	private int price;
	protected int upgradeCosts;
	
	protected int radius;
	protected int fireDelay;
	
	protected int level = 0;
	protected int maxLevel;
	
	private int delayCounter;
	
	public Color getDefaultColor() {
		return Color.BLUE;
	}
	
	public int fire(List<Enemy> enemies, Graphics g, int fieldWidth, int fieldHeight) {
		if (delayCounter < this.fireDelay) {
			delayCounter++;
			return -1;
		}
		else {
			delayCounter = 0;
			return 0;
		}
	}
	
	public abstract void upgrade();
	
	public void drawFireArea(Graphics g, int fieldWidth, int fieldHeight) {
		Graphics2D g2d = (Graphics2D)g;
		
		
		Rectangle towerRect = this.getRect(fieldWidth, fieldHeight);
		float centerX = towerRect.x + towerRect.width / 2;
		float centerY = towerRect.y + towerRect.height / 2;
		
		Point2D center = new Point2D.Float(centerX, centerY);
		float radius = this.getFireArea(fieldWidth, fieldHeight).getBounds().width / 2;
		Point2D focus = new Point2D.Float(centerX, centerY);
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {new Color(1.0f, 1.0f, 1.0f, 0.2f), new Color(0.0f, 0.0f, 1.0f, 0.2f)};
		RadialGradientPaint p = new RadialGradientPaint(center, radius, focus, dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
		
		g2d.setPaint(p);
		g2d.fill(this.getFireArea(fieldWidth, fieldHeight));
		
		g2d.setColor(new Color(1.0f, 1.0f, 1.0f, .3f));
		g2d.setStroke(new BasicStroke(3));
		g2d.draw(this.getFireArea(fieldWidth, fieldHeight));
		g2d.setStroke(new BasicStroke(1));
	}
	
	public void drawPriceTag(Graphics g, int fieldWidth, int fieldHeight) {
		Graphics2D g2d = (Graphics2D)g;
		
		Rectangle towerRect = this.getRect(fieldWidth, fieldHeight);
		
		int x = (int)(towerRect.x + towerRect.width / 2.5);
		int y = towerRect.y + fieldHeight;
		int width = (int)(towerRect.width / 1.5);
		int height = (int)(towerRect.height * .2);
		
		Rectangle priceTag = new Rectangle(x, y, width, height);
		
		g2d.setColor(Color.WHITE);
		g2d.fill(priceTag);
		
		Font f = new Font("Courier", Font.PLAIN, height);
		g2d.setFont(f);
		g2d.setColor(Color.BLACK);
		g2d.drawString(String.format("%d $", this.price), x, y + priceTag.height - 1);
	}
	
	public Shape getFireArea(int fieldWidth, int fieldHeight) {
		double widthRatio = (double)fieldWidth / 100;
		double heightRatio = (double)fieldHeight / 100;
		
		double x = Math.rint(widthRatio * (this.position.x - (radius - 50))) + 1;
		double y = Math.rint(heightRatio * (this.position.y - (radius - 50))) + 1;
		
		double width = Math.rint(radius * 2 * widthRatio) - 2;
		double height = Math.rint(radius * 2 * heightRatio) - 2;
		
		return new Ellipse2D.Double(x, y, width, height);
	}
	
	public void place(int x, int y) {
		this.position.move(x * 100, y * 100);
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getUpgradeCosts() {
		return this.upgradeCosts;
	}
	
	public abstract String getDescription();
}