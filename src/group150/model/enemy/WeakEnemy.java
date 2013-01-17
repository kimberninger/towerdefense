package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class WeakEnemy extends Enemy {
	public WeakEnemy(Path p) {
		super("weakenemy", p, 10, 40, 4, 0);
	}
}