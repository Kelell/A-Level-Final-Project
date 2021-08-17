package gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import logic.Robot;
import logic.KeyboardController;
import logic.Object;

//the game panel on which  the true panels of the game are drawn
//it serves as an interlayer between the frame and the mosaic 
//of panels the player will see, it communicates with the statsPanel
//and the playPanel throwing them informations coming from the logic
//side of the game
public class GamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public GamePanel(){
		this.setRequestFocusEnabled(true);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);
		this.setBackground(Color.BLACK);

		this.add(statsPanel);
		statsPanel.setLocation(0, 0);

		this.add(playPanel);
		playPanel.setLocation(0, StatsPanel.STATS_HEIGHT);
				
		keyboardController=new KeyboardController();
		this.addKeyListener(keyboardController);
	}
	
	public void addRobot(Robot robot) {
		this.robot=robot;
		playPanel.addRobot(robot);
		statsPanel.addRobot(robot);
	}
	
	public void repaintGame(){
		playPanel.repaint();
		statsPanel.repaint();
	}
	
	public void clearObjects() {
		playPanel.clearObjects();
	}

	public void addObjects(ArrayList<Object> currentObjects) {
		playPanel.addObjects(currentObjects);
	}

	
	private KeyboardController keyboardController;
	private StatsPanel statsPanel=new StatsPanel();
	private PlayPanel playPanel=new PlayPanel();
	@SuppressWarnings("unused")
	private Robot robot;
}