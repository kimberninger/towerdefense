package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class LegendaryEnemy extends Enemy {
	public LegendaryEnemy(Path p) {
		super("legendaryenemy", p, 100, 1000, 2, 90);
	}
}