package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;

public class OBJDoor extends SuperObject {
    GamePanel gp;
    
    public OBJDoor(GamePanel gp) {
        this.gp = gp;
        name = "Door";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            }
        } catch(IOException e) {}
        collision = true;
    }
}