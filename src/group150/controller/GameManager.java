package group150.controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.util.Random;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import group150.exception.MapFormatException;

import group150.model.GameMap;

/**
 * Der Hauptcontroller des Spiels, der die Zust&auml;nde und die Benutzereingaben verwaltet.
 */
public class GameManager implements MouseListener, MouseMotionListener {
	enum State {
		TITLE_SCREEN,
		SELECT_MAP,
		SELECT_MAPFILE,
		PLAY_GAME,
		GAME_WON,
		GAME_OVER
	}
	
	public enum Language {
		GERMAN,
		ENGLISH,
		FRENCH,
		SPANISH,
		LEET
	}
	
	public static Language language = Language.GERMAN;
	
	public GameManager(Container container) {
		this.container = container;
	}

	private Container container;
	
	private State state = State.TITLE_SCREEN;
	
	private Game game = null;
	
	private Game titleGame = null;
	
	private JFileChooser fileChooser = null;
	
	private Rectangle randomMapButton;
	private Rectangle mapFromFileButton;
	private Rectangle[] staticMapButtons = new Rectangle[5];
	
	private String[] staticMaps = {
		"##############\n" +
		"#__S__v<<____#\n" +
		"#vSv_>X_^<v<<#\n" +
		"#v_v>^___^<_^#\n" +
		"#>>>^_______S#\n" +
		"#__S______S__#\n" +
		"#__v___>X_v__#\n" +
		"#__v___^__v__#\n" +
		"#__>>>X^<<<__#\n" +
		"##############\n",
		
		"##############\n" +
		"#S>>>>>>v___S#\n" +
		"#_______v<<<<#\n" +
		"#_______v____#\n" +
		"#_>>>>>>X____#\n" +
		"#>^___^______#\n" +
		"#^____^<<____#\n" +
		"#^______^____#\n" +
		"#S______^<<<S#\n" +
		"##############\n",
		
		"############\n" +
		"#_Sv______X#\n" +
		"#__v__v<<_^#\n" +
		"#__v__v_^_^#\n" +
		"#__v__v_^_^#\n" +
		"#__>>>X_S>^#\n" +
		"#_________S#\n" +
		"############\n",
		
		"############################\n" +
		"#S>>>>>>v___S____S__v<<____#\n" +
		"#_______v<<<<__vSv_>X_^<v<<#\n" +
		"#_______v______v_v>^___^<_^#\n" +
		"#_>>>>>>X______>>>^_______S#\n" +
		"#>^___^___v<<<<<<S______S__#\n" +
		"#^____^<<<<______v___>X_v__#\n" +
		"#^______^________v___^__v__#\n" +
		"#S______^<<<S____>>>X^<<<__#\n" +
		"#_________v______^<<<______#\n" +
		"#_________v_______>>^______#\n" +
		"#_________>>X_____^__v<___X#\n" +
		"#_S>>>>>>>^_______^<<<^S__^#\n" +
		"#___^_^___________^_______^#\n" +
		"#___^<S>>v________Sv______^#\n" +
		"#___^____v_________v__v<<_^#\n" +
		"#___^<<S_v_________v__v_^_^#\n" +
		"#v<<<<<<<<_________v__v_^>^#\n" +
		"#X_________________>>>X_S^S#\n" +
		"############################\n",
		
		"##############\n" +
		"#_________>>X#\n" +
		"#_S>>>>>>>^__#\n" +
		"#___^_^______#\n" +
		"#___^<S>>v___#\n" +
		"#___^____v___#\n" +
		"#___^<<S_v___#\n" +
		"#v<<<<<<<<___#\n" +
		"#X___________#\n" +
		"##############\n"
	};
	
	public void drawState(Graphics g) {
		int w = g.getClipBounds().width;
		int h = g.getClipBounds().height;
		
		int buttonX = (int)(w / 10);
		int buttonY = (int)(h / 10);
		int buttonWidth = w - 2 * buttonX;
		int buttonHeight = (int)(h / 2) - buttonY;
		randomMapButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
		
		buttonWidth = w - buttonX;
		buttonHeight -= buttonY;
		buttonX = (int)(w / 2);
		buttonY = 3 * buttonY + buttonHeight;
		buttonWidth -= buttonX;
		mapFromFileButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
		
		buttonX = (int)(w / 10);
		buttonWidth = w - buttonWidth - buttonX * 3;
		
		int buttonMargin = (int)((buttonWidth * .25) / staticMapButtons.length);
		buttonWidth = (int)((buttonWidth * .75) / (staticMapButtons.length));
		
		for (int i = 0; i < 5; i++) {
			staticMapButtons[i] = new Rectangle(buttonWidth * i + buttonMargin * i + buttonX, buttonY, buttonWidth, 20);
		}
	
		switch (state) {
			case TITLE_SCREEN:
				drawTitleScreen(g);
				break;
			case SELECT_MAP:
				drawSelectMap(g);
				break;
			case SELECT_MAPFILE:
				openFileChooser();
				break;
			case PLAY_GAME:
				playGame(g);
				break;
			case GAME_WON:
				drawGameWon(g);
				break;
			case GAME_OVER:
				drawGameOver(g);
				break;
		}
	}
	
