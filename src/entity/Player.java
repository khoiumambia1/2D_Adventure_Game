package entity;

import java.awt.Color;
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
    public int hasKey =0;
    
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

        if(i != 999) {

            String objectName = gp.obj[i].name;
            long now = System.currentTimeMillis();

            switch(objectName) {

            case "Key":
                hasKey++;

                if(hasKey == 1) {
                    gp.firstKeyPickupTime = now;
                    gp.recordEvent("FIRST_KEY", now - gp.gameStartTime);

                } else if(hasKey == 2) {
                    gp.secondKeyPickupTime = now;
                    gp.recordEvent("SECOND_KEY", now - gp.gameStartTime);

                } else if(hasKey == 3) {
                    gp.thirdKeyPickupTime = now;
                    gp.recordEvent("THIRD_KEY", now - gp.gameStartTime);
                }

                gp.obj[i] = null;
                gp.ui.showMessage("You got a key!");
                break;

            case "Door":

                if(hasKey > 0) {

                    if(hasKey == 1) {
                        gp.recordEvent("FIRST_KEY_TO_DOOR", now - gp.firstKeyPickupTime);

                    } else if(hasKey == 2) {
                        gp.recordEvent("SECOND_KEY_TO_DOOR", now - gp.secondKeyPickupTime);

                    } else if(hasKey == 3) {
                        gp.recordEvent("THIRD_KEY_TO_DOOR", now - gp.thirdKeyPickupTime);

                        // TOTAL GAME TIME
                        gp.recordEvent("TOTAL_GAME_TIME", now - gp.gameStartTime);
                    }

                    gp.obj[i] = null;
                    hasKey--;
                    gp.ui.showMessage("Door opened!");
                }
                else
                {
                	gp.ui.showMessage("You need a key!");
                }
                break;
            case "Chest":
            	gp.ui.gameFinished =true;
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
        g2.setColor(Color.red);
        g2.drawRect(screenX+solidArea.x,screenY+solidArea.y,solidArea.width,solidArea.height);
        
        
    }

}