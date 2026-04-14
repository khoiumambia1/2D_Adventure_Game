package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;

public class OBJChest extends SuperObject {
    GamePanel gp;
    
    public OBJChest(GamePanel gp) {
        this.gp = gp;
        name = "Chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/Chast.png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        collision = false;
    }
}