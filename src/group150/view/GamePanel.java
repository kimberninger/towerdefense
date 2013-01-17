package group150.view;

import java.awt.Graphics;

import javax.swing.JPanel;

import group150.controller.GameManager;

public class GamePanel extends JPanel {
	public GamePanel(GameManager manager) {
		this.manager = manager;
		
		this.setIgnoreRepaint(true);
		this.addMouseListener(this.manager);
		this.addMouseMotionListener(this.manager);
	}
	
	private GameManager manager;

	public void paintComponent(Graphics g) {
		this.manager.drawState(g);
	}
}