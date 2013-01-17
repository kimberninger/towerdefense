package group150.model.tower;

import java.awt.Point;

import group150.controller.GameManager;
import group150.model.BulletTower;
import group150.model.Tower;

public class CritTower extends BulletTower {
	public CritTower(int x, int y) {
		this(new Point(x, y));
	}

	public CritTower(Point p) {
		super("crittower", p, 250, 125, 150, 100, 2, "critbullet", 85, 5, 5);
	}

	public void upgrade() {
		if (level == maxLevel)
			return;
		this.upgradeCosts = this.upgradeCosts +10;

		this.level++;
		this.radius = this.radius + 5;
		this.bulletDamage = this.bulletDamage + 10;
		this.fireDelay = this.fireDelay - 7;
		this.bulletSpeed = this.bulletSpeed +1;
		switch (level) {
		case 0:
			super.imgName = "crittower";
			break;
		case 1:
			super.imgName = "crittower";
			break;
		case 2:
			super.imgName = "crittowerlevel1";
			break;
		case 3:
			super.imgName = "crittowerlevel1";
			break;
		case 4:
			super.imgName = "crittowerlevel2";
			break;
		case 5:
			super.imgName = "crittowerlevel2";
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
				description = "A une probabilit\u00E9#de 25 % d'un crit#(150 % de d\u00E9g\u00E2ts)#\u00E0 la terre.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "H4t 31n3 25 %1g3#Ch4nc3, 31n3n#kr1t1sch3n Tr3ff3r#(150 % Sch4d3n) zu#l4nd3n.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Tiene una probabilidad#del 25 % de un#cr\u00EEtico (150 % de da\u00F1os)#a la tierra.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Has a chance of 25 %#to deal 150 % damage.";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Hat eine 25 %ige Chance,#einen kritischen Treffer#(150 % Schaden) zu#landen.";
				break;
		}
		
		return String.format(infoFormat, "Crit Tower##",  this.getPrice(),this.bulletDamage, this.radius, this.fireDelay, this.upgradeCosts, description);
	}
}