package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;

public class OBJBoot extends SuperObject {
    GamePanel gp;
    
    public OBJBoot(GamePanel gp) {
        this.gp = gp;
        name = "Boot";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        collision = false;
    }
}