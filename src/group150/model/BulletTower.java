package group150.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;

import java.awt.Shape;

import java.awt.geom.Ellipse2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BulletTower extends Tower {
	public BulletTower(String imgName, Point p, int price, int upgradeCosts, int radius, int fireDelay, int fireRate, String bulletImgName, int bulletDamage, int bulletSpeed, int maxLevel) {
		super(imgName, p, price, upgradeCosts, radius, fireDelay, maxLevel);
			
		this.fireRate = fireRate;
		this.bulletImgName = bulletImgName;
		this.bulletDamage = bulletDamage;
		this.bulletSpeed = bulletSpeed;
	}
	
	protected int fireRate;
	private String bulletImgName;
	protected int bulletDamage;
	protected int bulletSpeed;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public int fire(List<Enemy> enemies, Graphics g, int fieldWidth, int fieldHeight) {
		int initialScore = super.fire(enemies, g, fieldWidth, fieldHeight);
		
		int score = 0;
		
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
		
			b.move();
			
			for (int j = 0; j < enemies.size(); j++) {
				Enemy e = enemies.get(j);
				
				b.handleCollision(e, fieldWidth, fieldHeight);
				
				if (e.isDead()) {
					enemies.remove(e);
					score += e.getValue();
					j--;
				}
			}
			
			if (b.hasReachedTarget() || b.getEnemy().isDead()) {
				bullets.remove(b);
				i--;
			}
				
			b.draw(g, fieldWidth, fieldHeight);
		}
		
		if (bullets.size() >= this.fireRate || initialScore == -1) return score;
		
		ArrayList<Enemy> nearbyEnemies = new ArrayList<Enemy>();
		
		for (Enemy e : enemies)
			if (this.getFireArea(fieldWidth, fieldHeight).intersects(e.getRect(fieldWidth, fieldHeight)))
				nearbyEnemies.add(e);
		
		if (nearbyEnemies.size() == 0) return score;
				
		Random rnd = new Random();
		Enemy targetEnemy = nearbyEnemies.get(rnd.nextInt(nearbyEnemies.size()));
		
		bullets.add(new Bullet(bulletImgName, this.getCoordinates(), targetEnemy, bulletDamage, bulletSpeed));
		
		return score;
	}
	
	public ArrayList<Bullet> getBullets() {
		return this.bullets;
	}
	
	
	
}