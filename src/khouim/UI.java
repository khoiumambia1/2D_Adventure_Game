package khouim;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import object.OBJKey;

public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, arial_20;
    BufferedImage keyImage;
    BufferedImage gameLogo;  // Add game logo image
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public double finalTime = 0;
    
    public boolean dialogueOn = false;
    public String currentDialogue = "";
    int dialogueCounter = 0;
    
    // Title screen animation
    private int blinkCounter = 0;
    private float titleYOffset = 0;
    private boolean titleYOffsetIncreasing = true;
    private float logoRotation = 0;  // For rotation animation
    private float logoSpeed = 0.02f;  // Rotation speed
    
    public UI(GamePanel gp) {
        this.gp = gp;
        
        arial_40 = new Font("Arial", Font.PLAIN, 35);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        OBJKey key = new OBJKey(gp);
        keyImage = key.image;
        
        // Load game logo (create a simple one if not available)
        loadGameLogo();
    }
    
    private void loadGameLogo() {
        try {
            // Try to load a custom logo image if you have one
            gameLogo = ImageIO.read(getClass().getResourceAsStream("/tiles/logo.png"));
            if(gameLogo != null) {
                // Scale the logo to a reasonable size
                java.awt.Image scaled = gameLogo.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
                gameLogo = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = gameLogo.createGraphics();
                g2d.drawImage(scaled, 0, 0, null);
                g2d.dispose();
            }
        } catch(IOException e) {
            // If no logo image, create a beautiful drawn logo
            createDrawnLogo();
        }
        
        if(gameLogo == null) {
            createDrawnLogo();
        }
    }
    
    private void createDrawnLogo() {
        gameLogo = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = gameLogo.createGraphics();
        
        // Draw a golden shield with sword and key emblem
        // Shield background
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.fillRoundRect(25, 25, 100, 110, 20, 20);
        
        // Shield inner border
        g2d.setColor(new Color(255, 165, 0)); // Orange
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRoundRect(28, 28, 94, 104, 18, 18);
        
        // Shield top curve
        g2d.setColor(new Color(255, 200, 50));
        g2d.fillArc(25, 15, 100, 50, 0, 180);
        
        // Sword (vertical)
        g2d.setColor(new Color(192, 192, 192)); // Silver
        g2d.fillRect(70, 45, 10, 60);
        g2d.setColor(new Color(255, 255, 255));
        g2d.fillRect(72, 40, 6, 10);
        g2d.setColor(new Color(139, 69, 19)); // Brown handle
        g2d.fillRect(70, 95, 10, 15);
        
        // Key emblem on shield
        g2d.setColor(new Color(255, 255, 0));
        g2d.fillOval(68, 65, 14, 14);
        g2d.setColor(new Color(255, 200, 0));
        g2d.fillRect(75, 72, 20, 4);
        g2d.fillRect(85, 68, 4, 12);
        
        // Stars decoration
        g2d.setColor(new Color(255, 255, 255, 200));
        for(int i = 0; i < 3; i++) {
            int starX = 40 + (i * 35);
            g2d.fillOval(starX, 110, 4, 4);
        }
        
        g2d.dispose();
    }
    
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        // Title screen
        if(gp.gameState == gp.titleState) {
            drawTitleScreen(g2);
            return;
        }
        
        // Level select screen
        if(gp.gameState == gp.levelSelectState) {
            drawLevelSelectScreen(g2);
            return;
        }
        
        // Draw gameplay UI for other states
        if(gp.gameState == gp.playState) {
            drawPlayState();
            drawHazardWarning();
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        
        if(gameFinished == true) {
            drawGameFinished();
        }
    }
    
    public void drawTitleScreen(Graphics2D g2) {
        // Update animations
        if(titleYOffsetIncreasing) {
            titleYOffset += 0.5f;
            if(titleYOffset > 15) titleYOffsetIncreasing = false;
        } else {
            titleYOffset -= 0.5f;
            if(titleYOffset < -15) titleYOffsetIncreasing = true;
        }
        
        // Update logo rotation
        logoRotation += logoSpeed;
        if(logoRotation > Math.PI * 2) {
            logoRotation -= Math.PI * 2;
        }
        
        blinkCounter++;
        if(blinkCounter > 60) blinkCounter = 0;
        
        // Background with gradient effect
        Color bgColor1 = new Color(20, 20, 40);
        Color bgColor2 = new Color(40, 20, 60);
        for(int i = 0; i < gp.screenHeight; i++) {
            float ratio = (float)i / gp.screenHeight;
            int r = (int)(bgColor1.getRed() * (1 - ratio) + bgColor2.getRed() * ratio);
            int g = (int)(bgColor1.getGreen() * (1 - ratio) + bgColor2.getGreen() * ratio);
            int b = (int)(bgColor1.getBlue() * (1 - ratio) + bgColor2.getBlue() * ratio);
            g2.setColor(new Color(r, g, b));
            g2.drawLine(0, i, gp.screenWidth, i);
        }
        
        // Draw decorative stars/particles
        g2.setColor(new Color(255, 255, 255, 100));
        for(int i = 0; i < 150; i++) {
            int x = (i * 131) % gp.screenWidth;
            int y = (i * 253) % gp.screenHeight;
            int size = 1 + (i % 3);
            g2.fillOval(x, y, size, size);
        }
        
        // Draw floating game logo with rotation effect
        int logoX = gp.screenWidth/2 - 75;
        int logoY = gp.screenHeight/4 + (int)titleYOffset;
        
        // Add glow effect behind logo
        g2.setColor(new Color(255, 215, 0, 50));
        g2.fillOval(logoX - 10, logoY - 10, 170, 170);
        
        // Draw the logo with full rotation
        if(gameLogo != null) {
            // Save original transform
            java.awt.geom.AffineTransform old = g2.getTransform();
            
            // Rotate around center
            float centerX = logoX + 75;
            float centerY = logoY + 75;
            g2.rotate(logoRotation, centerX, centerY);
            g2.drawImage(gameLogo, logoX, logoY, null);
            
            // Restore transform
            g2.setTransform(old);
        }
        
        // Game Title with shadow effect and floating animation
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        String title = "FAmBani";
        
        // Shadow
        g2.setColor(new Color(0, 0, 0, 150));
        int x = getXforCenteredText(title) + 5;
        int y = gp.screenHeight/2 + (int)titleYOffset + 5;
        g2.drawString(title, x, y);
        
        // Main title gradient effect
        g2.setColor(new Color(255, 215, 0));
        x = getXforCenteredText(title);
        y = gp.screenHeight/2 + (int)titleYOffset;
        g2.drawString(title, x, y);
        
        // Title outline
        g2.setColor(new Color(255, 165, 0));
        g2.drawString(title, x - 2, y - 2);
        g2.drawString(title, x + 2, y + 2);
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, x, y);
         
        // Draw Start Button
        int buttonWidth = 220;
        int buttonHeight = 65;
        int buttonX = gp.screenWidth/2 - buttonWidth/2;
        int buttonY = gp.screenHeight - 220;
        
        // Button glow effect
        if(blinkCounter < 30) {
            g2.setColor(new Color(255, 215, 0, 80));
            g2.fillRoundRect(buttonX - 8, buttonY - 8, buttonWidth + 16, buttonHeight + 16, 25, 25);
        }
        
        // Button background
        g2.setColor(new Color(70, 70, 100));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(new Color(255, 215, 0));
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        
        // Button inner highlight
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillRoundRect(buttonX + 2, buttonY + 2, buttonWidth - 4, 20, 10, 10);
        
        // Button text - Changed from "PLAY" to "START"
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        String playText = "START";
        g2.setColor(Color.WHITE);
        x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(playText)/2;
        y = buttonY + 48;
        g2.drawString(playText, x, y);
        
        // Instructions with blinking effect - Changed text
        if(blinkCounter < 30) {
            g2.setFont(new Font("Arial", Font.PLAIN, 22));
            g2.setColor(new Color(255, 215, 0));
            String instruction = "Press ENTER to Select Level";
            x = getXforCenteredText(instruction);
            y = buttonY + 100;
            g2.drawString(instruction, x, y);
        }
        
        // Draw controls info at bottom
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(150, 150, 150));
        String controls = "Controls: Arrow Keys to Move | SPACE/F to Attack | P to Pause";
        x = getXforCenteredText(controls);
        y = gp.screenHeight - 50;
        g2.drawString(controls, x, y);
        
        String objective = "Objective: Collect Keys to Open Doors and Find the Chest!";
        x = getXforCenteredText(objective);
        y = gp.screenHeight - 25;
        g2.drawString(objective, x, y);
    }
    
    // Rest of your existing methods (drawPlayState, drawPlayerHealth, etc.) remain the same
    public void drawPlayState() {
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        if(keyImage != null) {
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        }
        g2.drawString("x " + gp.player.hasKey, 74, 67);
        
        drawPlayerHealth();
        drawTimer();
        
        if(messageOn == true) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.setColor(Color.white);
            g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
            
            messageCounter++;
            if(messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }
    
    public void drawLevelSelectScreen(Graphics2D g2) {
        // Background
        g2.setColor(new Color(20, 20, 40));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.setColor(new Color(255, 215, 0));
        String title = "SELECT LEVEL";
        int x = getXforCenteredText(title);
        int y = 100;
        g2.drawString(title, x, y);
        
        // Level buttons
        int buttonWidth = 300;
        int buttonHeight = 80;
        int startY = 200;
        int spacing = 100;
        
        // Easy Level Button
        int buttonX = gp.screenWidth/2 - buttonWidth/2;
        int buttonY = startY;
        
        // Highlight on hover effect (using selection)
        if(blinkCounter < 30) {
            g2.setColor(new Color(0, 255, 0, 50));
            g2.fillRoundRect(buttonX - 5, buttonY - 5, buttonWidth + 10, buttonHeight + 10, 20, 20);
        }
        
        g2.setColor(new Color(0, 100, 0));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(Color.GREEN);
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        
        g2.setFont(new Font("Arial", Font.BOLD, 35));
        g2.setColor(Color.WHITE);
        String easyText = "EASY - Press 1";
        x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(easyText)/2;
        y = buttonY + 50;
        g2.drawString(easyText, x, y);
        
        // Medium Level Button
        buttonY = startY + spacing;
        
        if(blinkCounter > 30) {
            g2.setColor(new Color(255, 255, 0, 50));
            g2.fillRoundRect(buttonX - 5, buttonY - 5, buttonWidth + 10, buttonHeight + 10, 20, 20);
        }
        
        g2.setColor(new Color(100, 100, 0));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(Color.YELLOW);
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        
        String mediumText = "MEDIUM - Press 2";
        x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(mediumText)/2;
        y = buttonY + 50;
        g2.drawString(mediumText, x, y);
        
        // Hard Level Button
        buttonY = startY + spacing * 2;
        
        g2.setColor(new Color(100, 0, 0));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(Color.RED);
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20);
        
        String hardText = "HARD - Press 3";
        x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(hardText)/2;
        y = buttonY + 50;
        g2.drawString(hardText, x, y);
        
        // Back instruction
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.GRAY);
        String backText = "Press ESC to return to Title Screen";
        x = getXforCenteredText(backText);
        y = gp.screenHeight - 50;
        g2.drawString(backText, x, y);
        
        // Level descriptions
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(150, 150, 150));
        
        String easyDesc = "Easy: 5 hearts, more health potions, weaker enemies";
        x = getXforCenteredText(easyDesc);
        y = startY + buttonHeight + 15;
        g2.drawString(easyDesc, x, y);
        
        String mediumDesc = "Medium: 3 hearts, fewer items, stronger enemies";
        x = getXforCenteredText(mediumDesc);
        y = startY + spacing + buttonHeight + 15;
        g2.drawString(mediumDesc, x, y);
        
        String hardDesc = "Hard: 2 hearts, rare items, deadly enemies!";
        x = getXforCenteredText(hardDesc);
        y = startY + spacing * 2 + buttonHeight + 15;
        g2.drawString(hardDesc, x, y);
    }
    
    public void drawPlayerHealth() {
        int heartSize = gp.tileSize / 2;
        int startX = gp.screenWidth / 2 - ((gp.player.maxHealth * (heartSize + 5)) / 2);
        int startY = gp.screenHeight - gp.tileSize / 2;
        
        for(int i = 0; i < gp.player.maxHealth; i++) {
            if(i < gp.player.health) {
                g2.setColor(Color.RED);
                g2.fillRoundRect(startX + (i * (heartSize + 5)), startY, heartSize, heartSize, 5, 5);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(startX + (i * (heartSize + 5)) + 5, startY + 5, heartSize - 10, heartSize - 10, 3, 3);
            } else {
                g2.setColor(Color.GRAY);
                g2.fillRoundRect(startX + (i * (heartSize + 5)), startY, heartSize, heartSize, 5, 5);
            }
        }
    }
    
    public void drawTimer() {
        double timeSeconds = gp.getGameTimeSeconds();
        g2.setFont(g2.getFont().deriveFont(28F));
        
        String timeText = "Time: " + String.format("%.2f", timeSeconds) + " s";
        int textLength = (int) g2.getFontMetrics().getStringBounds(timeText, g2).getWidth();
        int x = gp.screenWidth - textLength - 20;
        
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(gp.screenWidth - textLength - 30, 15, textLength + 20, 35, 15, 15);
        g2.setColor(Color.CYAN);
        g2.drawString(timeText, x, 38);
    }
    
    public void drawHazardWarning() {
        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;
        boolean onHazard = gp.tileM.isTileHazardous(playerCenterX, playerCenterY);
        
        if(onHazard) {
            g2.setColor(new Color(255, 0, 0, 50 + (int)(Math.random() * 100)));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            g2.setFont(g2.getFont().deriveFont(20F));
            g2.setColor(Color.RED);
            String warning = "DANGER! TAKING DAMAGE!";
            int x = getXforCenteredText(warning);
            g2.drawString(warning, x, gp.screenHeight - 80);
        }
    }
    
    public void drawPauseScreen() {
        g2.setFont(arial_80B);
        g2.setColor(Color.WHITE);
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
        
        g2.setFont(arial_40);
        String resumeText = "Press P to Resume";
        x = getXforCenteredText(resumeText);
        y += 80;
        g2.drawString(resumeText, x, y);
    }
    
    public void drawDialogueScreen() {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int boxX = gp.tileSize * 2;
        int boxY = gp.screenHeight - gp.tileSize * 4;
        int boxWidth = gp.screenWidth - (gp.tileSize * 4);
        int boxHeight = gp.tileSize * 3;
        
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        g2.setColor(Color.BLACK);
        
        if(currentDialogue != null) {
            String[] words = currentDialogue.split(" ");
            StringBuilder line = new StringBuilder();
            int lineY = boxY + gp.tileSize;
            
            for(String word : words) {
                if(g2.getFontMetrics().stringWidth(line + word) < boxWidth - 40) {
                    line.append(word).append(" ");
                } else {
                    g2.drawString(line.toString(), boxX + 20, lineY);
                    line = new StringBuilder(word + " ");
                    lineY += 30;
                }
            }
            g2.drawString(line.toString(), boxX + 20, lineY);
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.GRAY);
        String instruction = "Press ENTER to continue";
        int x = getXforCenteredText(instruction);
        g2.drawString(instruction, x, boxY + boxHeight - 20);
        
        dialogueCounter++;
        if(dialogueCounter > 10 && (gp.keyH.enterPressed || gp.keyH.upPressed)) {
            dialogueCounter = 0;
            gp.gameState = gp.playState;
            dialogueOn = false;
        }
    }
    
    public void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        g2.setFont(arial_80B);
        g2.setColor(Color.RED);
        String text = "GAME OVER";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2 - 50;
        g2.drawString(text, x, y);
        
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        String timeText = "Time survived: " + String.format("%.2f", gp.getGameTimeSeconds()) + " s";
        x = getXforCenteredText(timeText);
        y += 80;
        g2.drawString(timeText, x, y);
        
        String restartText = "Press R to return to Title Screen";
        x = getXforCenteredText(restartText);
        y += 60;
        g2.drawString(restartText, x, y);
    }
    
    public void drawGameFinished() {
        if(finalTime == 0) {
            finalTime = gp.getGameTimeSeconds();
        }
        
        g2.setFont(arial_80B);
        g2.setColor(Color.ORANGE);
        String text = "Game Finished!";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2 + (gp.tileSize*4);
        g2.drawString(text, x, y);
        
        g2.setFont(g2.getFont().deriveFont(40F));
        String timeText = "Final Time: " + String.format("%.2f", finalTime) + " s";
        x = getXforCenteredText(timeText);
        y = gp.screenHeight/2 + (gp.tileSize*5 + 20);
        g2.drawString(timeText, x, y);
        
        gp.gameThread = null;
    }
    
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}