package group150;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import group150.controller.GameManager;

import group150.view.GamePanel;

public class TowerDefense {
	public static final String appName = "Group 150 - Tower Defense Game";

	public TowerDefense() {
		JFrame frame = new JFrame(appName);
		
		panel = new GamePanel(new GameManager(frame));
		
		frame.setContentPane(panel);
		frame.setBounds(50, 50, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private Timer loopTimer;
	private GamePanel panel;
	
	public void startGameLoop() {
		loopTimer = new Timer();
		
		loopTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				loop();
			}
		}, 0, 30);
	}
	
	public void stopGameLoop() {
		loopTimer.cancel();
		loopTimer = null;
	}
	
	public void loop() {
		panel.repaint();
	}

	public static void main(String[] args) {
		new TowerDefense().startGameLoop();
	}
}