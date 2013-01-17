package group150.model.tower;

import java.awt.Point;

import group150.controller.GameManager;
import group150.model.BulletTower;
import group150.model.Tower;

public class StandardTower extends BulletTower {
	public StandardTower(int x, int y) {
		this(new Point(x, y));
	}

	public StandardTower(Point p) {
		super("standardtower", p, 50, 25, 150, 50, 2, "standardbullet", 50, 5,
				5);
	}

	public void upgrade() {
		if (level == maxLevel)
			return;
		this.radius = this.radius + 10;
		this.bulletDamage = this.bulletDamage + 5;
		this.fireDelay = this.fireDelay - 5;
		this.level++;
		this.upgradeCosts = this.upgradeCosts +5;
		this.bulletSpeed = this.bulletSpeed +1;
		switch (level) {
		case 0:
			super.imgName = "standardtower";
			break;
		case 1:
			super.imgName = "standardtower";
			break;
		case 2:
			super.imgName = "standardtowerlevel1";
			break;
		case 3:
			super.imgName = "standardtowerlevel1";
			break;
		case 4:
			super.imgName = "standardtowerlevel2";
			break;
		case 5:
			super.imgName = "standardtowerlevel2";
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
				description = "Norme tour, ce#n'est pas quelque#chose de sp\u00E9cial.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "St4nd4rd Turm, d13s3r#k4nn n1chts b3s0nd3r3s.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Standard torre,#este no es nada#especial.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Standard tower,#nothing special.";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Standard Turm, dieser#kann nichts besonderes.";
				break;
		}
		
		return String.format(infoFormat, "Standard Tower##",  this.getPrice(),this.bulletDamage, this.radius, this.fireDelay, this.upgradeCosts, description);
	}
}