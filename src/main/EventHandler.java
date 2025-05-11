package main;

import entity.Entity;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][][];
    Entity eventMaster;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventMaster = new Entity(gp);

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;

            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;

                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

        setDialogue();
    }

    public void setDialogue() {

        eventMaster.dialogues[0][0] = "You fell into a pit!";

        eventMaster.dialogues[1][0] = "You feel refreshed!, You gain health and mana.\nYour progress has been saved.";


    }

    public void checkEvent() {

        //Check if the player is more than a tile away from previous event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if (hit(0, 27, 16, "right")) {damagePit(gp.dialogueState);}
            else if (hit(0, 23, 37, "down")) {healingPool(gp.dialogueState);}
            else if(hit(0, 12, 17, "any")) {teleport(1, 6, 26, gp.indoor);} //Goes into traders hut
            else if(hit(1, 6, 26, "any")) {teleport(0, 12, 17, gp.outside);} //Ability to leave traders hut
            else if(hit(1, 6, 23, "up")) {speak(gp.npc[1][0]);} //allows player to talk to merchant through a table
            else if(hit(0, 33, 16, "any")) {teleport(2, 9, 41, gp.dungeon);}//allows player to travel to dungeon
            else if(hit(2, 9, 41, "any")) {teleport(0, 33, 16, gp.outside);}//allows player to leave dungeon
            else if(hit(2, 8, 7, "any")) {teleport(3, 27, 41, gp.dungeon);} //travel to second floor of dungeon
            else if(hit(3, 26, 41, "any") == true) {teleport(2, 8, 7, gp.dungeon);} //travel back to first floor of dungeon
        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {

        boolean hit = false;

        if(map == gp.currentMap) {

            //GETTING PLAYERS SOLID-AREA POS
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            // GETTING EVENT RECT POS
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            // after checking the collision, reset the player's solid-area x & y position
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.playSE(6);
        eventMaster.startDialogue(eventMaster, 0);
        gp.player.life -= 1;
        canTouchEvent = false;
    }

    public void teleport(int map, int col, int row, int area) {

        gp.gameState = gp.transitionState;
        gp.nextArea = area;

        tempMap = map;
        tempCol = col;
        tempRow = row;

        canTouchEvent = false;
        gp.playSE(13);
    }

    public void healingPool(int gameState) {

        if(gp.keyH.enterPressed) {
            gp.gameState = gameState;
            gp.player.attackCancel = true;
            gp.playSE(4);
            eventMaster.startDialogue(eventMaster, 1);
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.assetSetter.setMonster();
            gp.saveLoad.save();
        }
    }

    public void speak(Entity entity) {

        if(gp.keyH.enterPressed) {
            gp.gameState = gp.dialogueState;
            gp.player.attackCancel = true;
            entity.speak();
        }
    }
}
