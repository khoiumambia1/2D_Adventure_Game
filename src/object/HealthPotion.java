package object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;

public class HealthPotion extends SuperObject {
    GamePanel gp;
    
    public HealthPotion(GamePanel gp) {
        this.gp = gp;
        name = "HealthPotion";
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/health_potion.png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            } else {
                createFallbackImage();
            }
        } catch(IOException e) {
            createFallbackImage();
        }
        collision = false;
    }
    
    private void createFallbackImage() {
        image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.PINK);
        g2.fillRoundRect(gp.tileSize/4, gp.tileSize/4, gp.tileSize/2, gp.tileSize/2, 10, 10);
        g2.setColor(Color.RED);
        g2.fillRoundRect(gp.tileSize/3, gp.tileSize/6, gp.tileSize/3, gp.tileSize/4, 5, 5);
        g2.dispose();
        image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
    }
}