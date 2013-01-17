package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class RunnerEnemy extends Enemy {
	public RunnerEnemy(Path p) {
		super("runnerenemy", p, 40, 60, 9, 0);
	}
}