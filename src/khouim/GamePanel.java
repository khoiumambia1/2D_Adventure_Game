package khouim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;

import entity.NPC;
import entity.Player;
import entity.Enemy;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    private static final long serialVersionUID = 1L;
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    
    public long gameStartTime;
    public long firstKeyPickupTime;
    public long secondKeyPickupTime;
    public long thirdKeyPickupTime;
    public long pauseStartTime = 0;
    public long totalPausedTime = 0;
    
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    
    int FPS = 70;
    
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;
    
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[20];
    public NPC npc[] = new NPC[10];
    public Enemy enemies[] = new Enemy[20];
    
    public int gameState;
    public final int titleState = 0;
    public final int levelSelectState = 1;  // New level select state
    public final int playState = 2;
    public final int pauseState = 3;
    public final int dialogueState = 4;
    public final int gameOverState = 5;
    
    public int currentLevel = 1;  // 1 = Easy, 2 = Medium, 3 = Hard
    
    public GamePanel() {
        gameStartTime = System.currentTimeMillis();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        // Start with title screen
        gameState = titleState;
    }
    
    public void setUpGame() {
        // Game is set up when starting from title screen
    }
    
    public void startGame(int level) {
        currentLevel = level;
        
        // Reset timing
        gameStartTime = System.currentTimeMillis();
        totalPausedTime = 0;
        pauseStartTime = 0;
        
        // Reset player
        player.setDefaultValues();
        player.hasKey = 0;
        player.totalCollectedKey = 0;
        player.health = player.maxHealth;
        player.invincible = false;
        player.invincibleCounter = 0;
        player.attacking = false;
        player.attackCooldown = 0;
        
        // Apply level difficulty settings
        applyLevelSettings();
        
        // Clear all arrays
        obj = new SuperObject[20];
        npc = new NPC[10];
        enemies = new Enemy[20];
        
        // Initialize objects, NPCs, and enemies based on level
        aSetter.setObject(level);
        aSetter.setNPC(level);
        aSetter.setEnemy(level);
        
        // Reset UI
        ui.gameFinished = false;
        ui.finalTime = 0;
        ui.messageOn = false;
        ui.message = "";
        ui.messageCounter = 0;
        ui.dialogueOn = false;
        ui.currentDialogue = "";
        
        // Stop any existing music
        music.stop();
        
        // Play music
        playMusic(0);
        
        // Set game state to play
        gameState = playState;
    }
    
    private void applyLevelSettings() {
        switch(currentLevel) {
            case 1: // Easy
                player.maxHealth = 5;
                player.health = 5;
                player.speed = 4;
                player.attack = 2;
                break;
            case 2: // Medium
                player.maxHealth = 3;
                player.health = 3;
                player.speed = 3;
                player.attack = 2;
                break;
            case 3: // Hard
                player.maxHealth = 2;
                player.health = 2;
                player.speed = 3;
                player.attack = 1;
                break;
        }
    }
    
    public void restartGame() {
        // Stop all current music
        music.stop();
        
        // Return to title screen
        gameState = titleState;
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void saveTimeToFile(String event, double time) {
        try (FileWriter fw = new FileWriter("game_times.txt", true)) {
            fw.write("Level " + currentLevel + " - " + event + " : " + time + " seconds\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void recordEvent(String eventName, long eventTimeMillis) {
        double timeSeconds = eventTimeMillis / 1000.0;
        try (FileWriter fw = new FileWriter("game_times.txt", true)) {
            fw.write("Level " + currentLevel + " - " + eventName + " : " + timeSeconds + " seconds\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public double getGameTimeSeconds() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - gameStartTime - totalPausedTime;
        if (gameState == pauseState && pauseStartTime != 0) {
            elapsed -= (currentTime - pauseStartTime);
        }
        return elapsed / 1000.0;
    }
    
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update() {
        if(gameState == playState) {
            player.update();
            
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }
            
            for(int i = 0; i < enemies.length; i++) {
                if(enemies[i] != null) {
                    enemies[i].update();
                }
            }
            
            if(pauseStartTime != 0) {
                totalPausedTime += System.currentTimeMillis() - pauseStartTime;
                pauseStartTime = 0;
            }
        }
        
        if(gameState == pauseState) {
            if(pauseStartTime == 0) {
                pauseStartTime = System.currentTimeMillis();
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        long drawStart = 0;
        if(keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }
        
        // Only draw game elements if not on title screen or level select
        if(gameState == playState) {
            tileM.draw(g2);
            
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].draw(g2);
                }
            }
            
            for(int i = 0; i < enemies.length; i++) {
                if(enemies[i] != null) {
                    enemies[i].draw(g2);
                }
            }
            
            player.draw(g2);
        }
        
        // Draw UI (includes title screen and level select)
        ui.draw(g2);
        
        if(keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
        }
        
        g2.dispose();
    }
    
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    
    public void stopMusic(int i) {
        music.stop();
    }
    
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}