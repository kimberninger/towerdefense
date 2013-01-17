package group150.model;

import java.awt.Graphics;
import java.awt.Point;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import group150.controller.GameManager;

import group150.exception.FieldDoesNotExistException;
import group150.exception.MapFormatException;

import group150.helper.GameImage;
import group150.helper.ImageLoader;
import group150.helper.MapRandomizer;

import group150.model.tower.StandardTower;

/**
 * <p>Repr&auml;sentiert ein Spielfeld f&uuml;r Towerdefense.</p>
 * <p>Es gibt folgende Wege, ein Spielfeld zu erzeugen:</p>
 * <ol>
 * <li>Einlesen einer <code>.txt</code>-Datei mit dem unten beschriebenen Inhalt &uuml;ber die Methode <code>GameMap.parse(File)</code>.</li>
 * <li>Einlesen eines <code>String</code> mit dem unten beschriebenen Format &uuml;ber die Methode <code>GameMap.parse(String)</code>.</li>
 * <li>Erzeugen eines zuf&auml;lligen Spielfeldes &uuml;ber die Methode <code>GameMap.randomMap(int, int)</code>.</li>
 * </ol>
 * <p>Eine Karte muss aus mindestens einer Zeile folgender Zeichen bestehen:</p>
 * <ul>
 * <li><code>#</code> f&uuml;r ein blockiertes Feld</li>
 * <li><code>_</code> f&uuml;r ein leeres Feld</li>
 * <li><code>^</code> f&uuml;r einen Pfad nach oben</li>
 * <li><code>&gt;</code> f&uuml;r einen Pfad nach rechts</li>
 * <li><code>v</code> f&uuml;r einen Pfad nach unten</li>
 * <li><code>&lt;</code> f&uuml;r einen Pfad nach links</li>
 * <li><code>t</code> f&uuml;r einen bereits platzierten Standard-Turm</li>
 * <li><code>S</code> f&uuml;r einen Spawn-Punkt</li>
 * <li><code>X</code> f&uuml;r einen Target-Punkt</li>
 * </ul>
 * <p>Bei mehr als einer Zeile m&uuml;ssen alle Zeilen die gleiche L&auml;nge haben. Nicht ben&ouml;tigter Whitespace wird abgeschnitten.</p>
 * <p>Schl&auml;gt das Einlesen eines Spielfeldes mit <code>GameMap.parse</code> aufgrund eines falschen Kartenformates fehl, wird eine unbehandelte <code>MapFormatException</code> geworfen</p>
 * <p>Mit der <code>draw</code>-Methode l&auml;sst sich das Spielfeld abh&auml;ngig von einem Grafikkontext zeichnen.
 * 
 * @author Kim Werner Berninger
 * @author Pascal Franke
 * @author Philipp Mario Gauer
 * @author Tobias Sebastian Engert
 * @version 1.0
 *
 * @see group150.exception.MapFormatException
 * @see group150.model.FieldStatus
 */
public class GameMap {	
	/**
	 * Erzeugt das Spielfeld mit den angegebenen Feldern.
	 * Der Konstruktor ist nach au&szlig;en unsichtbar und kann nur &uuml;ber <code>parse</code> aufgerufen werden.
	 *
	 * @param fields das zweidimensionale Array von Feldern, das das Spielfeld repr&auml;sentiert.
	 * @see group150.model.GameMap#parse(String)
	 * @see group150.model.GameMap#parse(File)
	 */
	private GameMap(FieldStatus[][] fields) {
		this.fields = fields;
		this.fieldImages = new BufferedImage[fields.length][fields[0].length];
		
		updateImages();
	}
	
	/**
	 * Die zeilen- und spaltenweise gespeicherten Felder des Spielfeldes.
	 */
	private FieldStatus[][] fields;
	
	/**
	 * Die den Feldern zugeordneten Icons.
	 */
	private BufferedImage[][] fieldImages;
	
