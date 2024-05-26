package interactive_tiles;

import entity.Entity;
import main.GamePanel;


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
}
