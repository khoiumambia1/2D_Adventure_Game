package khouim;

import object.OBJBoot;
import object.OBJChest;
import object.OBJDoor;
import object.OBJKey;
import object.HealthPotion;
import entity.NPC;
import entity.Enemy;

public class AssetSetter {
    
    GamePanel gp;
    
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(int level) {
        // Clear existing objects
        gp.obj = new object.SuperObject[20];
        
        switch(level) {
            case 1:
                setEasyLevelObjects();
                break;
            case 2:
                setMediumLevelObjects();
                break;
            case 3:
                setHardLevelObjects();
                break;
        }
    }
    
    private void setEasyLevelObjects() {
        // Keys
        gp.obj[0] = new OBJKey(gp);
        gp.obj[0].worldX = 33 * gp.tileSize;
        gp.obj[0].worldY = 38 * gp.tileSize;
        
        gp.obj[1] = new OBJKey(gp);
        gp.obj[1].worldX = 1 * gp.tileSize;
        gp.obj[1].worldY = 43 * gp.tileSize;
        
        gp.obj[2] = new OBJKey(gp);
        gp.obj[2].worldX = 48 * gp.tileSize;
        gp.obj[2].worldY = 21 * gp.tileSize;
        
        // Doors
        gp.obj[3] = new OBJDoor(gp);
        gp.obj[3].worldX = 1 * gp.tileSize;
        gp.obj[3].worldY = 11 * gp.tileSize;
        
        gp.obj[4] = new OBJDoor(gp);
        gp.obj[4].worldX = 1 * gp.tileSize;
        gp.obj[4].worldY = 47 * gp.tileSize;
        
        gp.obj[5] = new OBJDoor(gp);
        gp.obj[5].worldX = 11 * gp.tileSize;
        gp.obj[5].worldY = 27 * gp.tileSize;
        
        // Chest
        gp.obj[6] = new OBJChest(gp);
        gp.obj[6].worldX = 1 * gp.tileSize;
        gp.obj[6].worldY = 48 * gp.tileSize;
        
        // Power-ups
        gp.obj[7] = new OBJBoot(gp);
        gp.obj[7].worldX = 12 * gp.tileSize;
        gp.obj[7].worldY = 13 * gp.tileSize;
        
        // Health potions
        gp.obj[8] = new HealthPotion(gp);
        gp.obj[8].worldX = 20 * gp.tileSize;
        gp.obj[8].worldY = 20 * gp.tileSize;
        
        gp.obj[9] = new HealthPotion(gp);
        gp.obj[9].worldX = 35 * gp.tileSize;
        gp.obj[9].worldY = 30 * gp.tileSize;
        
        gp.obj[10] = new HealthPotion(gp);
        gp.obj[10].worldX = 25 * gp.tileSize;
        gp.obj[10].worldY = 40 * gp.tileSize;
    }
    
    private void setMediumLevelObjects() {
    	// Keys
        gp.obj[0] = new OBJKey(gp);
        gp.obj[0].worldX = 33 * gp.tileSize;
        gp.obj[0].worldY = 38 * gp.tileSize;
        
        gp.obj[1] = new OBJKey(gp);
        gp.obj[1].worldX = 1 * gp.tileSize;
        gp.obj[1].worldY = 43 * gp.tileSize;
        
        gp.obj[2] = new OBJKey(gp);
        gp.obj[2].worldX = 48 * gp.tileSize;
        gp.obj[2].worldY = 21 * gp.tileSize;
        
        // Doors
        gp.obj[3] = new OBJDoor(gp);
        gp.obj[3].worldX = 1 * gp.tileSize;
        gp.obj[3].worldY = 11 * gp.tileSize;
        
        gp.obj[4] = new OBJDoor(gp);
        gp.obj[4].worldX = 1 * gp.tileSize;
        gp.obj[4].worldY = 47 * gp.tileSize;
        
        gp.obj[5] = new OBJDoor(gp);
        gp.obj[5].worldX = 11 * gp.tileSize;
        gp.obj[5].worldY = 27 * gp.tileSize;
        
        // Chest
        gp.obj[6] = new OBJChest(gp);
        gp.obj[6].worldX = 1 * gp.tileSize;
        gp.obj[6].worldY = 48 * gp.tileSize;
        
        // Fewer power-ups
        gp.obj[7] = new OBJBoot(gp);
        gp.obj[7].worldX = 15 * gp.tileSize;
        gp.obj[7].worldY = 15 * gp.tileSize;
        
        // Fewer health potions
        gp.obj[8] = new HealthPotion(gp);
        gp.obj[8].worldX = 25 * gp.tileSize;
        gp.obj[8].worldY = 25 * gp.tileSize;
    }
    