	/**
	 * Die beim Einlesen gefundenen Pfade.
	 */
	private ArrayList<Path> paths;
	
	/**
	 * Liefert die Gesamtanzahl an Zeilen auf dem Spielfeld.
	 *
	 * @return die Zeilenzahl.
	 */
	public int getNumberOfRows() {
		return fields.length;
	}
	
	/**
	 * Liefert die Gesamtanzahl an Spalten auf dem Spielfeld.
	 *
	 * @return die Spaltenzahl.
	 */
	public int getNumberOfCols() {
		return fields[0].length;
	}
	
	/**
	 * Liefert den <code>FieldStatus</code> des gegeben Feldes.
	 * Mit <code>FieldStatus.getRepresentation()</code> kann der <code>char</code>, der f&uuml;r den Status steht abgerufen werden.
	 *
	 * @param col die Spalte des Feldes, das abgefragt werden soll.
	 * @param row die Zeile des Feldes, das abgefragt werden soll.
	 * @return die Instanz von <code>FieldStatus</code>, die auf das Feld gelegt wurde.
	 * @throws group150.exception.FieldDoesNotExistException wenn das gesuchte Feld nicht existiert.
	 * @see group150.model.FieldStatus#getRepresentation()
	 */
	public FieldStatus getFieldStatusFor(int col, int row) throws FieldDoesNotExistException {
		if (row >= fields.length || col >= fields[row].length) throw new FieldDoesNotExistException();
		return fields[row][col];
	}
	
	/**
	 * Erzeugt eine Spielfeld-Instanz aufgrund der &uuml;bergebenen Datei.
	 *
	 * @param mapFile die Datei, die das Spielfeld enth&auml;lt.
	 * @return das aus der Datei geparste Spielfeld.
	 * @throws group150.exception.MapFormatException wenn die Datei nicht den Vorgaben entspricht.
	 */
	public static GameMap parse(File mapFile) throws IOException, MapFormatException {		
		FileReader fileReader = new FileReader(mapFile);
		BufferedReader reader = new BufferedReader(fileReader);
		
		StringBuilder lines = new StringBuilder();
		
		String line = null;		
		while ((line = reader.readLine()) != null) {
			lines.append(line.trim());
			lines.append("\n");
		}
		
		reader.close();
		
		return parse(lines.toString());
	}
	
