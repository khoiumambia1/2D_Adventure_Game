package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;
import khouim.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    
    public int dx = 0;
    public int dy = 0;
    
    public final int screenX;
    public final int screenY;
    int hasKey =0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
    	this.gp = gp;
    	this.keyH = keyH;
    	
    	screenX = gp.screenWidth/2 - (gp.tileSize/2);
    	screenY = gp.screenHeight/2 - (gp.tileSize/2);
    	
    	solidArea = new Rectangle();
    	solidArea.x = 12;
    	solidArea.y =20;
    	solidAreaDefaultX = solidArea.x;
    	solidAreaDefaultY = solidArea.y;
    	solidArea.width = 22;
    	solidArea.height = 22;
    	
    	setDefaultValues();
    	getPlayerImage();
    }
    public void setDefaultValues() {
    	worldX=gp.tileSize *2;
    	worldY=gp.tileSize *2;
    	speed=4;
    	direction = "down";
    	
    }
    public void getPlayerImage() {
        try {

            for(int i = 0; i < 4; i++) {
                up[i] = ImageIO.read(getClass().getResourceAsStream("/player/up" + (i+1) + ".png"));
                down[i] = ImageIO.read(getClass().getResourceAsStream("/player/down" + (i+1) + ".png"));
                left[i] = ImageIO.read(getClass().getResourceAsStream("/player/left" + (i+1) + ".png"));
                right[i] = ImageIO.read(getClass().getResourceAsStream("/player/right" + (i+1) + ".png"));
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        boolean moving = false;

        // Prioritize keys: up > down > left > right
        if(keyH.upPressed) {
            direction = "up";
            moving = true;
        }
        else if(keyH.downPressed) {
            direction = "down";
            moving = true;
        }
        else if(keyH.leftPressed) {
            direction = "left";
            moving = true;
        }
        else if(keyH.rightPressed) {
            direction = "right";
            moving = true;
        }
        

        // Only move if a key is pressed
        if(moving) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            int objIndex = gp.cChecker.checkObject(this, true);
            pickupObject(objIndex);
            
            if(!collisionOn) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // Animate sprite
            spriteCounter++;
            if(spriteCounter > 10) {
                spriteIndex++;
                if(spriteIndex >= 4) spriteIndex = 0;
                spriteCounter = 0;
            }
        } 
        else {
            // No movement, reset to idle frame
            spriteIndex = 0;
        }
    }

    public void pickupObject(int i) {
    	if(i!=999) {
    		
    		String objectName = gp.obj[i].name;
    		
    		switch(objectName) {
    		case"Key":
    			hasKey++;
    			gp.obj[i] = null;
    			System.out.println("Key: "+ hasKey);
    			break;
    		case"Door":
    			if(hasKey>0) {
    				gp.obj[i]= null;
    				hasKey--;
    			}
    			System.out.println("Key: "+ hasKey);
    			break;
    		}
    	}
    	
    }
 

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch(direction) {
            case "up":
                image = up[spriteIndex];
                break;
            case "down":
                image = down[spriteIndex];
                break;
            case "left":
                image = left[spriteIndex];
                break;
            case "right":
                image = right[spriteIndex];
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}