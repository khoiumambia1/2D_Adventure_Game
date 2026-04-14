package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import khouim.GamePanel;
import khouim.KeyHandler;
import khouim.UtilityTool;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    
    public int dx = 0;
    public int dy = 0;
    
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public int totalCollectedKey = 0;
    
    // Combat properties
    public int attackCooldown = 0;
    public boolean attacking = false;
    public int attackFrame = 0;
    
    // Hazard damage
    public int hazardDamageCounter = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 20;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 22;
        solidArea.height = 22;
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 1;
        worldY = gp.tileSize * 1;
        speed = 4;
        direction = "down";
        
        // Health settings - 3 hearts
        maxHealth = 3;
        health = maxHealth;
        attack = 2;
        defense = 1;
    }
    
    public void getPlayerImage() {
        for(int i = 0; i < 4; i++) {
            up[i] = setup("up" + (i+1));
            down[i] = setup("down" + (i+1));
            left[i] = setup("left" + (i+1));
            right[i] = setup("right" + (i+1));
        }
        for(int i = 0; i < 4; i++) {
            attack_u[i] = setup("attack_u");
            attack_d[i] = setup("attack_d");
            attack_l[i] = setup("attack_l");
            attack_r[i] = setup("attack_r");
        }
    }
    
    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            if(image != null) {
                image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
            } else {
                image = createFallbackPlayerImage();
            }
        } catch(IOException e) {
            image = createFallbackPlayerImage();
        }
        return image;
    }
    
    private BufferedImage createFallbackPlayerImage() {
        BufferedImage img = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.GREEN);
        g2.fillRoundRect(gp.tileSize/4, gp.tileSize/4, gp.tileSize/2, gp.tileSize/2, 10, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval(gp.tileSize/3, gp.tileSize/3, gp.tileSize/8, gp.tileSize/8);
        g2.dispose();
        return img;
    }
    
    public void checkHazardousTile() {
        int playerCenterX = worldX + gp.tileSize / 2;
        int playerCenterY = worldY + gp.tileSize / 2;
        
        boolean onHazard = gp.tileM.isTileHazardous(playerCenterX, playerCenterY);
        
        if(onHazard && !invincible) {
            if(hazardDamageCounter <= 0) {
                takeDamage(1);
                hazardDamageCounter = 30;
                gp.ui.showMessage("Burning! Taking damage!");
            } else {
                hazardDamageCounter--;
            }
        } else {
            hazardDamageCounter = 0;
        }
    }
    
    public void update() {
        if(health <= 0) {
            die();
            return;
        }
        
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter >= 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        
        if(attackCooldown > 0) {
            attackCooldown--;
        }
        
        if(keyH.attackPressed && attackCooldown == 0 && !attacking) {
            attacking = true;
            attackCooldown = 20;
            attackFrame = 0;
            performAttack();
        }
        
        if(attacking) {
            attackFrame++;
            if(attackFrame >= 10) {
                attacking = false;
                attackFrame = 0;
            }
        }
        
        boolean moving = false;
        
        if(!attacking || attackFrame >= 8) {
            if(keyH.upPressed) {
                direction = "up";
                moving = true;
            }
            else if(keyH.downPressed) {
                direction = "down";
                moving = true;
            }
            else if(keyH.leftPressed) {
                direction = "left";
                moving = true;
            }
            else if(keyH.rightPressed) {
                direction = "right";
                moving = true;
            }
        }
        
        if(moving && (!attacking || attackFrame >= 8)) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            int objIndex = gp.cChecker.checkObject(this, true);
            pickupObject(objIndex);
            
            int enemyIndex = gp.cChecker.checkEntity(this, gp.enemies);
            if(enemyIndex != 999 && gp.enemies[enemyIndex] != null) {
                takeDamage(gp.enemies[enemyIndex].damage);
            }
            
            if(!collisionOn) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            spriteCounter++;
            if(spriteCounter > 10) {
                spriteIndex++;
                if(spriteIndex >= 4) spriteIndex = 0;
                spriteCounter = 0;
            }
        } 
        else {
            if(!attacking) {
                spriteIndex = 0;
                spriteCounter = 0;
            }
        }
        
        checkHazardousTile();
    }
    
    public void performAttack() {
        int attackRange = gp.tileSize;
        Rectangle attackArea = new Rectangle();
        
        switch(direction) {
            case "up":
                attackArea = new Rectangle(worldX + solidArea.x, worldY + solidArea.y - attackRange, 
                                         solidArea.width, attackRange);
                break;
            case "down":
                attackArea = new Rectangle(worldX + solidArea.x, worldY + solidArea.y + solidArea.height, 
                                         solidArea.width, attackRange);
                break;
            case "left":
                attackArea = new Rectangle(worldX + solidArea.x - attackRange, worldY + solidArea.y, 
                                         attackRange, solidArea.height);
                break;
            case "right":
                attackArea = new Rectangle(worldX + solidArea.x + solidArea.width, worldY + solidArea.y, 
                                         attackRange, solidArea.height);
                break;
        }
        
        for(int i = 0; i < gp.enemies.length; i++) {
            if(gp.enemies[i] != null) {
                Rectangle enemyArea = new Rectangle(
                    gp.enemies[i].worldX + gp.enemies[i].solidArea.x,
                    gp.enemies[i].worldY + gp.enemies[i].solidArea.y,
                    gp.enemies[i].solidArea.width,
                    gp.enemies[i].solidArea.height
                );
                
                if(attackArea.intersects(enemyArea)) {
                    gp.enemies[i].takeDamage(attack);
                    gp.playSE(6);
                    gp.ui.showMessage("Hit! Damage: " + attack);
                }
            }
        }
    }
    
    public void takeDamage(int damage) {
        if(!invincible) {
            int actualDamage = Math.max(1, damage - defense);
            health -= actualDamage;
            invincible = true;
            gp.playSE(7);
            
            gp.ui.showMessage("Took " + actualDamage + " damage!");
            
            if(health <= 0) {
                die();
            }
        }
    }
    
    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
        gp.ui.showMessage("Healed " + amount + " HP!");
    }
    
    public void die() {
        gp.ui.showMessage("You died! Game Over!");
        gp.gameState = gp.gameOverState;
    }
    
    public void pickupObject(int i) {
        if(i != 999 && gp.obj[i] != null) {
            String objectName = gp.obj[i].name;
            long now = System.currentTimeMillis();

            switch(objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    totalCollectedKey++;
                    if(totalCollectedKey == 1) {
                        gp.firstKeyPickupTime = now;
                        gp.recordEvent("FIRST_KEY", now - gp.gameStartTime);
                    } else if(totalCollectedKey == 2) {
                        gp.secondKeyPickupTime = now;
                        gp.recordEvent("SECOND_KEY", now - gp.gameStartTime);
                    } else if(totalCollectedKey == 3) {
                        gp.thirdKeyPickupTime = now;
                        gp.recordEvent("THIRD_KEY", now - gp.gameStartTime);
                    }
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a key!");
                    break;

                case "Door":
                    gp.playSE(3);
                    if(hasKey > 0) {
                        if(hasKey == 1) {
                            gp.recordEvent("FIRST_KEY_TO_DOOR", now - gp.firstKeyPickupTime);
                        } else if(hasKey == 2) {
                            gp.recordEvent("SECOND_KEY_TO_DOOR", now - gp.secondKeyPickupTime);
                        } else if(hasKey == 3) {
                            gp.recordEvent("THIRD_KEY_TO_DOOR", now - gp.thirdKeyPickupTime);
                            gp.recordEvent("TOTAL_GAME_TIME", now - gp.gameStartTime);
                        }
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("Door opened!");
                    } else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                    
                case "Boot":
                    gp.playSE(2);
                    speed += 1;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed increased!");
                    break;
                    
                case "Chest":
                    gp.stopMusic(0);
                    gp.playSE(4);
                    gp.ui.gameFinished = true;
                    break;
                    
                case "HealthPotion":
                    gp.playSE(1);
                    heal(1);
                    gp.obj[i] = null;
                    break;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int drawWidth = gp.tileSize;
        int drawHeight = gp.tileSize;
        int drawX = screenX;
        int drawY = screenY;
        
        if(attacking) {
            int attackFrameIndex = attackFrame / 2;
            if(attackFrameIndex >= 4) attackFrameIndex = 3;
            
            switch(direction) {
                case "up": 
                    if(attack_u != null && attackFrameIndex < attack_u.length && attack_u[attackFrameIndex] != null) {
                        image = attack_u[attackFrameIndex];
                        // Extend upward
                        drawWidth = gp.tileSize* 3/2;
                        drawHeight = gp.tileSize;
                        drawY = screenY;  // Move up by one tile
                    }
                    break;
                case "down": 
                    if(attack_d != null && attackFrameIndex < attack_d.length && attack_d[attackFrameIndex] != null) {
                        image = attack_d[attackFrameIndex];
                        // Extend downward
                        drawWidth = gp.tileSize* 3/2;
                        drawHeight = gp.tileSize;
                        drawY = screenY;  // Keep same Y, height extends down
                    }
                    break;
                case "left": 
                    if(attack_l != null && attackFrameIndex < attack_l.length && attack_l[attackFrameIndex] != null) {
                        image = attack_l[attackFrameIndex];
                        // Extend leftward
                        drawWidth = gp.tileSize * 3/2;
                        drawHeight = gp.tileSize;
                        drawX = screenX - gp.tileSize;  // Move left by one tile
                    }
                    break;
                case "right": 
                    if(attack_r != null && attackFrameIndex < attack_r.length && attack_r[attackFrameIndex] != null) {
                        image = attack_r[attackFrameIndex];
                        // Extend rightward
                        drawWidth = gp.tileSize * 3/2;
                        drawHeight = gp.tileSize;
                        drawX = screenX;  // Keep same X, width extends right
                    }
                    break;
            }
        }
        
        if(image == null) {
            switch(direction) {
                case "up": 
                    if(up != null && spriteIndex < up.length) image = up[spriteIndex];
                    break;
                case "down": 
                    if(down != null && spriteIndex < down.length) image = down[spriteIndex];
                    break;
                case "left": 
                    if(left != null && spriteIndex < left.length) image = left[spriteIndex];
                    break;
                case "right": 
                    if(right != null && spriteIndex < right.length) image = right[spriteIndex];
                    break;
            }
            drawWidth = gp.tileSize;
            drawHeight = gp.tileSize;
            drawX = screenX;
            drawY = screenY;
        }
        
        if(invincible && (invincibleCounter / 5) % 2 == 0) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        
        if(image != null) {
            g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
        }
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}