	/**
	 * Erzeugt eine Spielfeld-Instanz aufgrund des &uuml;bergebenen <code>String</code>.
	 *
	 * @param mapString der <code>String</code>, der das Spielfeld beschreibt.
	 * @return das aus dem <code>String</code> geparste Spielfeld.
	 * @throws group150.exception.MapFormatException wenn der String nicht den Vorgaben entspricht.
	 */
	public static GameMap parse(String mapString) throws MapFormatException {
		ArrayList<String> lineList = new ArrayList<String>(Arrays.asList(mapString.trim().split("\n")));
		
		int nrRows = 0;
		int nrCols = lineList.get(0).length();
		
		for (String l : lineList) {
			if (!(l.length() == nrCols && l.matches("[_#t^>v<SX]+"))) throw new MapFormatException();
			
			nrRows++;
		}
		
		FieldStatus[][] fields = new FieldStatus[nrRows][nrCols];
		
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				Character c = lineList.get(i).charAt(j);
				FieldStatus s = FieldStatus.get(c);
				if (s != null)
					fields[i][j] = s;
				else
					throw new MapFormatException();
			}
		}
		
		GameMap map = new GameMap(fields);
		map.validatePaths();
		map.paths = GameMap.parsePaths(fields);
				
		return map;
	}
	
	/**
	 * Pr&uuml;ft, ob die Pfade valide sind. 
	 * Also es keine Sackgassen gibt und jeder Spawn und jedes Target mindestens an einem Pfad liegen.
	 * Au&szlig;erdem wird gepr&uuml;ft, ob es mindestens ein Spawn und ein Target gibt und diese niemals unmittelbar nebeneinander liegen.
	 *
	 * @throws group150.exception.MapFormatException wenn die obigen Vorgaben nicht erf&uuml;llt sind.
	 */
	private void validatePaths() throws MapFormatException {
		boolean spawnExists = false;
		boolean targetExists = false;
		
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				boolean fieldInvalid = false;
			
				FieldStatus top;
				try { top = fields[i-1][j]; }
				catch (ArrayIndexOutOfBoundsException ex) { top = null; }
				
				FieldStatus right;
				try { right = fields[i][j+1]; }
				catch (ArrayIndexOutOfBoundsException ex) { right = null; }
				
				FieldStatus bottom;
				try { bottom = fields[i+1][j]; }
				catch (ArrayIndexOutOfBoundsException ex) { bottom = null; }
				
				FieldStatus left;
				try { left = fields[i][j-1]; }
				catch (ArrayIndexOutOfBoundsException ex) { left = null; }
				
				switch (fields[i][j]) {
					case PATH_UP:
						fieldInvalid =
							top == null ||
							top == FieldStatus.EMPTY ||
							top == FieldStatus.TOWER ||
							top == FieldStatus.BLOCKED ||
							top == FieldStatus.PATH_DOWN;
						break;
						
					case PATH_RIGHT:
						fieldInvalid =
							right == null ||
							right == FieldStatus.EMPTY ||
							right == FieldStatus.TOWER ||
							right == FieldStatus.BLOCKED ||
							right == FieldStatus.PATH_LEFT;
						break;
						
					case PATH_DOWN:
						fieldInvalid =
							bottom == null ||
							bottom == FieldStatus.EMPTY ||
							bottom == FieldStatus.TOWER ||
							bottom == FieldStatus.BLOCKED ||
							bottom == FieldStatus.PATH_UP;
						break;
						
					case PATH_LEFT:
						fieldInvalid =
							left == null ||
							left == FieldStatus.EMPTY ||
							left == FieldStatus.TOWER ||
							left == FieldStatus.BLOCKED ||
							left == FieldStatus.PATH_RIGHT;
						break;
						
					case SPAWN:
						spawnExists = true;
						
						fieldInvalid =
							top == FieldStatus.PATH_DOWN ||
							right == FieldStatus.PATH_LEFT ||
							bottom == FieldStatus.PATH_UP ||
							left == FieldStatus.PATH_RIGHT ||
							(top != FieldStatus.PATH_UP && top != FieldStatus.PATH_RIGHT && top != FieldStatus.PATH_LEFT &&
							right != FieldStatus.PATH_UP && right != FieldStatus.PATH_RIGHT && right != FieldStatus.PATH_DOWN &&
							bottom != FieldStatus.PATH_RIGHT && bottom != FieldStatus.PATH_DOWN && bottom != FieldStatus.PATH_LEFT &&
							left != FieldStatus.PATH_UP && left != FieldStatus.PATH_DOWN && left != FieldStatus.PATH_LEFT);
						break;
						
					case TARGET:
						targetExists = true;
						
						fieldInvalid =
							top == FieldStatus.PATH_UP ||
							right == FieldStatus.PATH_RIGHT ||
							bottom == FieldStatus.PATH_DOWN ||
							left == FieldStatus.PATH_LEFT ||
							(top != FieldStatus.PATH_DOWN &&
							right != FieldStatus.PATH_LEFT &&
							bottom != FieldStatus.PATH_UP &&
							left != FieldStatus.PATH_RIGHT);
						break;
				}
				
				if (fieldInvalid) throw new MapFormatException();
			}
		}
		
		if (!(spawnExists && targetExists)) throw new MapFormatException();
	}
	
	/**
	 * Liest die Pfade aus einem Spielfeld aus und liefert sie in einer <code>ArrayList</code> zur&uuml;ck.
	 * Das &uuml;bergebene Spielfeld wird durch ein <code>FieldStatus[][]</code> repr&auml;sentiert und muss valide sein.
	 *
	 * @param fields Das Spielfeld, f&uuml;r das die Pfade ausgelesen werden sollen.
	 * @return Eine <code>ArrayList</code> mit den gefundenen Pfaden.
	 *
	 * @see group150.model.GameMap#validatePaths
	 */
	private static ArrayList<Path> parsePaths(FieldStatus[][] fields) throws MapFormatException {
		ArrayList<Point> spawnList = new ArrayList<Point>();
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				if (fields[i][j] == FieldStatus.SPAWN)
					spawnList.add(new Point(j, i));
		
		ArrayList<ArrayList<Point>> pathList = new ArrayList<ArrayList<Point>>();
		for (Point s : spawnList)
			for (int i = s.x-1; i <= s.x+1; i++)
				for (int j = s.y-1; j <= s.y+1; j++) {
					if (i != s.x && j != s.y)
						continue;
					
					try {
						boolean isPath =
							fields[j][i] == FieldStatus.PATH_UP ||
							fields[j][i] == FieldStatus.PATH_RIGHT ||
							fields[j][i] == FieldStatus.PATH_DOWN ||
							fields[j][i] == FieldStatus.PATH_LEFT;
						
						if (isPath) {
							ArrayList<Point> path = new ArrayList<Point>();
							path.add(s);
							path.add(new Point(i, j));
							
							pathList.add(path);		
						}
					}
					catch(ArrayIndexOutOfBoundsException ex) {
						continue;
					}
				}
		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		for (int i = 0; i < pathList.size(); i++) {
			ArrayList<Point> path = pathList.get(i);
			Point lastElement = path.get(1);
			while (fields[lastElement.y][lastElement.x] != FieldStatus.TARGET) {
				int x = lastElement.x;
				int y = lastElement.y;
		
				switch(fields[y][x]) {
					case PATH_UP:
						lastElement = new Point(x, y-1);
						break;
					case PATH_RIGHT:
						lastElement = new Point(x+1, y);
						break;
					case PATH_DOWN:
						lastElement = new Point(x, y+1);
						break;
					case PATH_LEFT:
						lastElement = new Point(x-1, y);
						break;
				}
				
				if (path.contains(lastElement)) throw new MapFormatException();
				
				path.add(lastElement);
			}
			
			paths.add(new Path(path));
		}
		
		return paths;
	}
	
	/**
	 * Erstellt eine zuf&auml;llige Karte.
	 *
	 * @param width die Breite des Feldes.
	 * @param height die H&ouml;he des Feldes.
	 */
	public static GameMap randomMap(int width, int height) {
		try {
			return GameMap.parse(MapRandomizer.randomMap(width, height, 2, 1, MapRandomizer.Difficulty.NORMAL));
		}
		catch (MapFormatException ex) {
			return GameMap.randomMap(width, height);
		}
	}
	
	/**
	 * L&auml;dt f&uuml;r jedes Feld das entsprechende Bild, abh&auml;ngig von dem gew&auml;hlten Skin.
	 */
	public void updateImages() {
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				String imgName = imageForField(i, j);
				
				GameImage image = ImageLoader.getInstance().loadGameImage("map", imgName);
				
				if (image.getNumberOfImages() > 0) {
					int imageIndex = new Random().nextInt(image.getNumberOfImages());
					fieldImages[i][j] = image.getImage(imageIndex);					
				}
				else {
					fieldImages[i][j] = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
	
					Graphics g = fieldImages[i][j].createGraphics();
					g.setColor(fields[i][j].getDefaultColor());
					g.fillRect(0, 0, 100, 100);
					g.dispose();
				}			
			}
		}
	}
	
	/**
	 * <p>Liefert den Namen der Bilddatei, die auf das entsprechende Feld gezeichnet werden soll.</p>
	 * <p>M&ouml;gliche Namen sind:</p>
	 * <ul>
	 * <li><code>path_top_bottom.png</code></li>
	 * <li><code>path_right_left.png</code></li>
	 * <li><code>path_top_right.png</code></li>
	 * <li><code>path_right_bottom.png</code></li>
	 * <li><code>path_bottom_left.png</code></li>
	 * <li><code>path_top_left.png</code></li>
	 * <li><code>path_right_bottom_left.png</code></li>
	 * <li><code>path_top_bottom_left.png</code></li>
	 * <li><code>path_top_right_left.png</code></li>
	 * <li><code>path_top_right_bottom.png</code></li>
	 * <li><code>path_top_right_bottom_left.png</code></li>
	 * <li><code>field.png</code></li>
	 * <li><code>block.png</code></li>
	 * </ul>
	 *
	 * @param row die Zeile des Feldes, dessen Bild abgefragt werden soll.
	 * @param col die Spalte des Feldes, dessen Bild abgefragt werden soll.
	 * @return der Name der Bilddatei, die auf das entsprechende Feld gezeichnet werden soll.
	 */
	private String imageForField(int row, int col) {
		StringBuilder result = new StringBuilder();
			
		FieldStatus top;
		try { top = fields[row-1][col]; }
		catch (ArrayIndexOutOfBoundsException ex) { top = null; }
				
		FieldStatus right;
		try { right = fields[row][col+1]; }
		catch (ArrayIndexOutOfBoundsException ex) { right = null; }
				
		FieldStatus bottom;
		try { bottom = fields[row+1][col]; }
		catch (ArrayIndexOutOfBoundsException ex) { bottom = null; }
				
		FieldStatus left;
		try { left = fields[row][col-1]; }
		catch (ArrayIndexOutOfBoundsException ex) { left = null; }
				
				
		switch(fields[row][col]) {
			case PATH_UP:
				result.append("path");
				result.append("_top");
				if (right != null && (right == FieldStatus.PATH_LEFT || right == FieldStatus.SPAWN)) result.append("_right");
				if (bottom != null && (bottom == FieldStatus.PATH_UP || bottom == FieldStatus.SPAWN)) result.append("_bottom");
				if (left != null && (left == FieldStatus.PATH_RIGHT || left == FieldStatus.SPAWN)) result.append("_left");
				break;
			case PATH_RIGHT:
				result.append("path");
				if (top != null && (top == FieldStatus.PATH_DOWN || top == FieldStatus.SPAWN)) result.append("_top");
				result.append("_right");
				if (bottom != null && (bottom == FieldStatus.PATH_UP || bottom == FieldStatus.SPAWN)) result.append("_bottom");
				if (left != null && (left == FieldStatus.PATH_RIGHT || left == FieldStatus.SPAWN)) result.append("_left");
				break;
			case PATH_DOWN:
				result.append("path");
				if (top != null && (top == FieldStatus.PATH_DOWN || top == FieldStatus.SPAWN)) result.append("_top");
				if (right != null && (right == FieldStatus.PATH_LEFT || right == FieldStatus.SPAWN)) result.append("_right");
				result.append("_bottom");
				if (left != null && (left == FieldStatus.PATH_RIGHT || left == FieldStatus.SPAWN)) result.append("_left");
				break;
			case PATH_LEFT:
				result.append("path");
				if (top != null && (top == FieldStatus.PATH_DOWN || top == FieldStatus.SPAWN)) result.append("_top");
				if (right != null && (right == FieldStatus.PATH_LEFT || right == FieldStatus.SPAWN)) result.append("_right");
				if (bottom != null && (bottom == FieldStatus.PATH_UP || bottom == FieldStatus.SPAWN)) result.append("_bottom");
				result.append("_left");
				break;
			case SPAWN:
				result.append("path");
				if (top != null && (top == FieldStatus.PATH_UP || top == FieldStatus.PATH_RIGHT || top == FieldStatus.PATH_LEFT))
					result.append("_top");
				if (right != null && (right == FieldStatus.PATH_UP || right == FieldStatus.PATH_RIGHT || right == FieldStatus.PATH_DOWN))
					result.append("_right");
				if (bottom != null && (bottom == FieldStatus.PATH_RIGHT || bottom == FieldStatus.PATH_DOWN || bottom == FieldStatus.PATH_LEFT))
					result.append("_bottom");
				if (left != null && (left == FieldStatus.PATH_UP || left == FieldStatus.PATH_DOWN || left == FieldStatus.PATH_LEFT))
					result.append("_left");
				break;
			case TARGET:
				result.append("path");
				if (top != null && top == FieldStatus.PATH_DOWN) result.append("_top");
				if (right != null && right == FieldStatus.PATH_LEFT) result.append("_right");
				if (bottom != null && bottom == FieldStatus.PATH_UP) result.append("_bottom");
				if (left != null && left == FieldStatus.PATH_RIGHT) result.append("_left");
				break;
			case BLOCKED:
				result.append("block");
				break;
			default:
				result.append("field");
		}
		
		return result.toString();
	}
	
	public ArrayList<Spawn> getSpawns() {
		ArrayList<Spawn> spawns = new ArrayList<Spawn>();
		
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				if (fields[i][j] == FieldStatus.SPAWN)
					spawns.add(new Spawn(new Point(j, i)));
		
		return spawns;
	}
	
	public ArrayList<Target> getTargets() {
		ArrayList<Target> targets = new ArrayList<Target>();
		
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				if (fields[i][j] == FieldStatus.TARGET)
					targets.add(new Target(new Point(j, i)));
		
		return targets;
	}
	
	public ArrayList<Path> getPaths() {
		return this.paths;
	}
	
	public ArrayList<Tower> getTowers() {
		ArrayList<Tower> towers = new ArrayList<Tower>();
		
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				if (fields[i][j] == FieldStatus.TOWER)
					towers.add(new StandardTower(new Point(j, i)));
		
		return towers;
	}
	
	/**
	 * Zeichnet die Karte auf den gegebenen Grafikkontext.
	 * Die Gr&ouml;%szlig;e der Karte wird an die des Kontexts angepasst.
	 * Es werden nur Pfade, Blockaden und leere Felder gezeichnet, da T&uuml;rme und Pfadbegrenzungen als Sprites gezeichnet werden.
	 *
	 * @param g der Grafikkontext, auf den gezeichnet werden soll.
	 */
	public void draw(Graphics g) {
		int clipWidth = g.getClipBounds().width;
		int clipHeight = g.getClipBounds().height;
		
		int fieldWidth = (int)(clipWidth / getNumberOfCols());
		int fieldHeight = (int)(clipHeight / getNumberOfRows());
		
		for (int i = 0; i < getNumberOfRows(); i++)
			for (int j = 0; j < getNumberOfCols(); j++)
				if (fieldImages[i][j] != null) {
					int widthCorrection = (j == getNumberOfCols() - 1 ? clipWidth - fieldWidth * getNumberOfCols() : 0);
					int heightCorrection = (i == getNumberOfRows() - 1 ? clipHeight - fieldHeight * getNumberOfRows() : 0);
					g.drawImage(fieldImages[i][j], j * fieldWidth, i * fieldHeight, fieldWidth + widthCorrection, fieldHeight + heightCorrection, null);
				}
	}
	
	/**
	 * Liefert einen <code>String</code>, der dem Inhalt der Kartendatei entspricht.
	 * Somit lassen sich Karten auch exportieren.
	 *
	 * @return die <code>String</code>-Repr&auml;sentation der <code>GameMap</code>.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getNumberOfRows(); i++) {
			for (int j = 0; j < getNumberOfCols(); j++) {
				sb.append(getFieldStatusFor(j, i).getRepresentation());
			}
			if (i < getNumberOfRows() - 1) sb.append("\n");
		}
		return sb.toString();
	}
}