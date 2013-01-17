package group150;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import group150.controller.GameManager;

import group150.view.GamePanel;

public class TDApplet extends JApplet implements ActionListener {
	public void init() {
		loopTimer = new Timer(30, this);
		panel = new GamePanel(new GameManager(this));
		this.setContentPane(panel);
	}
	
	private Timer loopTimer;
	private GamePanel panel;
	
	public void actionPerformed(ActionEvent ev) {
		panel.repaint();
	}
}