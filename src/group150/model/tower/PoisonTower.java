package group150.model.tower;

import java.awt.Point;

import group150.controller.GameManager;
import group150.model.BulletTower;
import group150.model.Tower;

public class PoisonTower extends BulletTower {
	public PoisonTower(int x, int y) {
		this(new Point(x, y));
	}
	
	public PoisonTower(Point p) {
		super("poisontower", p, 150, 75, 150, 50, 2, "poisonbullet", 60, 5, 5);
	}
	
	public void upgrade() {
		if (level == maxLevel) return;
		
		this.level++;
		this.upgradeCosts = this.upgradeCosts +5;

		this.radius = this.radius + 10;
		this.bulletDamage = this.bulletDamage +5 ;
		this.fireDelay = this.fireDelay -5;
		this.bulletSpeed = this.bulletSpeed +2;
		switch (level) {
			case 0:
				super.imgName = "poisontower";				
				break;
			case 1:
				super.imgName = "poisontower";
				break;
			case 2:
				super.imgName = "poisontowerlevel1";
				break;
			case 3:
				super.imgName = "poisontowerlevel1";
				break;
			case 4:
				super.imgName = "poisontowerlevel2";
				break;
			case 5:
				super.imgName = "poisontowerlevel2";
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
				description = "Donne un suppl\u00E9ment de#30 % des d\u00E9gâts de poison,#qui ignore l'armure.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "M4cht zusaetl1ch 30 %#G1ftsch4d3n, w3lch3r d13#Ruestung 1gn0r13rt.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Hace un 30 % adicional#da\u00F1o de veneno,#que ignora la armadura.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Deals an additional#poison damage of 30 %#which ignores the armor.";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Macht zus\u00E4tzlich 30 %#Giftschaden, welcher die#R\u00FCstung ignoriert.";
				break;
		}
		
		return String.format(infoFormat, "Poison Tower##",  this.getPrice(),this.bulletDamage, this.radius, this.fireDelay, this.upgradeCosts, description);
	}
}