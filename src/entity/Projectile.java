package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;
import khouim.UtilityTool;

public class Projectile extends Entity {
    GamePanel gp;
    public int damage = 1;
    public int range = 300;
    public int startX, startY;
    public boolean alive = true;
    
    public Projectile(GamePanel gp) {
        this.gp = gp;
        
        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 12;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 24;
        
        getProjectileImage();
    }
    
    public void getProjectileImage() {
        up = new BufferedImage[1];
        down = new BufferedImage[1];
        left = new BufferedImage[1];
        right = new BufferedImage[1];
        
        up[0] = setup("projectile_up");
        down[0] = setup("projectile_down");
        left[0] = setup("projectile_left");
        right[0] = setup("projectile_right");
    }
    
    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/projectile/" + imageName + ".png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        return image;
    }
    
    public void set(int worldX, int worldY, String direction, boolean user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        startX = worldX;
        startY = worldY;
        alive = true;
    }
    
    public void update() {
        if(!alive) return;
        
        int distance = (int) Math.hypot(worldX - startX, worldY - startY);
        if(distance > range) {
            alive = false;
            return;
        }
        
        switch(direction) {
            case "up": worldY -= 8; break;
            case "down": worldY += 8; break;
            case "left": worldX -= 8; break;
            case "right": worldX += 8; break;
        }
        
        for(int i = 0; i < gp.enemies.length; i++) {
            if(gp.enemies[i] != null) {
                if(getSolidArea().intersects(gp.enemies[i].getSolidArea())) {
                    gp.enemies[i].takeDamage(damage);
                    alive = false;
                    return;
                }
            }
        }
    }
    
    public Rectangle getSolidArea() {
        return new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);
    }
    
    public void draw(Graphics2D g2) {
        if(!alive) return;
        
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        switch(direction) {
            case "up": image = up[0]; break;
            case "down": image = down[0]; break;
            case "left": image = left[0]; break;
            case "right": image = right[0]; break;
        }
        
        if(image != null) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}