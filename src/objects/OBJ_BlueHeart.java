package objects;

import entity.Entity;
import main.GamePanel;

public class OBJ_BlueHeart extends Entity {

    GamePanel gp;
    public static final String objName = "Blue Heart";

    public OBJ_BlueHeart(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        down1 = setup("objects/diamond", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nA heart that is blue.\nIt looks shiny.....";

        setDialogue();
    }

    public void setDialogue() {

        dialogues[0][0] = "You noticed the shine.";
        dialogues[0][1] = "From a distance....";
        dialogues[0][2] = "This is the blue heart.\nA legendary treasure....\nWhich everyone has been after...";
        dialogues[0][3] = "Congratulations!!\nYou have freed the land....";
        dialogues[0][4] = "You are the hero.\nAnd completed the game....";
    }

    public boolean use(Entity entity) {

        gp.gameState = gp.cutSceneState;
        gp.csManager.sceneNum = gp.csManager.ending;
        return true;
    }
}
