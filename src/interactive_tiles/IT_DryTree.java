package interactive_tiles;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTiles {

    GamePanel gp;

    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = col * gp.tileSize;
        this.worldY = row * gp.tileSize;

        down1 = setup("interactive_tiles/drytree", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 3;
    }
    public boolean isCorrectItem(Entity entity) {
        boolean correctItem = false;

        if (entity.currentWeapon.type == type_axe) {
            correctItem = true;
        }
        return correctItem;
    }

    public void playSE() {
        gp.playSE(11);
    }

    public InteractiveTiles getDestroyedForm() {
        InteractiveTiles tile = new IT_Trunk(gp, worldX/gp.tileSize, worldY/gp.tileSize);
        return tile;
    }
}