    private void setHardLevelObjects() {
    	// Keys
        gp.obj[0] = new OBJKey(gp);
        gp.obj[0].worldX = 33 * gp.tileSize;
        gp.obj[0].worldY = 38 * gp.tileSize;
        
        gp.obj[1] = new OBJKey(gp);
        gp.obj[1].worldX = 1 * gp.tileSize;
        gp.obj[1].worldY = 43 * gp.tileSize;
        
        gp.obj[2] = new OBJKey(gp);
        gp.obj[2].worldX = 48 * gp.tileSize;
        gp.obj[2].worldY = 21 * gp.tileSize;
        
        // Doors
        gp.obj[3] = new OBJDoor(gp);
        gp.obj[3].worldX = 1 * gp.tileSize;
        gp.obj[3].worldY = 11 * gp.tileSize;
        
        gp.obj[4] = new OBJDoor(gp);
        gp.obj[4].worldX = 1 * gp.tileSize;
        gp.obj[4].worldY = 47 * gp.tileSize;
        
        gp.obj[5] = new OBJDoor(gp);
        gp.obj[5].worldX = 11 * gp.tileSize;
        gp.obj[5].worldY = 27 * gp.tileSize;
        
        // Chest
        gp.obj[6] = new OBJChest(gp);
        gp.obj[6].worldX = 1 * gp.tileSize;
        gp.obj[6].worldY = 48 * gp.tileSize;
        
        // No power-ups on hard mode
        // Only one health potion
        gp.obj[7] = new HealthPotion(gp);
        gp.obj[7].worldX = 30 * gp.tileSize;
        gp.obj[7].worldY = 30 * gp.tileSize;
    }
    
    public void setNPC(int level) {
        gp.npc = new NPC[10];
        
        switch(level) {
            case 1:
                setEasyLevelNPC(level);
                break;
            case 2:
                setMediumLevelNPC(level);
                break;
            case 3:
                setHardLevelNPC(level);
                break;
        }
    }
    
    private void setEasyLevelNPC(int level) {
        NPC npc1 = new NPC(gp);
        npc1.worldX = gp.tileSize * 10;
        npc1.worldY = gp.tileSize * 15;
        npc1.setDialogue(level);
        gp.npc[0] = npc1;
        
        NPC npc2 = new NPC(gp);
        npc2.worldX = gp.tileSize * 20;
        npc2.worldY = gp.tileSize * 32;
        npc2.setDialogue(level);
        gp.npc[1] = npc2;
    }
    
    private void setMediumLevelNPC(int level) {
        NPC npc1 = new NPC(gp);
        npc1.worldX = gp.tileSize * 15;
        npc1.worldY = gp.tileSize * 20;
        npc1.setDialogue(level);
        gp.npc[0] = npc1;
    }
    
    private void setHardLevelNPC(int level) {
        NPC npc1 = new NPC(gp);
        npc1.worldX = gp.tileSize * 2;
        npc1.worldY = gp.tileSize * 2;
        npc1.setDialogue(level);
        gp.npc[0] = npc1;
    }
    
