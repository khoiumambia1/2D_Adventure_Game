package khouim;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean attackPressed;
    public boolean enterPressed;
    
    boolean checkDrawTime = false;
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Handle title screen input
        if(gp.gameState == gp.titleState) {
            if(code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.levelSelectState;
            }
            return;
        }
        
        // Handle level select screen input
        if(gp.gameState == gp.levelSelectState) {
            if(code == KeyEvent.VK_1) {
                gp.startGame(1); // Easy
            } else if(code == KeyEvent.VK_2) {
                gp.startGame(2); // Medium
            } else if(code == KeyEvent.VK_3) {
                gp.startGame(3); // Hard
            } else if(code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.titleState; // Back to title
            }
            return;
        }
        
        // Handle game over screen input
        if(gp.gameState == gp.gameOverState) {
            if(code == KeyEvent.VK_R) {
                gp.restartGame(); // Returns to title screen
            }
            return;
        }
        
        // Gameplay controls
        if(code == KeyEvent.VK_UP) upPressed = true;
        if(code == KeyEvent.VK_DOWN) downPressed = true;
        if(code == KeyEvent.VK_LEFT) leftPressed = true;
        if(code == KeyEvent.VK_RIGHT) rightPressed = true;
        
        if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_F) attackPressed = true;
        if(code == KeyEvent.VK_ENTER) enterPressed = true;
        
        if(code == KeyEvent.VK_P) {
            if(gp.gameState == gp.playState) gp.gameState = gp.pauseState;
            else if(gp.gameState == gp.pauseState) gp.gameState = gp.playState;
        }
        
        if(code == KeyEvent.VK_E) {
            for(int i = 0; i < gp.npc.length; i++) {
                if(gp.npc[i] != null) {
                    int distance = Math.abs(gp.player.worldX - gp.npc[i].worldX) + 
                                  Math.abs(gp.player.worldY - gp.npc[i].worldY);
                    if(distance < gp.tileSize * 2) {
                        gp.npc[i].speak();
                        gp.gameState = gp.dialogueState;
                        break;
                    }
                }
            }
        }
        
        if(code == KeyEvent.VK_TAB) {
            checkDrawTime = !checkDrawTime;
            System.out.println("Debug draw time: " + (checkDrawTime ? "ON" : "OFF"));
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_UP) upPressed = false;
        if(code == KeyEvent.VK_DOWN) downPressed = false;
        if(code == KeyEvent.VK_LEFT) leftPressed = false;
        if(code == KeyEvent.VK_RIGHT) rightPressed = false;
        if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_F) attackPressed = false;
        if(code == KeyEvent.VK_ENTER) enterPressed = false;
    }
}