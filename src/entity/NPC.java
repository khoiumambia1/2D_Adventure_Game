package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import khouim.GamePanel;
import khouim.UtilityTool;

public class NPC extends Entity {
    GamePanel gp;
    public String name;
    public String[] dialogues = new String[10];
    int dialogueIndex = 0;
    int actionLockCounter = 0;
    Random random = new Random();
    
    public NPC(GamePanel gp) {
        this.gp = gp;
        
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        
        setDefaultValues();
        getNPCImage();
        setDefaultDialogue(); // Set default dialogue
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 15;
        worldY = gp.tileSize * 15;
        speed = 1;
        direction = "down";
    }
    
    public void setDefaultDialogue() {
        dialogues[0] = "Hello traveler!";
        dialogues[1] = "Be careful of enemies!";
        dialogues[2] = "Collect keys to open doors.";
        dialogues[3] = "Find the chest to win!";
        dialogues[4] = "Press SPACE to attack!";
        dialogues[5] = "Health potions can heal you.";
    }
    
    public void setDialogue(int level) {
        switch(level) {
            case 1:
                dialogues[0] = "Welcome to Easy Mode!";
                dialogues[1] = "Enemies are weaker here.";
                dialogues[2] = "Collect keys to open doors.";
                dialogues[3] = "Find the chest to win!";
                dialogues[4] = "You have 5 hearts!";
                dialogues[5] = "Good luck on your journey!";
                break;
            case 2:
                dialogues[0] = "Medium Mode - Be careful!";
                dialogues[1] = "Enemies are stronger now.";
                dialogues[2] = "Use your sword wisely!";
                dialogues[3] = "Health potions are rare.";
                dialogues[4] = "You have 3 hearts.";
                dialogues[5] = "Stay alert!";
                break;
            case 3:
                dialogues[0] = "HARD MODE! Good luck!";
                dialogues[1] = "Enemies are deadly!";
                dialogues[2] = "Every hit matters!";
                dialogues[3] = "You'll need skill to survive!";
                dialogues[4] = "Only 2 hearts!";
                dialogues[5] = "May the odds be with you!";
                break;
            default:
                setDefaultDialogue();
                break;
        }
    }
    
    public void speak() {
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        
        if(gp.ui.currentDialogue != null) {
            gp.ui.dialogueOn = true;
        }
    }
    
    public void getNPCImage() {
        up = new BufferedImage[4];
        down = new BufferedImage[4];
        left = new BufferedImage[4];
        right = new BufferedImage[4];
        
        for(int i = 0; i < 4; i++) {
            up[i] = setup("npc_up" + (i+1));
            down[i] = setup("npc_down" + (i+1));
            left[i] = setup("npc_left" + (i+1));
            right[i] = setup("npc_right" + (i+1));
        }
        
        if(up[0] == null) {
            createFallbackImages();
        }
    }
    
    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/" + imageName + ".png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        return image;
    }
    
    private void createFallbackImages() {
        for(int i = 0; i < 4; i++) {
            up[i] = createNPCImage(Color.BLUE);
            down[i] = createNPCImage(Color.BLUE);
            left[i] = createNPCImage(Color.BLUE);
            right[i] = createNPCImage(Color.BLUE);
        }
    }
    
    private BufferedImage createNPCImage(Color color) {
        BufferedImage img = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRoundRect(gp.tileSize/4, gp.tileSize/4, gp.tileSize/2, gp.tileSize/2, 10, 10);
        g2.setColor(Color.WHITE);
        g2.fillOval(gp.tileSize/3, gp.tileSize/3, gp.tileSize/6, gp.tileSize/6);
        g2.dispose();
        return img;
    }
    
    public boolean isTileSafe(int targetX, int targetY) {
        int centerX = targetX + gp.tileSize / 2;
        int centerY = targetY + gp.tileSize / 2;
        return !gp.tileM.isTileHazardous(centerX, centerY);
    }
    
    public void update() {
        setAction();
        
        int newWorldX = worldX;
        int newWorldY = worldY;
        
        switch(direction) {
            case "up": newWorldY -= speed; break;
            case "down": newWorldY += speed; break;
            case "left": newWorldX -= speed; break;
            case "right": newWorldX += speed; break;
        }
        
        boolean safeToMove = isTileSafe(newWorldX, newWorldY);
        
        if(safeToMove) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkNPC(this, gp.npc);
            
            if(!collisionOn) {
                worldX = newWorldX;
                worldY = newWorldY;
            } else {
                direction = getRandomDirection();
            }
        } else {
            direction = getRandomDirection();
            actionLockCounter = 0;
        }
        
        spriteCounter++;
        if(spriteCounter > 12) {
            spriteIndex++;
            if(spriteIndex >= 4) spriteIndex = 0;
            spriteCounter = 0;
        }
    }
    
    public String getRandomDirection() {
        int i = random.nextInt(100) + 1;
        if(i <= 25) {
            return "up";
        } else if(i <= 50) {
            return "down";
        } else if(i <= 75) {
            return "left";
        } else {
            return "right";
        }
    }
    
    public void setAction() {
        actionLockCounter++;
        
        if(actionLockCounter == 120) {
            direction = getRandomDirection();
            actionLockCounter = 0;
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            switch(direction) {
                case "up": image = up[spriteIndex]; break;
                case "down": image = down[spriteIndex]; break;
                case "left": image = left[spriteIndex]; break;
                case "right": image = right[spriteIndex]; break;
            }
            
            if(image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(12f));
            String name = "Villager";
            int nameWidth = (int)g2.getFontMetrics().getStringBounds(name, g2).getWidth();
            g2.drawString(name, screenX + (gp.tileSize - nameWidth)/2, screenY - 5);
        }
    }
}