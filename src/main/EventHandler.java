package main;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {

        if(hit(27, 16, "right") == true) {
            damagePit(gp.dialogueState);
        }
        if(hit(27, 16, "right") == true) {
            teleport(gp.dialogueState);
        }
        if(hit(23, 12, "up") == true){
            healingPool(gp.dialogueState);
        }
    }

    public boolean hit(int eventCol, int eventRow, String reqDirection) {

        boolean hit = false;

        //GETTING PLAYERS SOLID-AREA POS
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // GETTING EVENT RECT POS
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        if(gp.player.solidArea.intersects(eventRect)) {
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
            }
        }

        // after checking the collision, reset the player's solid-area x & y position
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit!";
        gp.player.life -= 1;
    }

    public void teleport(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "You have been teleported!";
        gp.player.worldX = 37 * gp.tileSize;
        gp.player.worldY = 10 * gp.tileSize;
    }

    public void healingPool(int gameState) {

        if(gp.keyH.enterPressed == true) {
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You feel refreshed!, You gain health.";
            gp.player.life = gp.player.maxLife;
        }
    }
}
