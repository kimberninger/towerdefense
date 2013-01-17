package group150.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.Random;

public abstract class Enemy extends Sprite {
	public Enemy(String imgName, Path path, int value, int health, int speed, int armor) {
		super(imgName, path.getSpawn());
		
		this.path = path;
		this.value = value;
		this.health = health;
		this.speed = speed;
		this.armor = armor;
		
		this.healthRemaining = this.health;
		
		currentPoint = path.getSpawn();
		nextPoint = path.getNextPoint(currentPoint);
	}
	
	private Path path;
	
	private int value;
	private int health;
	private int speed;
	private int armor;
	
	private int healthRemaining;
	
	private boolean targetReached = false;
	private boolean isDead = false;
	
	private Point currentPoint;
	private Point nextPoint;
	
	public Color getDefaultColor() {
		return Color.RED;
	}
	
	public void move() {
		if (this.targetReached) return;
	
		boolean nextPointReached =
			(this.position.x != nextPoint.x * 100 && Math.abs(nextPoint.x * 100 - this.position.x) < this.speed) ||
			(this.position.y != nextPoint.y * 100 && Math.abs(nextPoint.y * 100 - this.position.y) < this.speed) ||
			(Math.abs(nextPoint.x * 100 - this.position.x) < this.speed && Math.abs(nextPoint.y * 100 - this.position.y) < this.speed);
		
		if (nextPointReached) {
			this.position.move(nextPoint.x * 100, nextPoint.y * 100);
			
			currentPoint = nextPoint;
			nextPoint = this.path.getNextPoint(currentPoint);
		}
		
		if (this.position.x < nextPoint.x * 100) {
			this.position.translate(this.speed, 0);
			this.angle = 90;
		}
		else if (this.position.x > nextPoint.x * 100) {
			this.position.translate(-this.speed, 0);
			this.angle = 270;
		}
		else if (this.position.y < nextPoint.y * 100) {
			this.position.translate(0, this.speed);
			this.angle = 180;
		}
		else if (this.position.y > nextPoint.y * 100) {
			this.position.translate(0, -this.speed);
			this.angle = 0;
		}
		else {
			this.position.move(nextPoint.x * 100, nextPoint.y * 100);
		}
		
		if (this.getCoordinates().equals(path.getTarget()) || isDead()) {
			this.targetReached = true;
		}
	}
	
	public void damage(int damage) {
		this.healthRemaining = Math.max(this.healthRemaining - damage, 0);
		
		if (this.healthRemaining == 0) this.die();
	}
	
	public void damage(Bullet b) {
		if (b.getBulletName().equals("armortearingbullet") && this.armor > 0) {
				this.armor = this.armor -1;			
		}
		
		int poisonDamage = 0;	
		if (b.getBulletName().equals("poisonbullet")) {			
			poisonDamage = (int) (b.getDamage() * 0.3);			
		}
		
		if (b.getBulletName().equals("slowingbullet") && this.speed > 0) {
			this.speed = this.speed -1; 
		}	
		
		int critDamage = 0;
		if (b.getBulletName().equals("critbullet"))	{
			int critChance = new Random().nextInt(99);
			
			if(critChance <= 24)
				critDamage = (int)(b.getDamage() * 0.5);
		}
		
		
		int normalDamage = Math.max((b.getDamage() + critDamage - this.armor), 0);
		this.healthRemaining = Math.max(this.healthRemaining - normalDamage - poisonDamage, 0);
		
		if (this.healthRemaining == 0) this.die();
	}
	
	public void die() {
		this.isDead = true;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public boolean hasReachedTarget() {
		return this.targetReached;
	}
	
	public boolean isDead() {
		return this.isDead;
	}
	
	public void draw(Graphics g, int fieldWidth, int fieldHeight) {
		super.draw(g, fieldWidth, fieldHeight);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Rectangle enemyRect = this.getRect(fieldWidth, fieldHeight);
		
		int x = enemyRect.x;
		int y = (int)(enemyRect.y - fieldHeight / 50);
		int width = enemyRect.width;
		int height = (int)(enemyRect.height * .1);
		
		Rectangle lifeBar = new Rectangle(x, y, width, height);
		Rectangle lifeRemaining = new Rectangle(x, y, (int)(width * this.healthRemaining / this.health), height);
		
		g2d.setColor(Color.WHITE);
		g2d.fill(lifeBar);
		
		g2d.setColor(Color.GREEN);
		g2d.fill(lifeRemaining);
		
		g2d.setColor(Color.WHITE);
		g2d.draw(lifeBar);
		
	}
}