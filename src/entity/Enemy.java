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
import object.HealthPotion;

public class Enemy extends Entity {
    GamePanel gp;
    public int health = 3;
    public int damage = 1;
    public int type;
    public int attackCooldown = 0;
    Random random = new Random();
    
    public Enemy(GamePanel gp) {
        this.gp = gp;
        
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        
        setDefaultValues();
        getEnemyImage();
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 25;
        speed = 2;
        direction = "down";
        health = 3;
    }
    
    public void getEnemyImage() {
        up = new BufferedImage[4];
        down = new BufferedImage[4];
        left = new BufferedImage[4];
        right = new BufferedImage[4];
        
        for(int i = 0; i < 4; i++) {
            up[i] = setup("enemy_up" + (i+1));
            down[i] = setup("enemy_down" + (i+1));
            left[i] = setup("enemy_left" + (i+1));
            right[i] = setup("enemy_right" + (i+1));
        }
        
        if(up[0] == null) {
            createFallbackImages();
        }
    }
    
    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/enemy/" + imageName + ".png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        return image;
    }
    
    private void createFallbackImages() {
        for(int i = 0; i < 4; i++) {
            up[i] = createEnemyImage(Color.RED);
            down[i] = createEnemyImage(Color.RED);
            left[i] = createEnemyImage(Color.RED);
            right[i] = createEnemyImage(Color.RED);
        }
    }
    
    private BufferedImage createEnemyImage(Color color) {
        BufferedImage img = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillOval(gp.tileSize/4, gp.tileSize/4, gp.tileSize/2, gp.tileSize/2);
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(gp.tileSize/3, gp.tileSize/3, gp.tileSize/6, gp.tileSize/6);
        g2.dispose();
        return img;
    }
    
    public Rectangle getSolidArea() {
        return new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);
    }
    
    public boolean isTileSafe(int targetX, int targetY) {
        int centerX = targetX + gp.tileSize / 2;
        int centerY = targetY + gp.tileSize / 2;
        return !gp.tileM.isTileHazardous(centerX, centerY);
    }
    
    public void checkLavaDeath() {
        int centerX = worldX + gp.tileSize / 2;
        int centerY = worldY + gp.tileSize / 2;
        if(gp.tileM.isTileHazardous(centerX, centerY)) {
            gp.ui.showMessage("Enemy burned in lava!");
            die();
        }
    }
    
    public void update() {
        if(health <= 0) {
            die();
            return;
        }
        
        // Check if enemy is standing on lava
        checkLavaDeath();
        
        if(attackCooldown > 0) {
            attackCooldown--;
        }
        
        // Store attempted new position
        int newWorldX = worldX;
        int newWorldY = worldY;
        
        chasePlayer();
        
        switch(direction) {
            case "up": newWorldY -= speed; break;
            case "down": newWorldY += speed; break;
            case "left": newWorldX -= speed; break;
            case "right": newWorldX += speed; break;
        }
        
        // Check if the new position is safe (not on lava)
        boolean safeToMove = isTileSafe(newWorldX, newWorldY);
        
        if(safeToMove) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkEntity(this, gp.npc);
            
            if(!collisionOn) {
                worldX = newWorldX;
                worldY = newWorldY;
            }
        } else {
            // If target tile is lava, choose a different direction
            direction = getAvoidanceDirection();
        }
        
        spriteCounter++;
        if(spriteCounter > 12) {
            spriteIndex++;
            if(spriteIndex >= 4) spriteIndex = 0;
            spriteCounter = 0;
        }
    }
    
    public String getAvoidanceDirection() {
        // Try to find a safe direction to move
        String[] directions = {"up", "down", "left", "right"};
        for(String dir : directions) {
            int testX = worldX;
            int testY = worldY;
            switch(dir) {
                case "up": testY -= speed; break;
                case "down": testY += speed; break;
                case "left": testX -= speed; break;
                case "right": testX += speed; break;
            }
            if(isTileSafe(testX, testY)) {
                return dir;
            }
        }
        // If all directions unsafe, stay in place
        return direction;
    }
    
    // ============================================================
    // ENEMY DETECTION DISTANCE - LOCATED HERE:
    // ============================================================
    // The enemy "notices" the player when the player enters its detection range.
    // The detection is handled by the chasePlayer() method below.
    // 
    // The enemy will chase the player if the player is within a certain distance.
    // Currently, there is NO distance limit - the enemy will chase the player
    // from ANY distance because the chasePlayer() method always updates direction
    // based on player position.
    //
    // To add a detection radius, uncomment the if statement below:
    // ============================================================
    
    public void chasePlayer() {
        // Calculate distance to player
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // ============================================================
        // DETECTION RADIUS:
        // Change this value to set how far the enemy can see the player
        // Value is in pixels. Example: 200 pixels = about 4 tiles
        // ============================================================
        int DETECTION_RADIUS = 200;  // <-- CHANGE THIS VALUE
        
        // UNCOMMENT THE LINE BELOW TO ENABLE DETECTION RADIUS:
         if(distance < DETECTION_RADIUS) {
            if(Math.abs(dx) > Math.abs(dy)) {
                if(dx > 0) {
                    direction = "right";
                } else if(dx < 0) {
                    direction = "left";
                }
            } else {
                if(dy > 0) {
                    direction = "down";
                } else if(dy < 0) {
                    direction = "up";
                }
            }
         }
        // If you uncomment the above, enemies will only chase if within radius
        // Otherwise they will stand still
    }
    
    public void die() {
        gp.playSE(5);
        
        for(int i = 0; i < gp.enemies.length; i++) {
            if(gp.enemies[i] == this) {
                gp.enemies[i] = null;
                break;
            }
        }
        
        if(random.nextInt(100) < 20) {
            for(int j = 0; j < gp.obj.length; j++) {
                if(gp.obj[j] == null) {
                    HealthPotion potion = new HealthPotion(gp);
                    potion.worldX = worldX;
                    potion.worldY = worldY;
                    gp.obj[j] = potion;
                    break;
                }
            }
            gp.ui.showMessage("Enemy dropped a health potion!");
        }
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        gp.playSE(6);
        gp.ui.showMessage("Enemy hit! Health: " + health);
        
        if(health <= 0) {
            gp.ui.showMessage("Enemy defeated!");
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
            
            drawHealthBar(g2, screenX, screenY);
        }
    }
    
    public void drawHealthBar(Graphics2D g2, int x, int y) {
        int barWidth = gp.tileSize;
        int barHeight = 6;
        int barX = x;
        int barY = y - 10;
        
        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);
        
        g2.setColor(Color.RED);
        int healthWidth = (health * barWidth) / 3;
        g2.fillRect(barX, barY, healthWidth, barHeight);
    }
}