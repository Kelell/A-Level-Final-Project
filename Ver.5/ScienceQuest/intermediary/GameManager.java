package intermediary;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.ArrayList;

import logic.Robot;
import logic.KeyboardController;
import logic.Object;
import logic.World;
import gui.GamePanel;

//the GameManager is the main thread of the game, it calls repaints 
//for the play panel and statsPanel when necessary and manages keys 
//pressed, associating them to actions 
public class GameManager extends Thread {
	public GameManager(GamePanel gamePanel){
		this.world=new World();
		this.world.initializeStage(currentLevel);
		
		this.objectManager=new ObjectManager(currentLevel);
		
		//Initialise the protag of the game
		this.robot=new Robot();
		
		//stores the gamePanel and adds the robot and the npcs to it
		this.gamePanel=gamePanel;
		this.gamePanel.addRobot(robot);
		
		if(objectManager.getObjects().size()>0){
			gamePanel.addObjects(objectManager.getObjects());
		} else {
			gamePanel.clearObjects();
		}
		
		//while you're playing the game, the gameIsRunning is set to true
		this.gameIsRunning=true;
	}
	
	@Override
	public void run() {
		while(gameIsRunning){
			
			if(robot.outOfBounds()){
				currentLevel++;
				objectManager.initializeStage(currentLevel);
				world.initializeStage(currentLevel);
				robot.reinitialize();
			}
			
			robot.checkFallingState();
			
			//updates the character movement if it's 'jumping'
			robot.checkJumpState();
			
			//manage the keys currently pressed
			manageKeys();
			
			robot.checkCollectibles();
			
			robot.checkBlockCollisions();
			
			robot.checkRestoringCount();
				
			gamePanel.repaintGame();
			
			try {
				Thread.sleep(MAIN_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//the function manages the keys currently pressed associating concrete
	//actions to them
	private void manageKeys() {
		//get the currently pressed keys from the KeyboardController
		HashSet<Integer> currentKeys=KeyboardController.getActiveKeys();
		
		if(!listening){
			//manage the two possible run direction
			if(currentKeys.contains(KeyEvent.VK_RIGHT)){
			   // to simulate a boundingBox, while an object hasn't been interacted
			   // collidingObject will be set to true, removing the ability to move
				//forward until the object has been interacted with. 
				if (!objectManager.collidingObject(robot.getRow(),robot.getCol())) {
					//move right
					robot.move(KeyEvent.VK_RIGHT);
				}
				
			} else if (currentKeys.contains(KeyEvent.VK_LEFT)){
				//move left
				robot.move(KeyEvent.VK_LEFT);
			} else if(currentKeys.isEmpty() && !robot.getJumping() && !robot.getFalling()){
				//if the player is not pressing keys, the protag stands still
				robot.stop();
			}
		}
		
		if(currentKeys.contains(KeyEvent.VK_SPACE)) {
			if(!robot.getJumping() && !robot.getFalling()){
				robot.jump();
			}
		}
		
		if(currentKeys.contains(KeyEvent.VK_ENTER)){
			Object tempObject;
			//find the closest object according to the character's position
			if((tempObject=objectManager.closestNPC(robot.getRow(),robot.getCol()))!=null){
				
				//if the object is already talking, keep talking...
				if(tempObject.isTalking()){
					if(!(tempObject.continueTalking())){
						listening=false;
					}
				
				//otherwise interact with the object
				} else {
					tempObject.interact();
					
					//put the character in <idle> status when it's talking
					robot.stop();
					
					//prevent the character from moving when talking
					listening=true;
					
					//this object has been interacted with, get the name
					String name = tempObject.getName();
					ArrayList<Object> objects = objectManager.getObjects();
					for(int i=0; i<objects.size(); i++){
						Object objToInspect = objects.get(i);
						if(objToInspect.ref.equals(name)){
							objToInspect.interact();
						}
					}
				}
			}
			currentKeys.remove(KeyEvent.VK_ENTER);
		}	
	}

	public Robot getRobot(){
		return robot;
	}
	
	private boolean listening=false;
	
	//number of the current level the character finds themself in
	private int currentLevel=1;
	
	//variable set to 'true' if the game is running, 'false' otherwise
	private boolean gameIsRunning;
	
	//reference to the gamePanel
	private GamePanel gamePanel;
	
	//main sleep time of the GameManager thread - in this case
	//the gameManager does all it has to do and then waits for 18ms
	//before starting once again
	private static final int MAIN_SLEEP_TIME=16;
	
	//reference to the game main character
	private Robot robot;
	
	private World world;
	
	private ObjectManager objectManager;
}
