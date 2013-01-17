package group150.model;

import java.awt.Color;
import java.awt.Point;

public class Spawn extends Sprite {
	public Spawn(Point p) {
		super("spawn", p);
	}
	
	public Color getDefaultColor() {
		return Color.DARK_GRAY;
	}
}