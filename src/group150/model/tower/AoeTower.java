package group150.model.tower;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.callback.LanguageCallback;

import group150.controller.GameManager;
import group150.model.Enemy;
import group150.model.Tower;

public class AoeTower extends Tower {
	public AoeTower(int x, int y) {
		this(new Point(x, y));
	}

	public AoeTower(Point p) {
		super("aoetower", p, 150, 75, 150, 50, 5);
	}

	public int fire(List<Enemy> enemies, Graphics g, int fieldWidth, int fieldHeight) {
		int score = super.fire(enemies, g, fieldWidth, fieldHeight);

		if (score == -1)
			return 0;
			
		score = 0;

		ArrayList<Enemy> nearbyEnemies = new ArrayList<Enemy>();

		for (Enemy e : enemies)
			if (this.getFireArea(fieldWidth, fieldHeight).intersects(e.getRect(fieldWidth, fieldHeight)))
				nearbyEnemies.add(e);

		if (nearbyEnemies.size() == 0)
			return 0;

		for (Enemy e : nearbyEnemies) {
			e.damage(30);
		}
		
		for (int i = 0; i < enemies.size(); i++) {	
			if (enemies.get(i).isDead()) {
				score += enemies.get(i).getValue();
				
				enemies.remove(i);
				i--;
			}
		}

		return score;
	}

	public void upgrade() {
		if (level == maxLevel)
			return;
		this.radius = this.radius + 5;
		this.upgradeCosts = this.upgradeCosts +5;
		this.fireDelay = this.fireDelay - 7;
		this.level++;
		
		switch (level) {
		case 0:
			super.imgName = "aoetower";
			break;
		case 1:
			super.imgName = "aoetower";
			break;
		case 2:
			super.imgName = "aoetowerlevel1";
			break;
		case 3:
			super.imgName = "aoetowerlevel1";
			break;
		case 4:
			super.imgName = "aoetowerlevel2";
			break;
		case 5:
			super.imgName = "aoetowerlevel2";
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
				description = "Attaque tous#les ennemis#simultan\u00E9ment dans#une zone cible.";
				break;
			case LEET:
				infoFormat = Tower.towerInfoLeet;
				description = "Gr31ft 4ll3 G3gn3r#gl31chz31t1g 1n#31n3m Z13lg3b13t 4n.";
				break;
			case SPANISH:
				infoFormat = Tower.towerInfoSpanish;
				description = "Ataca a todos los#enemigos de forma#simult\u00E1nea en un#\u00E1rea de orientación.";
				break;
			case ENGLISH:
				infoFormat = Tower.towerInfoEnglish;
				description = "Attacks all enemies#in its range#simultaneously";
				break;
			default:
				infoFormat = Tower.towerInfoGerman;
				description = "Greift alle Gegner#gleichzeitig in#einem Zielgebiet an.";
				break;
		}
		
		return String.format(infoFormat, "Aoe Tower##",  this.getPrice(), this.radius, 30, this.fireDelay, this.upgradeCosts, description);
	}
}