package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {

        up1 = setup("npc/oldman_up_1");
        up2 = setup("npc/oldman_up_2");
        down1 = setup("npc/oldman_down_1");
        down2 = setup("npc/oldman_down_2");
        left1 = setup("npc/oldman_left_1");
        left2 = setup("npc/oldman_left_2");
        right1 = setup("npc/oldman_right_1");
        right2 = setup("npc/oldman_right_2");

    }

    public void setDialogue() {
        dialogues[0] = "Hello, young one.";
        dialogues[1] = "I don't think we have met,\nI'm sheik. I have been roaming\nthis land for a long time.";
        dialogues[2] = "Head into the forest to the north\nto find the first temple.";
        dialogues[3] = "Or head into the village of\nthe south to get yourself better\nequipped.";
        dialogues[4] = "Fun travels, young one.";
    }

    public void setAction() {

        actionLockCounter ++;

        if(actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) + 1; // Picks a random number from 1 to 100.

            if (i <= 25) {
                direction = "up";
            } else if (i > 25 && i <= 50) {
                direction = "down";
            } else if (i > 50 && i <= 75) {
                direction = "left";
            } else if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

    public void speak() {

        //Do this character specific stuff
        super.speak();
    }
}
