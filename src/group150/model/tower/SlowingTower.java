package group150.model.tower;

import java.awt.Point;

import group150.controller.GameManager;
import group150.model.BulletTower;
import group150.model.Tower;

public class SlowingTower extends BulletTower {
	public SlowingTower(int x, int y) {
		this(new Point(x, y));
	}
	
	public SlowingTower(Point p) {
		super("slowingtower", p, 100, 50, 100, 120, 2, "slowingbullet", 50, 5, 5);
	}
	
	public void upgrade() {
		if (level == maxLevel) return;
		this.upgradeCosts = this.upgradeCosts +5;

		this.level++;
		this.bulletDamage = this.bulletDamage +5 ;
		this.fireDelay = this.fireDelay -5;
		this.bulletSpeed = this.bulletSpeed +1;
		this.radius = this.radius + 10;
		switch (level) {
			case 0:
				super.imgName = "slowingtower";				
				break;
			case 1:
				super.imgName = "slowingtower";
			
				break;
			case 2:
				super.imgName = "slowingtowerlevel1";
				
				break;
			case 3:
				super.imgName = "slowingtowerlevel1";
				break;
			case 4:
				super.imgName = "slowingtowerlevel2";
				break;
			case 5:
				super.imgName = "slowingtowerlevel2";
				this.upgradeCosts = 0;
				break;
		}

		
		super.loadImage();
	}
	
	public String getDescription() {
		String infoFormat;
		String description;
		
		switch (GameManager.language) {
			case FRENCH:
				infoFormat = Tower.towerInfoFrench;
				description = "Ralentit l'adversaire.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "V3rl4ngs4mt d3n G3gn3r.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Reduce la velocidad#del oponente.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Slows the enemy.";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Verlangsamt den Gegner.";
				break;
		}
		
		return String.format(infoFormat, "Slowing Tower##",  this.getPrice(),this.bulletDamage, this.radius, this.fireDelay, this.upgradeCosts, description);
	}
}