	private void drawTitleScreen(Graphics g) {
		try {
			InputStream stream = Game.class.getResourceAsStream("/group150/resources/images/background.png");
			BufferedImage backgroundImage = ImageIO.read(stream);
			g.drawImage(backgroundImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
			
			stream = Game.class.getResourceAsStream("/group150/resources/images/banner.png");
			BufferedImage titleImage = ImageIO.read(stream);
			g.drawImage(titleImage, 200, 200, g.getClipBounds().width - 400, g.getClipBounds().height - 400, null);
			
			g.setColor(new Color(1.0f, 1.0f, 1.0f, .3f));
			g.fillRect(200, 200, g.getClipBounds().width - 400, g.getClipBounds().height - 400);
		}
		catch (Exception ex) { }
	}
	
	private void drawSelectMap(Graphics g) {
		try {
			InputStream stream = Game.class.getResourceAsStream("/group150/resources/images/green.jpg");
			BufferedImage backgroundImage = ImageIO.read(stream);
			g.drawImage(backgroundImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
		
			stream = Game.class.getResourceAsStream("/group150/resources/images/buttons/randommapbutton.png");
			BufferedImage randomMapButtonImage = ImageIO.read(stream);
			g.drawImage(randomMapButtonImage, randomMapButton.x, randomMapButton.y, randomMapButton.width, randomMapButton.height, null);
			
			stream = Game.class.getResourceAsStream("/group150/resources/images/buttons/loadmapbutton.png");
			BufferedImage mapFromFileButtonImage = ImageIO.read(stream);
			g.drawImage(mapFromFileButtonImage, mapFromFileButton.x, mapFromFileButton.y, mapFromFileButton.width, mapFromFileButton.height, null);
		}
		catch (Exception ex) { }
		
		for (int i = 0; i < staticMapButtons.length; i++) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.WHITE);

			g2d.setColor(new Color(0.0f, 0.0f, 1.0f, .3f));
			g2d.fill(staticMapButtons[i]);
			
			g2d.setColor(Color.WHITE);
			g2d.drawString(i + 1 + "", staticMapButtons[i].x + 5, staticMapButtons[i].y + 15);
		}
	}
	
	private void openFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Karten-Textdateien (.txt)", "txt");
			fileChooser.setFileFilter(filter);
			int chooserValue = fileChooser.showOpenDialog(this.container);
		
			if (chooserValue == JFileChooser.APPROVE_OPTION) {
				try {
					GameMap map = GameMap.parse(fileChooser.getSelectedFile());
					game = new Game(map);
					state = State.PLAY_GAME;
				}
				catch (IOException ex) {
					state = State.SELECT_MAP;
				}
				catch (MapFormatException ex) {
					state = State.SELECT_MAP;
				}
				finally {
					fileChooser = null;
				}
			}
			else {
				state = State.SELECT_MAP;
			}
		}
	}
	
	private void playGame(Graphics g) {
		if (game.gameIsOver()) {
			state = State.GAME_OVER;
			return;
		}
		
		if (game.gameIsWon()) {
			state = State.GAME_WON;
			return;
		}
		
		game.play(g);
	}
	
	private void drawGameOver(Graphics g) {
		try {
			InputStream stream = Game.class.getResourceAsStream("/group150/resources/images/lost.png");
			BufferedImage backgroundImage = ImageIO.read(stream);
			g.drawImage(backgroundImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
		}
		catch (Exception ex) { }
	}
	
	private void drawGameWon(Graphics g) {
		try {
			InputStream stream = Game.class.getResourceAsStream("/group150/resources/images/won.png");
			BufferedImage backgroundImage = ImageIO.read(stream);
			g.drawImage(backgroundImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
		}
		catch (Exception ex) { }
	}
	
	private void selectMap(Point p) {
		if (mapFromFileButton.contains(p)) {
			state = State.SELECT_MAPFILE;
			return;
		}
			
		if (randomMapButton.contains(p)) {
			try {
				GameMap map = GameMap.parse(staticMaps[new Random().nextInt(staticMaps.length)]);
				game = new Game(map);
				state = State.PLAY_GAME;
				return;
			}
			catch (MapFormatException ex) {
				return;				
			}
		}
		
		for (int i = 0; i < staticMapButtons.length; i++) {
			if (staticMapButtons[i].contains(p)) {
				try {
					GameMap map = GameMap.parse(staticMaps[i]);
					game = new Game(map);
					state = State.PLAY_GAME;
					return;
				}
				catch (MapFormatException ex) {
					return;				
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent ev) {
		if (ev.getButton() == MouseEvent.BUTTON1) {
			switch (state) {
				case PLAY_GAME:
					if (ev.getClickCount() == 2)
						game.handleDoubleClick(ev);
					else
						game.handleLeftClick(ev);
					break;
					
				case SELECT_MAP:
					selectMap(ev.getPoint());
					break;
				
				default:
					state = State.SELECT_MAP;
			}
		}
		
		if (ev.getButton() == MouseEvent.BUTTON3)
			if (state == State.PLAY_GAME)
				game.handleRightClick(ev);
	}
	
	public void mouseDragged(MouseEvent ev) {
		if (state == State.PLAY_GAME)
			game.handleMouseMoved(ev);
	}
	
	public void mouseMoved(MouseEvent ev) {
		if (state == State.PLAY_GAME)
			game.handleMouseMoved(ev);
	}
	
	public void mousePressed(MouseEvent ev) {
		if (state == State.PLAY_GAME)
			game.handleMousePressed(ev);
	}
	
	public void mouseReleased(MouseEvent ev) {
		if (state == State.PLAY_GAME)
			game.handleMouseReleased(ev);
	}
	
	public void mouseEntered(MouseEvent ev) { }
	
	public void mouseExited(MouseEvent ev) { }
}