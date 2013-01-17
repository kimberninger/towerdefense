package group150.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;

import java.awt.event.MouseEvent;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import group150.helper.ImageLoader;

import group150.model.*;
import group150.model.enemy.*;
import group150.model.tower.*;

public class Game {
	public Game(GameMap map) {
		this.map = map;
		
		this.spawns = map.getSpawns();
		this.targets = map.getTargets();
		this.paths = map.getPaths();
		this.towers = map.getTowers();
		
		this.enemies = new ArrayList<Enemy>();
		
		this.optionsButtons = new Rectangle[2][];
		Rectangle[] skinOptions = new Rectangle[optionsNames[0].length];
		Rectangle[] languageOptions = new Rectangle[optionsNames[1].length];
		this.optionsButtons[0] = skinOptions;
		this.optionsButtons[1] = languageOptions;
		
		this.optionsImages = new BufferedImage[2][];
		BufferedImage[] skinImages = new BufferedImage[optionsNames[0].length];
		BufferedImage[] languageImages = new BufferedImage[optionsNames[1].length];
		this.optionsImages[0] = skinImages;
		this.optionsImages[1] = languageImages;
		
		loadOptionsImages();
		
		cultivateInventory();
	}
	
	protected GameMap map;
	
	protected ArrayList<Spawn> spawns;
	protected ArrayList<Target> targets;
	protected ArrayList<Path> paths;
	
	protected ArrayList<Tower> towers;
	protected ArrayList<Enemy> enemies;
	
	private ArrayList<Tower> towerInventory;
	private Tower selectedTower = null;
	private Tower hoveredTower = null;

	protected int lifes = 10;
	
	private String waveString =
		"sssss§s§s§s§s§s§s#" +
		"wwwwwwwwwwwwwwwwwwww#" +
		"assasasassawww#" +
		"§§b§§#" +
		"rrrrraarrrraarrrr#" +
		"rrbb§§#" +
		"l§§§#" +
		"rararararararara";
		
	private int waveNr = 1;
	private int waveDelay = 660;
	private int spawnDelay = 0;
	
	protected int money = 100;
	private int score = 0;
	
	private int panelWidth;
	private int panelHeight;
	
	private int divideX;
	
	private int towerWidth;
	private int towerHeight;
	
	private int mouseX;
	private int mouseY;
	
	private String[][] optionsNames = {
		{"defaultskin", "greenlands", "darksand", "lavaland", "cybot", "insane", "noskin"},
		{"german", "english", "french", "spanish", "leet"}
	};
	
	private Rectangle[][] optionsButtons;
	private BufferedImage[][] optionsImages;
	
	public void play(Graphics g) {
		if (gameIsWon() || gameIsOver()) return;
		
		panelWidth = g.getClipBounds().width;
		panelHeight = g.getClipBounds().height;
		
		divideX = (int)(panelWidth * .8);
		
		g.setClip(0, 0, divideX, panelHeight);
		drawGameClip(g);
		
		g.translate(divideX, 0);
		g.setClip(0, 0, panelWidth - divideX, panelHeight);
		
		towerWidth = (int)((panelWidth - divideX) / 3);
		towerHeight = (int)(panelHeight / 9);
		
		drawStatusClip(g, towerWidth, towerHeight);
		
		if (selectedTower != null) {
			int col = Math.min(map.getNumberOfCols() - 1, (int)((double)mouseX * (double)((double)map.getNumberOfCols() / (double)divideX)));
			int row = Math.min(map.getNumberOfRows() - 1, (int)((double)mouseY * (double)((double)map.getNumberOfRows() / (double)panelHeight)));
			selectedTower.place(col, row);
		}
	}
	
	private void drawGameClip(Graphics g) {
		int clipWidth = g.getClipBounds().width;
		int clipHeight = g. getClipBounds().height;
	
		int fieldWidth = (int)(clipWidth / map.getNumberOfCols());
		int fieldHeight = (int)(clipHeight / map.getNumberOfRows());
		
		map.draw(g);
		
		for (Tower t : towers)
			t.drawFireArea(g, fieldWidth, fieldHeight);
		
		for (Tower t : towers) {
			t.draw(g, fieldWidth, fieldHeight);
			int fireScore = t.fire(enemies, g, fieldWidth, fieldHeight);
			score += fireScore;
			money += fireScore;
		}
		
		if (selectedTower != null) {
			selectedTower.drawFireArea(g, fieldWidth, fieldHeight);
			selectedTower.draw(g, fieldWidth, fieldHeight);
		}
		
		if (waveDelay > 0) {
			waveDelay--;
		}
		else {
			startWave();
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			e.move();
		
			if (e.hasReachedTarget()) {
				e.die();
				enemies.remove(e);
				lifes--;
				i--;
			}
			
			e.draw(g, fieldWidth, fieldHeight);
		}
		
		for (Spawn s : spawns) s.draw(g, fieldWidth, fieldHeight);
		for (Target t : targets) t.draw(g, fieldWidth, fieldHeight);
	}
	
