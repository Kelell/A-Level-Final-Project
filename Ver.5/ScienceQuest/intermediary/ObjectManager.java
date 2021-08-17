package intermediary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import logic.Object;
import logic.Tile;

public class ObjectManager {
	public ObjectManager(int currentLevel){
		this.currentLevel=currentLevel;
		currentObjects=new ArrayList<Object>();
		loadInformations();
	}
	
	public void initializeStage(int currentLevel) {
		currentObjects.clear();
		this.currentLevel=currentLevel;
		loadInformations();
	}
	
	private void loadInformations() {
		InputStream is=this.getClass().getResourceAsStream("/object_info/level"+String.valueOf(currentLevel)+".txt");
		if(is==null){
			return;
		}
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		String line=null;
		String[] singleObjectInfo;
		try {
			while((line=reader.readLine())!=null){
				singleObjectInfo=line.split(" ");
				currentObjects.add(new Object(singleObjectInfo[0],Integer.valueOf(singleObjectInfo[1]),
						Integer.valueOf(singleObjectInfo[2]),Integer.valueOf(singleObjectInfo[3]),currentLevel,singleObjectInfo[4],Boolean.valueOf(singleObjectInfo[5])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Object> getObjects() {
		return currentObjects;
	}
	
	//returns the closest enemy within two tiles of distance (which is 
	//the maximum distance allowed for an interaction with a NPC)
	public Object closestNPC(int robotRow, int robotCol) {
		Object closestObject=null;
		int currentDistance;
		for(int i=0; i<currentObjects.size(); i++){
			if(robotRow!=currentObjects.get(i).getRow()){
				continue;
			}
			if((Math.abs(currentObjects.get(i).getCol()-robotCol))<=MAXIMUM_TALKING_DISTANCE){
				currentDistance=Math.abs(currentObjects.get(i).getCurrentX()-(robotCol*Tile.TILE_SIZE));
				if(closestObject==null){
					closestObject=currentObjects.get(i);
				} else {
					if(currentDistance<Math.abs(closestObject.getCurrentX()-robotCol+Tile.TILE_SIZE)){
						closestObject=currentObjects.get(i);
					}
				}
			}
		}
		
		return closestObject;
	}
	public boolean collidingObject(int robotRow, int robotCol) {
		Object closestObject=null;
		int currentDistance;
		//return false;
		for(int z=0; z<currentObjects.size(); z++){
			if(!currentObjects.get(z).interacted) {
				while((Math.abs(currentObjects.get(z).getCol()-robotCol))<=MAXIMUM_COLLISON_DISTANCE){
					currentDistance=Math.abs(currentObjects.get(z).getCurrentX()-(robotCol*Tile.TILE_SIZE));
					if(closestObject==null){
						closestObject=currentObjects.get(z);
						return true;
					}
				}
			}					
		}	
		return false;
	}
			
private static final int MAXIMUM_TALKING_DISTANCE=2;
private static final int MAXIMUM_COLLISON_DISTANCE=1;
private ArrayList<Object> currentObjects;
private int currentLevel;
}
