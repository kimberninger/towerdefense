package group150.model;

import java.awt.Color;
import java.awt.Point;

public class Target extends Sprite {
	public Target(Point p) {
		super("target", p);
	}
	
	public Color getDefaultColor() {
		return Color.DARK_GRAY;
	}
}