	private void drawStatusClip(Graphics g, int towerWidth, int towerHeight) {
		int clipWidth = g.getClipBounds().width;
		int clipHeight = g. getClipBounds().height;
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, clipWidth, clipHeight);
		
		for (Tower t : towerInventory)
			t.draw(g, towerWidth, towerHeight);
		
		for (Tower t : towerInventory)
			t.drawPriceTag(g, towerWidth, towerHeight);
			
		
		
		String[] statusLines = getStatusLines();
		String[] infoLines = getInfoLines();

		Font f = new Font(null, Font.PLAIN, 12);
		g.setFont(f);
		g.setColor(Color.BLACK);
		
		g.translate(0, (int)(panelHeight / 4));
		g.setClip(0, 0, g.getClipBounds().width, (int)(panelHeight / 6));
		
		drawLines(g, statusLines, 30);
		
		g.translate(0, (int)(panelHeight / 5));
		g.setClip(0, 0, g.getClipBounds().width, (int)(panelHeight / 6));
		
		drawOptionsButtons(g);
		
		g.translate(0, (int)(panelHeight / 5));
		g.setClip(0, 0, g.getClipBounds().width, (int)(panelHeight / 3));

		g.setColor(Color.WHITE);
		g.fillRect((int)((panelWidth - divideX) / 20), 0, g.getClipBounds().width - (int)((panelWidth - divideX) / 10), g.getClipBounds().height);
		
