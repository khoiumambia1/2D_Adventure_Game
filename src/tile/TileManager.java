package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import khouim.GamePanel;
import khouim.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    public boolean[] hazardous = new boolean[10];
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map02.txt");
    }
    
    public void getTileImage() {
        setup(0, "greengrasstile", false, false);
        setup(1, "Whitebricktile", true, false);
        setup(2, "lava2", false, true);
        setup(3, "lavarocktile", true, false);
        setup(4, "sandtile", false, false);
        setup(5, "tree2", true, false);
        setup(6, "blackrocktile", true, false);
    }
    
    public void setup(int index, String imageName, boolean colision, boolean isHazardous) {
        UtilityTool uTool = new UtilityTool();
        
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".jpg"));
            if(tile[index].image != null) {
                tile[index].image = uTool.scaledImage(tile[index].image, gp.tileSize, gp.tileSize);
            } else {
                createFallbackTile(index, imageName);
            }
            tile[index].colision = colision;
            hazardous[index] = isHazardous;
        } catch(IOException e) {
            createFallbackTile(index, imageName);
        }
    }
    
    private void createFallbackTile(int index, String imageName) {
        tile[index] = new Tile();
        tile[index].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tile[index].image.createGraphics();
        
        if(imageName.contains("lava")) {
            g2.setColor(new Color(255, 69, 0));
            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
            g2.setColor(new Color(255, 140, 0));
            for(int i = 0; i < 3; i++) {
                g2.fillOval(i * 15, 10, 10, 10);
            }
        } else if(imageName.contains("grass")) {
            g2.setColor(new Color(34, 139, 34));
            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
        } else if(imageName.contains("sand")) {
            g2.setColor(new Color(238, 203, 173));
            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
        } else {
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
        }
        g2.dispose();
        tile[index].image = new UtilityTool().scaledImage(tile[index].image, gp.tileSize, gp.tileSize);
    }
    
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if(line == null) break;
                
                while(col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isTileHazardous(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if(col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return false;
        }
        
        int tileNum = mapTileNum[col][row];
        if(tileNum >= 0 && tileNum < hazardous.length) {
            return hazardous[tileNum];
        }
        return false;
    }
    
    public void draw(Graphics2D g2) {
        int WorldCol = 0;
        int WorldRow = 0;
        
        while(WorldCol < gp.maxWorldCol && WorldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[WorldCol][WorldRow];
            
            int worldX = WorldCol * gp.tileSize;
            int worldY = WorldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                if(tile[tileNum] != null && tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
            
            WorldCol++;
            
            if(WorldCol == gp.maxWorldCol) {
                WorldCol = 0;
                WorldRow++;
            }
        }
    }
}