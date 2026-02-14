package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
	
    public int worldX,worldY;
    public int speed;
    
    public BufferedImage[] up = new BufferedImage[4];
    public BufferedImage[] down = new BufferedImage[4];
    public BufferedImage[] left = new BufferedImage[4];
    public BufferedImage[] right = new BufferedImage[4];

    public int spriteCounter = 0;
    public int spriteIndex = 0;
    public String direction;
    public Rectangle solidArea;
    public int solidAreaDefaultX,solidAreaDefaultY;
    public boolean collisionOn = false;
 
}