    public void setEnemy(int level) {
        gp.enemies = new Enemy[20];
        
        switch(level) {
            case 1:
                setEasyLevelEnemies();
                break;
            case 2:
                setMediumLevelEnemies();
                break;
            case 3:
                setHardLevelEnemies();
                break;
        }
    }
    
    private void setEasyLevelEnemies() {
        Enemy enemy1 = new Enemy(gp);
        enemy1.worldX = gp.tileSize * 18;
        enemy1.worldY = gp.tileSize * 18;
        enemy1.health = 2;
        gp.enemies[0] = enemy1;
        
        Enemy enemy2 = new Enemy(gp);
        enemy2.worldX = gp.tileSize * 25;
        enemy2.worldY = gp.tileSize * 35;
        enemy2.health = 2;
        gp.enemies[1] = enemy2;
    }
    
    private void setMediumLevelEnemies() {
//        Enemy 1 - Near starting area
        Enemy enemy1 = new Enemy(gp);
        enemy1.worldX = gp.tileSize * 6;
        enemy1.worldY = gp.tileSize * 3;
        enemy1.health = 3;
        enemy1.speed = 2;
        gp.enemies[0] = enemy1;
        
        // Enemy 2 - Near first key location
        Enemy enemy2 = new Enemy(gp);
        enemy2.worldX = gp.tileSize * 32;
        enemy2.worldY = gp.tileSize * 26;
        enemy2.health = 3;
        enemy2.speed = 2;
        gp.enemies[1] = enemy2;
        
        // Enemy 3 - Guarding a door
        Enemy enemy3 = new Enemy(gp);
        enemy3.worldX = gp.tileSize * 2;
        enemy3.worldY = gp.tileSize * 20;
        enemy3.health = 3;
        enemy3.speed = 2;
        gp.enemies[2] = enemy3;
    }
    private void setHardLevelEnemies() {
        // Enemy 1 - Near starting area
        Enemy enemy1 = new Enemy(gp);
        enemy1.worldX = gp.tileSize * 6;
        enemy1.worldY = gp.tileSize * 3;
        enemy1.health = 4;
        enemy1.speed = 3;
        enemy1.damage = 2;
        gp.enemies[0] = enemy1;
        
        // Enemy 2 - Near first key location
        Enemy enemy2 = new Enemy(gp);
        enemy2.worldX = gp.tileSize * 32;
        enemy2.worldY = gp.tileSize * 26;
        enemy2.health = 4;
        enemy2.speed = 3;
        enemy2.damage = 2;
        gp.enemies[1] = enemy2;
        
        // Enemy 3 - Guarding a door
        Enemy enemy3 = new Enemy(gp);
        enemy3.worldX = gp.tileSize * 2;
        enemy3.worldY = gp.tileSize * 20;
        enemy3.health = 4;
        enemy3.speed = 3;
        enemy3.damage = 2;
        gp.enemies[2] = enemy3;
        
        // Enemy 4 - Near chest area
        Enemy enemy4 = new Enemy(gp);
        enemy4.worldX = gp.tileSize * 48;
        enemy4.worldY = gp.tileSize * 18;
        enemy4.health = 4;
        enemy4.speed = 3;
        enemy4.damage = 2;
        gp.enemies[3] = enemy4;
        
        // Enemy 5 - Patrolling key area
        Enemy enemy5 = new Enemy(gp);
        enemy5.worldX = gp.tileSize * 10;
        enemy5.worldY = gp.tileSize * 27;
        enemy5.health = 4;
        enemy5.speed = 3;
        enemy5.damage = 2;
        gp.enemies[4] = enemy5;
        
        // Enemy 6 - Additional hard enemy
        Enemy enemy6 = new Enemy(gp);
        enemy6.worldX = gp.tileSize * 6;
        enemy6.worldY = gp.tileSize * 8;
        enemy6.health = 5;
        enemy6.speed = 3;
        enemy6.damage = 2;
        gp.enemies[5] = enemy6;
    }
}