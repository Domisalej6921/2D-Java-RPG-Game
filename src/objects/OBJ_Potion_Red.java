package objects;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\n" + "A potion that heals.\nIncreases health by " + value + ".";
        price = 50;
        stackable = true;

        setDialogue();
    }

    public void setDialogue() {
         dialogues[0][0] = "You drink the " + name + ". Health increased by " + value + ".";
    }

    public boolean use(Entity entity) {

        startDialogue(this, 0);
        entity.life += value;
        gp.playSE(4);
        return true;
    }
}
