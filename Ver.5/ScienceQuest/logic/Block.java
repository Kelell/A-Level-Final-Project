package logic;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

//blocks are all those tiles that you can walk on and collide against
//they do not entail any kind of interaction
public class Block extends Tile {

	public Block(String name,int i, int j) {
		super(name,i,j);
		loadInformations();
	}

	@Override
	protected void initializeStuff() {
		currentX=col*TILE_SIZE;
		currentY=row*TILE_SIZE;
		boundingBox=new Rectangle(currentX,currentY,TILE_SIZE,TILE_SIZE);
	}
	
	protected void loadInformations() {
		try {
			image=ImageIO.read(getClass().getResource("../images/"+name+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
