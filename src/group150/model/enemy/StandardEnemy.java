package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class StandardEnemy extends Enemy {
	public StandardEnemy(Path p) {
		super("standardenemy", p, 30, 100, 5, 0);
	}
}