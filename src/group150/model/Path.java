package group150.model;

import java.awt.Point;

import java.util.List;

public class Path {
	public Path(List<Point> points) {
		this.points = points;
	}
	
	private List<Point> points;
	
	public Point getSpawn() {
		return points.get(0);
	}
	
	public Point getNextPoint(Point p) {
		if (p.equals(this.getTarget()))
			return this.getTarget();
		if (this.points.contains(p))
			return points.get(this.points.lastIndexOf(p) + 1);
		else
			return getSpawn();
	}
	
	public Point getTarget() {
		return points.get(points.size() - 1);
	}
}