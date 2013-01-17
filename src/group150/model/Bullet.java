package group150.model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class Bullet extends Sprite {
	public Bullet(String imgName, Point p, Enemy enemy, int damage, int speed) {
		super(imgName, p);
		
		this.enemy = enemy;
		this.damage = damage;
		this.speed = speed;
		this.imgName = imgName;
		
		
	}
	
	private Enemy enemy;
	private int damage;
	private int speed;
	
	private String imgName;
	
	protected boolean targetReached = false;
	
	public Color getDefaultColor() {
		return Color.YELLOW;
	}
	
	public Rectangle getRect(int fieldWidth, int fieldHeight) {
		Rectangle rect = super.getRect(fieldWidth, fieldHeight);
		
		int x = rect.x + (int)(rect.width / 6);
		int y = rect.y + (int)(rect.height / 6);
		int width = (int)(rect.width / 3);
		int height = (int)(rect.height / 3);
		
		return new Rectangle(x, y, width, height);
	}

	public void move() {
		if (targetReached) return;
		
		if (this.position.x < enemy.getPosition().x) {
			this.position.translate(this.speed, 0);
		}
		if (this.position.x > enemy.getPosition().x) {
			this.position.translate(-this.speed, 0);
		}
		if (this.position.y < enemy.getPosition().y) {
			this.position.translate(0, this.speed);
		}
		if (this.position.y > enemy.getPosition().y) {
			this.position.translate(0, -this.speed);
		}
	}
	
	public void handleCollision(Enemy e, int fieldWidth, int fieldHeight) {
		if (targetReached) return;
	
		Rectangle enemyRect = e.getRect(fieldWidth, fieldHeight);
		Rectangle bulletRect = this.getRect(fieldWidth, fieldHeight);
		
		if (bulletRect.intersects(enemyRect)) {
			this.targetReached = true;
			
			e.damage(this);
		}
	}
	
	public Enemy getEnemy() {
		return this.enemy;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public String getBulletName(){
		return this.imgName;
	}
	
	public boolean hasReachedTarget() {
		return this.targetReached;
	}
}