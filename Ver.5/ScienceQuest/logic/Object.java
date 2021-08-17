package logic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

public class Object {
	public Object(String name, int row, int col,int numberOfSentences, int currentLevel, String ref, Boolean canTalk){
		this.name=name;
		this.row=row;
		this.col=col;
		this.numberOfSentences=numberOfSentences;
		this.currentX=col*Tile.TILE_SIZE;
		this.currentY=row*Tile.TILE_SIZE;
		this.currentLevel=currentLevel;
		sentences=new String[numberOfSentences+1];
		idleLeft=new BufferedImage[FRAMES_NUMBER];
		loadInformations();
		currentFrame=idleLeft[0];
		boundingBox=new Rectangle(currentX,currentY,width,height);
		this.ref = ref;
		this.canTalk = canTalk;
	}
	
	public void loadInformations(){
		try {
			idleLeft[0]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_off1.png"));
			idleLeft[1]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_off2.png"));
			idleLeft[2]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_off3.png"));
			
			InputStream is=this.getClass().getResourceAsStream("/object_info/"+name+"_script_"+currentLevel+".txt");
			
			if(is==null){
				return;
			}
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(is));
			String line=null;
			
			try {
				int i=0;
				while((line=reader.readLine())!=null){
					sentences[i]=line;
					i++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAfterInformations(){
		try {
			idleLeft[0]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_on1.png"));
			idleLeft[1]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_on2.png"));
			idleLeft[2]=ImageIO.read(getClass().getResource("../object_sprites/"+name+"_on3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getCurrentFrame() {
		if(interacted){
			return idleLeft[0];
		}
		
		if(frame_count>=2){
			add_value=-1;
		}
		if(frame_count<=0){
			add_value=1;
		}
		
		int currentFrame=frame_count;
			
		if(time_count%10==0){
			 frame_count+=add_value;	
		}
	
		time_count++;
		if(time_count>100){
			time_count=1;
		}
		
		return idleLeft[currentFrame];
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public int getCurrentX() {
		return currentX;
	}
	public int getCurrentY() {
		return currentY;
	}
	
	public String getName() {
		return name;
	}
	
	public void interact() {
		interacted=true;
		talking=true;
		loadAfterInformations();
	}

	public boolean isTalking() {
		return talking;
	}
	
	public boolean continueTalking() {
		currentSentence++;
		if(currentSentence>=numberOfSentences){
			currentSentence=numberOfSentences;
			talking=false;
			return false;
		}
		return true;
	}
	
	public String getSentence(){
		return sentences[currentSentence];
	}
	
	//if the object is not talking (talking='false') the game will not display 
	//any the speech balloon. otherwise it will display a specific speech 
	//depending on how many times the player has talked to this NPC
	private boolean talking=false;
	private String[] sentences;
	private int currentSentence=0;
	private int numberOfSentences;	
	private int add_value=1;
	private int time_count=1;
	private int frame_count=0;
	public boolean interacted=false;
	private static final int FRAMES_NUMBER=3;
	protected Rectangle boundingBox;
	private BufferedImage currentFrame;
	private String name;
	private BufferedImage[] idleLeft;
	private int row;
	private int col;
	private int width;
	private int height;
	private int currentX;
	private int currentY;
	private int currentLevel;
	public static final int OBJECT_SIZE=128;
	public String ref;
	public Boolean canTalk;
}