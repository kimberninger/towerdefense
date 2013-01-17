package group150.model.tower;

import java.awt.Point;

import group150.controller.GameManager;
import group150.model.BulletTower;
import group150.model.Tower;

public class ArmorTearingTower extends BulletTower {
	public ArmorTearingTower(int x, int y) {
		this(new Point(x, y));
	}
	
	public ArmorTearingTower(Point p) {
		super("armortearingtower", p, 200, 100, 150, 50, 2, "armortearingbullet", 20, 5, 5);
	}
	
	public void upgrade() {
		if (level == maxLevel) return;
		this.upgradeCosts = this.upgradeCosts +5;

		this.level++;
		this.radius = this.radius + 15;
		this.bulletDamage = this.bulletDamage + 10;
		this.fireDelay = this.fireDelay - 5;
		this.bulletSpeed = this.bulletSpeed +1; 
		switch (level) {
			case 0:
				super.imgName = "armortearingtower";				
				break;
			case 1:
				super.imgName = "armortearingtower";
				break;
			case 2:
				super.imgName = "armortearingtowerlevel1";
				break;
			case 3:
				super.imgName = "armortearingtowerlevel1";
				break;
			case 4:
				super.imgName = "armortearingtowerlevel2";
				break;
			case 5:
				super.imgName = "armortearingtowerlevel2";
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
				description = "R\u00E9duit l'Armure#d'un monstre.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "V3rr1ng3rt d13 Ruestung#31n3s M0nst3rs.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Reduce la armadura#de un monstruo.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Decreases the armor of#an enemy.";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Verringert die R\u00FCstung#eines Monsters.";
				break;
		}
		
		return String.format(infoFormat, "Armor Tearing Tower##",  this.getPrice(),this.bulletDamage, this.radius, this.fireDelay, this.upgradeCosts, description);
	}
}