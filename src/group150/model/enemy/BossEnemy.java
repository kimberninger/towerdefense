package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class BossEnemy extends Enemy {
	public BossEnemy(Path p) {
		super("bossenemy", p, 70, 500, 3, 60);
	}
}