package group150.model.enemy;

import group150.model.Enemy;
import group150.model.Path;

public class ArmoredEnemy extends Enemy {
	public ArmoredEnemy(Path p) {
		super("armoredenemy", p, 55, 120, 4, 30);
	}
}