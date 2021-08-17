package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.Robot;

public class StatsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public StatsPanel(){
		this.setSize(GameFrame.WIDTH, STATS_HEIGHT);
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		loadInformations();
	}
	
	private void loadInformations() {
		try {
			statsPanel=ImageIO.read(getClass().getResource("../images/statsBar.png"));
			YukonFont=Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("../fonts/Yukon Tech.ttf")).deriveFont(35.0f);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.setFont(YukonFont);
		g2.drawImage(statsPanel,0,0,GameFrame.WIDTH-5,STATS_HEIGHT,null);
		
		g2.drawString("x"+robot.getBatts(), BATT_COUNT_START_X, BATT_COUNT_START_Y);
			}
	

	public void addRobot(Robot robot) {
		this.robot=robot;
	}
	
	private Font YukonFont;
	private BufferedImage statsPanel;
	public static final int STATS_HEIGHT=40;
	private Robot robot;
	private static final int BATT_COUNT_START_X=785;
	private static final int BATT_COUNT_START_Y=30;
}
