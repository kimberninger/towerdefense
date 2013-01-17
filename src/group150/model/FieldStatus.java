package group150.model;

import java.awt.Color;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum, das die Stati der Felder einer <code>GameMap</code> beschreibt.
 * 
 * @author Kim Werner Berninger
 * @author Pascal Franke
 * @author Philipp Mario Gauer
 * @author Tobias Sebastian Engert
 * @version 1.0
 *
 * @see group150.model.GameMap
 */
public enum FieldStatus {
	EMPTY('_', new Color(34, 139, 34)),
	BLOCKED('#', new Color(139, 69, 19)),
	TOWER('t', new Color(34, 139, 34)),
	PATH_UP('^', Color.LIGHT_GRAY),
	PATH_RIGHT('>', Color.LIGHT_GRAY),
	PATH_DOWN('v', Color.LIGHT_GRAY),
	PATH_LEFT('<', Color.LIGHT_GRAY),
	SPAWN('S', Color.LIGHT_GRAY),
	TARGET('X', Color.LIGHT_GRAY);
	
	FieldStatus(char representation, Color defaultColor) {
		this.representation = representation;
		this.defaultColor = defaultColor;
	}
	
	private char representation;
	private Color defaultColor;
	
	public char getRepresentation() {
		return this.representation;
	}
	
	public Color getDefaultColor() {
		return this.defaultColor;
	}
	
	private static final Map<Character, FieldStatus> lookup = new HashMap<Character, FieldStatus>();
	
	static {
		for (FieldStatus s : EnumSet.allOf(FieldStatus.class))
			lookup.put(s.getRepresentation(), s);
	}
	
	public static FieldStatus get(char representation) { 
		return lookup.get(representation); 
	}
}