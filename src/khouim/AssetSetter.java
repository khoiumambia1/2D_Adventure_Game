package khouim;

import object.OBJChest;
import object.OBJDoor;
import object.OBJKey;

public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	public void setObject() {
		gp.obj[0]= new OBJKey();
		gp.obj[0].worldX = 33*gp.tileSize;
		gp.obj[0].worldY = 38*gp.tileSize;
		
		gp.obj[1]= new OBJKey();
		gp.obj[1].worldX = 1*gp.tileSize;
		gp.obj[1].worldY = 43*gp.tileSize;
		
		gp.obj[2]= new OBJKey();
		gp.obj[2].worldX = 48*gp.tileSize;
		gp.obj[2].worldY = 21*gp.tileSize;
		
		gp.obj[3]= new OBJDoor();
		gp.obj[3].worldX = 1*gp.tileSize;
		gp.obj[3].worldY = 11*gp.tileSize;
		
		gp.obj[4]= new OBJDoor();
		gp.obj[4].worldX = 1*gp.tileSize;
		gp.obj[4].worldY = 47*gp.tileSize;
		
		gp.obj[5]= new OBJDoor();
		gp.obj[5].worldX = 11*gp.tileSize;
		gp.obj[5].worldY = 27*gp.tileSize;
		
		gp.obj[6]= new OBJChest();
		gp.obj[6].worldX = 1*gp.tileSize;
		gp.obj[6].worldY = 48*gp.tileSize;
		
	}
}
