package interactive_tiles;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;


public class InteractiveTiles extends Entity {

    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTiles(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity) {
        boolean correctItem = false;
        return correctItem;
    }

    public void playSE() {

    }

    public InteractiveTiles getDestroyedForm() {
        InteractiveTiles tile = null;
        return tile;
    }

    public void update() {

        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(down1, screenX, screenY, null);
            changeAlpha(g2, 1f);
        }
    }
}
