package main;

import entity.MockPlayer;
import monster_src.MON_SkeletonLord;
import objects.OBJ_BlueHeart;
import objects.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    //Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending = 2;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;

        endCredit = "Programming/Music\n" +
                "Dominick George\n" +
                "\n\n\n\n\n\n\n\n\n\n\n" +
                "----------------------" +
                "\n\n\n\n\n\n\n\n\n\n\n" +
                "Art\n" +
                "RyiSnow\n" +
                "\n\n\n\n\n\n\n\n\n\n\n" +
                "----------------------" +
                "\n\n\n\n\n\n\n\n\n\n\n" +
                "Special Thanks:\n" +
                "Testing People\n" +
                "You the player!" +
                "\n\n\n\n\n\n\n\n\n\n\n" +
                "----------------------" +
                "Thank you for playing!" +
                "----------------------" +
                "\n\n\n\n\n\n\n\n\n\n\n";
    }

    public void draw(Graphics2D g2) {
       this.g2 = g2;

       switch(sceneNum) {
           case skeletonLord: scene_skeletonLord(); break;
           case ending: scene_ending(); break;
       }
    }

    public void scene_skeletonLord() {

        if (scenePhase == 0) {
            gp.bossBattleOn = true;

            //shut the entrance door
            for (int i = 0; i < gp.obj[1].length; i++) {
                if (gp.obj[gp.currentMap][i] == null) {
                    gp.obj[gp.currentMap][i] = new OBJ_Door_Iron(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.tileSize * 25;
                    gp.obj[gp.currentMap][i].worldY = gp.tileSize * 28;
                    gp.obj[gp.currentMap][i].temp = true;
                    gp.playSE(21);
                    break;
                }
            }

            // Set a fake player in position (searching for vacant slot)
            for (int i = 0; i < gp.npc[1].length; i++) {
                if (gp.npc[gp.currentMap][i] == null) {
                    gp.npc[gp.currentMap][i] = new MockPlayer(gp);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].direction = gp.player.direction;
                    break;
                }
            }

            gp.player.drawing = false;

            scenePhase++;
        }

        if (scenePhase == 1) {

            gp.player.worldY -= 2;

            if (gp.player.worldY < gp.tileSize * 16) {
                scenePhase++;
            }
        }

        if (scenePhase == 2) {
            for (int i = 0; i < gp.monster[1].length; i++) {
                if (gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].name.equals(MON_SkeletonLord.monName)) {
                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }

        if (scenePhase == 3) {

            // Boss dialogue
            gp.ui.drawDialogueScreen();
        }

        if (scenePhase == 4) {

            //Return to player
            //Search for mock player
            for(int i =0; i < gp.npc[1].length; i++) {

                if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(MockPlayer.npcName)) {
                    // restore player's position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                    gp.player.direction = gp.npc[gp.currentMap][i].direction;

                    //delete mock player
                    gp.npc[gp.currentMap][i] = null;
                    break;
                }
            }

            //Start drawing player again
            gp.player.drawing = true;

            //Reset values
            scenePhase = 0;
            sceneNum = NA;
            gp.gameState = gp.playState;

            //Play the boss music
            gp.stopMusic();
            gp.playMusic(22);
        }
    }

    public void scene_ending() {

        if(scenePhase == 0) {
            gp.stopMusic();
            gp.ui.npc = new OBJ_BlueHeart(gp);
            scenePhase++;
        }

        if (scenePhase == 1) {
            // Display dialogue
            gp.ui.drawDialogueScreen();
        }

        if (scenePhase == 2) {

            //Play the fanfare music
            gp.playSE(2);
            scenePhase++;
        }

        if (scenePhase == 3) {
            // wait until sound effect ends
            if(counterReached(300)){
                scenePhase++;
            }
        }

        if (scenePhase == 4) {
            //The screen gets darker
            alpha += 0.005f;
            if(alpha > 1f) {
                alpha = 1f;
            }
            drawBlackBackground(alpha);

            if (alpha == 1f) {
                alpha = 0f;
                scenePhase++;
            }
        }

        if (scenePhase == 5) {

            drawBlackBackground(1f);
            alpha += 0.005f;
            if(alpha > 1f) {
                alpha = 1f;
            }

            String text = "After a long journey,\n" +
                    "You have finally defeated\n" + "what some said impossible.\n"
                    + "You are the hero of the land.\n" +
                    "You have freed the land from evil....\n" +
                    "and completed the game....";
            drawString(alpha, 38f, 130, text, 70);

            if (counterReached(600)) {
                gp.playMusic(0);
                scenePhase++;
            }
        }

        if (scenePhase == 6) {
            drawBlackBackground(1f);

            drawString(1f, 120f, gp.screenHeight / 2, "The End", 120);

            if (counterReached(240)) {
                scenePhase++;
            }
        }

        if (scenePhase == 7) {
            drawBlackBackground(1f);

            drawString(1f, 120f, gp.screenHeight / 2, "Blue Boy Adventure", 120);

            if (counterReached(480)) {
                scenePhase++;
            }
        }

        if (scenePhase == 8) {

            drawBlackBackground(1f);

            y = gp.screenHeight / 2;

            drawString(1f, 38f, y, endCredit, 40);

            if (counterReached(480)) {
                scenePhase++;
            }
        }

        if (scenePhase == 9) {
            drawBlackBackground(1f);

            y--;
            drawString(1f, 38f, y, endCredit, 40);
        }
    }

    public boolean counterReached(int target) {
        boolean counterReached = false;
        counter++;
        if (counter > target) {
            counter = 0;
            counterReached = true;
        }

        return counterReached;
    }

    public void drawBlackBackground(float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for (String line: text.split("\n")) {
            int x = gp.ui.getXforCenteredText(line);
            g2.drawString(line, x, y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
