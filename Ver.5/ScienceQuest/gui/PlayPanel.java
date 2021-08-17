package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.Block;
import logic.Robot;
import logic.Collectible;
import logic.Object;
import logic.Tile;
import logic.World;

//PlayPanel - Is the panel where you see the actual game in motion,
//all the big part under the stats panel 
public class PlayPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public PlayPanel(){
		
		loadInformations();
		
		//set the size of the play panel
		this.setSize(GameFrame.WIDTH, PLAY_PANEL_HEIGHT);
		
		//set a random background colour to distinguish the play panel from the rest
		this.setBackground(Color.DARK_GRAY);
		
		//set no layouts
		this.setLayout(null);
		
		//double buffering should improve animations
		this.setDoubleBuffered(true);
	}
	
	private void loadInformations() {
		try {
			speechBalloon=ImageIO.read(getClass().getResource("../images/speechBalloon.png"));
			PlasmatiFont=Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("../fonts/Plasmati.ttf")).deriveFont(22.0f);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		collectibleAnimationCount++;
		if(collectibleAnimationCount%30==0){
			collectible_y_offset=-collectible_y_offset;
		} 
		
		if(collectibleAnimationCount>60){
			collectibleAnimationCount=0;
		}
		
		
		Graphics2D g2=(Graphics2D)g;
		g2.setFont(PlasmatiFont);
		
		//use antialiasing to draw smoother images and lines
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.drawImage(World.CURRENT_BACKGROUND,0,-Tile.TILE_SIZE,GameFrame.WIDTH,PLAY_PANEL_HEIGHT, null);
		
		for(int i=0; i<World.ROWS; i++){
			for(int j=0; j<World.COLS; j++){
				if(World.tiledMap[i][j] instanceof Block){
					g2.drawImage(World.tiledMap[i][j].getImage(), World.tiledMap[i][j].getCurrentX(), 
							World.tiledMap[i][j].getCurrentY(), null);
				} else if(World.tiledMap[i][j] instanceof Collectible){
					g2.drawImage(World.tiledMap[i][j].getImage(), World.tiledMap[i][j].getCurrentX(), 
								World.tiledMap[i][j].getCurrentY()+collectible_y_offset, null);
					}
				}
			}
		
		//draw the protag of the game
		if(!robot.getRestoring()){
			g2.drawImage(robot.getCurrentFrame(),robot.getCurrentX(),robot.getCurrentY(),null);
		}
		
		if(currentObjects.size()>0){
			Object currentObject;
			for(int i=0; i<currentObjects.size(); i++){
				currentObject=currentObjects.get(i);
				// the object sprite is drawn one row above there real position simply because the image of an object
				//is twice as tall as the protag's
				g2.drawImage(currentObject.getCurrentFrame(),currentObject.getCurrentX(),currentObject.getCurrentY()-Tile.TILE_SIZE,null);
				if(currentObject.isTalking()&&currentObject.canTalk){
						
					if(++balloonCount%30==0){
						if(dynamicBalloonOffset==2){
							dynamicBalloonOffset=0;
						} else {
							dynamicBalloonOffset=2;
						}
					}
					
					g2.drawImage(speechBalloon,currentObject.getCurrentX()-speechBalloon.getWidth()/2+
							Tile.TILE_SIZE/2-SPEECH_BALLOON_X_OFFSET,currentObject.getCurrentY()+Tile.TILE_SIZE+1
							+dynamicBalloonOffset,null);
					tempSentence=currentObject.getSentence();
					
					if(tempSentence.contains("NEWLINE")){
						g2.drawString(tempSentence.split(" NEWLINE ")[0], currentObject.getCurrentX()-speechBalloon.getWidth()/3-
								SPEECH_BALLOON_X_OFFSET*5,
								currentObject.getCurrentY()+speechBalloon.getHeight()-20+dynamicBalloonOffset);
						g2.drawString(tempSentence.split(" NEWLINE ")[1], currentObject.getCurrentX()-speechBalloon.getWidth()/3,
								currentObject.getCurrentY()+speechBalloon.getHeight()+dynamicBalloonOffset);
					} else {
						g2.drawString(tempSentence, currentObject.getCurrentX()-speechBalloon.getWidth()/3-SPEECH_BALLOON_X_OFFSET*5,
								currentObject.getCurrentY()+speechBalloon.getHeight()-20+dynamicBalloonOffset);
					}
					
				}
			}
		}
	}
	
	//function called by the GameManager to add the robot (protag) to the play panel at runtime
	//the PlayPanel needs a reference to the robot since it's drawn many times
	public void addRobot(Robot robot) {
		this.robot=robot;
	}
	

	public void addObjects(ArrayList<Object> currentObjects) {
		this.currentObjects=currentObjects;
	}
	
	public void clearObjects() {
		currentObjects.clear();
	}
	
	//height of the terrain in pixels - this is basically the distance of the robot's feet 
	//from the bottom border of the window you play the game in
	public static final int TERRAIN_HEIGHT=192;
	
	//height of the PlayPanel 
	public static final int PLAY_PANEL_HEIGHT=640;
	
	//reference to the protag of the game
	private Robot robot;
	
	private ArrayList<Object> currentObjects;
	
	private BufferedImage speechBalloon;
	
	private static final int SPEECH_BALLOON_X_OFFSET=5;
	
	private Font PlasmatiFont;
	
	private int dynamicBalloonOffset=0;
	private int balloonCount=0;
	
	private String tempSentence;
	
	private int collectibleAnimationCount=0;
	private int collectible_y_offset=2;
}