		drawLines(g, infoLines, 15);		
	}
	
	private void drawLines(Graphics g, String[] lines, int minLineDist) {
		int lineHeight = Math.min(minLineDist, (int)(g.getClipBounds().height / (lines.length + 1)));
		
		int textOffset = (int)(g.getClipBounds().width / 10);
		
		g.setColor(Color.BLACK);
		
		for (int i = 0; i < lines.length; i++) {
			int tabOffset = 0;
			while (lines[i].startsWith("§")) {
				lines[i] = lines[i].substring(1);
				tabOffset += 10;
			}
			g.drawString(lines[i], textOffset + tabOffset, (i + 1) * lineHeight + (int)(textOffset / 2));
		}	
	}
	
	private void drawOptionsButtons(Graphics g) {
		Rectangle rect = g.getClipBounds();
		
		int buttonYMargin = (int)((g.getClipBounds().height * .2) / (optionsButtons.length - 1));
		int buttonHeight = (int)((g.getClipBounds().height * .5) / optionsButtons.length);
		int buttonYOffset = (int)((g.getClipBounds().height - (buttonYMargin * (optionsButtons.length - 1) + buttonHeight * optionsButtons.length)) / 2);
		
		for (int i = 0; i < optionsButtons.length; i++) {
			int buttonXMargin = (int)((g.getClipBounds().width * .1) / (optionsButtons[i].length + 1));
			int buttonWidth = (int)((g.getClipBounds().width * .9) / optionsButtons[i].length);
			int buttonXOffset = (int)((g.getClipBounds().width - (buttonXMargin * (optionsButtons[i].length - 1) + buttonWidth * optionsButtons[i].length)) / 2);
			
			for (int j = 0; j < optionsButtons[i].length; j++) {
				optionsButtons[i][j] = new Rectangle(buttonXOffset + buttonXMargin * j + buttonWidth * j, buttonYOffset + buttonYMargin * i + buttonHeight * i, buttonWidth, buttonHeight);
				
				Rectangle r = optionsButtons[i][j];
				if (optionsImages[i][j] != null)
					g.drawImage(optionsImages[i][j], r.x, r.y, r.width, r.height, null);
				else {
					g.setColor(Color.GREEN);
					g.fillRect(r.x, r.y, r.width, r.height);
				}
			}
		}
	}
	
	private String[] getStatusLines() {String countDown;
		if (waveDelay == 0)
				countDown = "-";
		else
			countDown = String.format("%d s", waveDelay / 33);
		
		String statusFormat;
		
		switch(GameManager.language){
			case ENGLISH:
				statusFormat = "Wave: %d#next wave in: %s#money: %d $#score: %d#lifes: %d";
				break;
			case FRENCH:
				statusFormat = "vague: %d#prochaine vague de: %s#argent: %d $#score: %d#live: %d";
				break;
			case SPANISH:
				statusFormat = "ola : %d#siguiente paso en la: %s#precio: %d $#puntuaci\u00F3n: %d#vidas: %d";
				break;
			case LEET:
				statusFormat = "W3ll3: %d#N\u00E4chst3 W3ll3 1n: %s#G3ld: %d $#Punkt3: %d#L3b3n: %d";
				break;
			default:
				statusFormat = "Welle: %d#n\u00E4chste Welle in: %s#Geld: %d $#Punkte: %d#Leben: %d";
				break;
		}
		
		String statusString = String.format(statusFormat, this.waveNr, countDown, this.money, this.score, this.lifes);
		
		return statusString.split("#");
	}
	
	private String[] getInfoLines() {
		String info;
		
		if (hoveredTower == null) {
			switch(GameManager.language){
				case ENGLISH:
					info = "Mouseover:#§Get tower information##Double click:#§Upgrade tower##Right click:#§Sell tower###Drag a tower to#place it.";
					break;
				case FRENCH:
					info = "D\u00E9placez votre curseur:#§Obtenez de l'information#§tour##Double-cliquez#§Mise \u00E0 jour tour##Faites un clic droit:#§Vendre tour###Faites glisser un#tour pour le placer";
					break;
				case SPANISH:
					info = "Mouseover:#§Obtener informaci\u00F3n de#§la torre##Haga doble clic en:#§Actualiza la torre##Haga clic derecho:#§Venta torre###Arrastre una torre #para colocarlo.";
					break;
				case LEET:
					info = "M0us30v3r:#§Turm1nf0rm4t10n3n##D0pp3lkl1ck:#§Turm 4ufr\u00FCst3n##R3chtskl1ck:#§Turm v3rk4uf3n##Z13h3n s13 31n3n#Turm zum pl4tz13r3n.";
					break;
				default:
					info = "Mouseover:#§Turminformationen##Doppelklick:#§Turm aufr\u00FCsten##Rechtsklick:#§Turm verkaufen##Ziehen sie einen#Turm zum platzieren.";
					break;
			}
		}
		else {
			info = hoveredTower.getDescription();
		}
		
		return info.split("#");
	}
	
	public void startWave() {
		if (spawnDelay == 50) {
			spawnDelay = 0;
			spawnEnemy();
		}
		else {
			spawnDelay++;
		}
	}
	
	private void spawnEnemy() {
		if (waveString.isEmpty()) {
			return;
			
		}
		else {
			char enemyRepresentation = waveString.charAt(0);
		
			waveString = waveString.substring(1);
		
			int pathIndex = new Random().nextInt(paths.size());
			Path p = paths.get(pathIndex);
		
			switch (enemyRepresentation) {
				case 's':
					enemies.add(new StandardEnemy(p));
					break;
				case 'a':
					enemies.add(new ArmoredEnemy(p));
					break;
				case 'b':
					enemies.add(new BossEnemy(p));
					break;
				case 'l':
					enemies.add(new LegendaryEnemy(p));
					break;
				case 'r':
					enemies.add(new RunnerEnemy(p));
					break;
				case 'w':
					enemies.add(new WeakEnemy(p));
					break;
				case '§':
					break;
				default:
					waveDelay = 330;
					waveNr++;
			}
		}
	}
			
	
	public boolean gameIsWon() {
		return waveString.length() <= 0 && enemies.size() == 0;
	}
	
	public boolean gameIsOver() {
		return lifes <= 0;
	}
	
	public void updateSkin(Skin skin) {
		ImageLoader.getInstance().setSkin(skin);
		
		map.updateImages();
		
		for (Tower t : towers) {
			if (t instanceof BulletTower) {
				BulletTower bt = (BulletTower)t;
				for (Bullet b : bt.getBullets()) b.loadImage();
			}
	
			t.loadImage();
		}
		
		for (Enemy e : enemies) e.loadImage();
		for (Spawn s : spawns) s.loadImage();
		for (Target t : targets) t.loadImage();
		for (Tower t : towerInventory) t.loadImage();
	}
	
	private Tower getSelectedTower(Point p) {
		int divideX = (int)(panelWidth * .8);
		
		int col = (int)((double)p.x * (double)((double)map.getNumberOfCols() / (double)divideX));
		int row = (int)((double)p.y * (double)((double)map.getNumberOfRows() / (double)panelHeight));
	
		if (p.x < divideX) {
			for (Tower t1 : towers)
				if (col == t1.getCoordinates().x && row == t1.getCoordinates().y)
					return t1;
		}
		else {
			p.translate(-divideX, 0);
			for (Tower t2 : towerInventory)
				if (t2.getRect(towerWidth, towerHeight).contains(p)) {
					return t2;
				}
		}
		
		return null;
	}
	
	public void handleLeftClick(MouseEvent ev) {
		Point click = ev.getPoint();
		
		int originX = (int)(panelWidth * .8);
		int originY = (int)(panelHeight / 4) + (int)(panelHeight / 5);
		
		click.translate(-originX, -originY);
		
		for (int i = 0; i < optionsButtons.length; i++)
			for (int j = 0; j < optionsButtons[i].length; j++)
				if (optionsButtons[i][j].contains(click))
					handleOptionClicked(i, j);
	}
	
	private void handleOptionClicked(int x, int y) {
		if (x == 0) {
			switch (y) {
				case 0:
					updateSkin(Skin.DEFAULT_SKIN);
					return;
				case 1:
					updateSkin(Skin.GREENLANDS);
					return;
				case 2:
					updateSkin(Skin.DARKSANDS);
					return;
				case 3:
					updateSkin(Skin.LAVALAND);
					return;
				case 4:
					updateSkin(Skin.CYBOT);
					return;
				case 5:
					updateSkin(Skin.INSANE);
					return;
				case 6:
					updateSkin(Skin.NOSKIN);
					return;
			}
		}
		
		if (x == 1) {
			switch (y) {
				case 0:
					GameManager.language = GameManager.Language.GERMAN;
					return;
				case 1:
					GameManager.language = GameManager.Language.ENGLISH;
					return;
				case 2:
					GameManager.language = GameManager.Language.FRENCH;
					return;
				case 3:
					GameManager.language = GameManager.Language.SPANISH;
					return;
				case 4:
					GameManager.language = GameManager.Language.LEET;
					return;
			}
		}
	}
	
	public void handleRightClick(MouseEvent ev) {
		Tower t = getSelectedTower(ev.getPoint());
		
		if (t != null && towers.contains(t))
			sellTower(t);
	}
	
	public void handleDoubleClick(MouseEvent ev) {
		Tower t = getSelectedTower(ev.getPoint());
		
		if (t != null && towers.contains(t) && t.getUpgradeCosts() <= this.money) {
			this.money -= t.getUpgradeCosts();
			t.upgrade();
		}
	}
	
	public void handleMousePressed(MouseEvent ev) {
		Tower t = getSelectedTower(ev.getPoint());
		
		if (t != null && towerInventory.contains(t) && this.money >= t.getPrice()) {
			towerInventory.remove(selectedTower);
			cultivateInventory();
			
			selectedTower = t;
		}
	}
	
	public void handleMouseReleased(MouseEvent ev) {
		Point release = ev.getPoint();
		
		int divideX = (int)(panelWidth * .8);
		
		if (release.x < divideX) {
			if (selectedTower != null) {
				int col = selectedTower.getCoordinates().x;
				int row = selectedTower.getCoordinates().y;
				
				boolean towerPlaceable =
					map.getFieldStatusFor(col, row) == FieldStatus.EMPTY ||
					map.getFieldStatusFor(col, row) == FieldStatus.TOWER;
					
				for (Tower t : towers) {
					if (selectedTower.getCoordinates().equals(t.getCoordinates())) {
						towerPlaceable = false;
					}
				}
					
				if (towerPlaceable) {
					towers.add(selectedTower);
					
					this.money -= selectedTower.getPrice();
					selectedTower = null;
				}
				else {
					selectedTower = null;
				}
			}
		}
	}
	
	public void handleMouseMoved(MouseEvent ev) {
		mouseX = ev.getX();
		mouseY = ev.getY();
	
		hoveredTower = getSelectedTower(ev.getPoint());
	}
	
	public void sellTower(Tower t) {
		this.money += (int)(t.getPrice() * .5);
		towers.remove(t);
	}
	
	public void cultivateInventory() {
		this.towerInventory = new ArrayList<Tower>();
		this.towerInventory.add(new StandardTower(0, 0));
		this.towerInventory.add(new AoeTower(1, 0));
		this.towerInventory.add(new ArmorTearingTower(0, 1));
		this.towerInventory.add(new SlowingTower(1, 1));
		this.towerInventory.add(new PoisonTower(2, 0));
		this.towerInventory.add(new CritTower(2, 1));
	}
	
	private void loadOptionsImages() {
		for (int i = 0; i < optionsNames.length; i++)
			for (int j = 0; j < optionsNames[i].length; j++) {
				try {
					InputStream stream = Game.class.getResourceAsStream(String.format("/group150/resources/images/buttons/%s.png", optionsNames[i][j]));
					optionsImages[i][j] = ImageIO.read(stream);
				}
				catch (Exception ex) {
					optionsImages[i][j] = null;
				}
			}
	}
}