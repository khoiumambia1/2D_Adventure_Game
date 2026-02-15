package khouim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;
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

    public boolean firstKeyCollected = false;
    public boolean secondKeyCollected = false;
    public boolean thirdKeyCollected = false;


    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    
    //FPS
    int FPS = 60;
    
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    
    public Player player = new Player(this,keyH);
    public SuperObject obj[]= new SuperObject[10];
    
    
    public GamePanel() {
    	gameStartTime = System.currentTimeMillis();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setUpGame() {
    	aSetter.setObject();
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
 // Save time to local file
    public void saveTimeToFile(String event, double time) {
        try (FileWriter fw = new FileWriter("game_times.txt", true)) {
            fw.write(event + " : " + time + " seconds\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void recordEvent(String eventName, long eventTimeMillis) {
        double timeSeconds = eventTimeMillis / 1000.0;

        // 1️⃣ Save to local file
        try (FileWriter fw = new FileWriter("game_times.txt", true)) {
            fw.write(eventName + " : " + timeSeconds + " seconds\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2️⃣ Save to Snowflake
        SnowflakeConnector.saveEvent(eventName, timeSeconds);
    }
    
    public double getGameTimeSeconds() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - gameStartTime) / 1000.0;
    }

    
    
    @Override
    // public void run() {

    //     double drawInterval = 1000000000/FPS;
    //     double nextDrawTime = System.nanoTime() + drawInterval;

    //     while(gameThread != null)
    //     {
    //         long currentTime = System.nanoTime();
    //         // System.out.println("current Time: "+ currentTime);
            
    //         update();
            
    //         repaint();

    //         try {
    //             double remainingTime = nextDrawTime - System.nanoTime();
    //             remainingTime /=1000000;

    //             if(remainingTime<0)
    //             {
    //                 remainingTime = 0;
    //             }


    //             Thread.sleep((long) remainingTime);

    //             nextDrawTime += drawInterval;

    //         } catch (InterruptedException e) {
    //         }
    //     }
    // }
    
    
    

    public void run()
    {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta>=1)
            {
                update();
                repaint();
                delta--;
                drawCount++;
            }
//            if(timer>=1000000000)
//            {
//                System.out.println("FPS:" + drawCount);
//                drawCount = 0;
//                timer=0;
//            }
        }
    }
    public void update()
    {
       player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        tileM.draw(g2);
        
        for(int i =0; i<obj.length;i++)
        {
        	if(obj[i]!=null)
        	{
        		obj[i].draw(g2, this);
        	}
        }
        
        player.draw(g2);

        ui.draw(g2);
        g2.dispose();
    }
}
