package interactive_tiles;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class IT_DestructibleWall extends InteractiveTiles{

    GamePanel gp;

    public IT_DestructibleWall(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;

        down1 = setup("interactive_tiles/destructiblewall", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 3;
    }
    public boolean isCorrectItem(Entity entity) {
        boolean correctItem = false;

        if (entity.currentWeapon.type == type_pickaxe) {
            correctItem = true;
        }
        return correctItem;
    }

    public void playSE() {
        gp.playSE(20);
    }

    public InteractiveTiles getDestroyedForm() {
        InteractiveTiles tile = null;
        return tile;
    }

    public Color getParticleColor() {
        Color color = new Color(65, 65, 65);
        return color;
    }

    public int getParticleSize() {
        int size = 6; // 6 pixels
        return size;
    }

    public int getParticleSpeed() {
        int speed = 1; // 2 pixels per frame
        return speed;
    }

    public int getParticleMaxLife() {
        int maxLife = 20; // 30 frames
        return maxLife;
    }
}
