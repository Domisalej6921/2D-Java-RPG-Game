package main;

import data.Progress;
import entity.NPC_BigRock;
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import interactive_tiles.IT_DestructibleWall;
import interactive_tiles.IT_DryTree;
import interactive_tiles.IT_MetalPlate;
import monster_src.*;
import objects.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        int mapNum = 0;
        int i = 0;

        gp.obj[mapNum][i] = new OBJ_Lantern(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 24;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_Key(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 23;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_Tent(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 22;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 17;
        gp.obj[mapNum][i].worldY = gp.tileSize * 5;
        i++;

        gp.obj[mapNum][i] = new OBJ_Shield_Blue(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 29;
        gp.obj[mapNum][i].worldY = gp.tileSize * 5;
        i++;

        gp.obj[mapNum][i] = new OBJ_Door(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 33;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_Door(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 23;
        gp.obj[mapNum][i].worldY = gp.tileSize * 6;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Key(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 34;
        gp.obj[mapNum][i].worldY = gp.tileSize * 16;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 36;
        gp.obj[mapNum][i].worldY = gp.tileSize * 41;

        //Places stuff in dungeon
        mapNum = 2;
        i = 0;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Pickaxe(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 40;
        gp.obj[mapNum][i].worldY = gp.tileSize * 41;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 13;
        gp.obj[mapNum][i].worldY = gp.tileSize * 16;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 26;
        gp.obj[mapNum][i].worldY = gp.tileSize * 34;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 27;
        gp.obj[mapNum][i].worldY = gp.tileSize * 15;
        i++;

        gp.obj[mapNum][i] = new OBJ_Door_Iron(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 18;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;
        i++;

        mapNum = 3;
        i = 0;

//        gp.obj[mapNum][i] = new OBJ_Door_Iron(gp);
//        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
//        gp.obj[mapNum][i].worldY = gp.tileSize * 15;
//        i++;

        gp.obj[mapNum][i] = new OBJ_BlueHeart(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
        gp.obj[mapNum][i].worldY = gp.tileSize * 8;
        i++;
    }

    public void setNPC() {

        int mapNum = 0;
        int i = 0;

        //MAP 0
        gp.npc[mapNum][i] = new NPC_OldMan(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 21;
        gp.npc[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        //MAP 1
        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new NPC_Merchant(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 6;
        gp.npc[mapNum][i].worldY = gp.tileSize * 21;

        //MAP 2
        mapNum = 2;
        i = 0;

        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 20;
        gp.npc[mapNum][i].worldY = gp.tileSize * 25;
        i++;

        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 11;
        gp.npc[mapNum][i].worldY = gp.tileSize * 18;
        i++;

        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize * 23;
        gp.npc[mapNum][i].worldY = gp.tileSize * 14;
        i++;
    }

    public void setMonster() {

        int i = 0;
        int mapNum = 0;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 24;
        gp.monster[mapNum][i].worldY = gp.tileSize * 1;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 20;
        gp.monster[mapNum][i].worldY = gp.tileSize * 2;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 21;
        gp.monster[mapNum][i].worldY = gp.tileSize * 3;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 28;
        gp.monster[mapNum][i].worldY = gp.tileSize * 2;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 18;
        gp.monster[mapNum][i].worldY = gp.tileSize * 3;
        i++;

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 18;
        gp.monster[mapNum][i].worldY = gp.tileSize * 3;
        i++;

        gp.monster[mapNum][i] = new MON_RedSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 23;
        gp.monster[mapNum][i].worldY = gp.tileSize * 4;
        i++;

        gp.monster[mapNum][i] = new MON_Orc(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 33;
        gp.monster[mapNum][i].worldY = gp.tileSize * 18;
        i++;

        mapNum = 2;
        i = 0;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 34;
        gp.monster[mapNum][i].worldY = gp.tileSize * 39;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 36;
        gp.monster[mapNum][i].worldY = gp.tileSize * 25;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 39;
        gp.monster[mapNum][i].worldY = gp.tileSize * 26;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 28;
        gp.monster[mapNum][i].worldY = gp.tileSize * 11;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 10;
        gp.monster[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        mapNum = 3;
        i = 0;

        if(Progress.skeletonLordDefeated == false) {
            gp.monster[mapNum][i] = new MON_SkeletonLord(gp);
            gp.monster[mapNum][i].worldX = gp.tileSize * 23;
            gp.monster[mapNum][i].worldY = gp.tileSize * 16;
            i++;
        }
    }

    public void setInteractiveTile() {

        //Spawn world map
        int i = 0;
        int mapNum = 0;

        gp.iTiles[mapNum][i] = new IT_DryTree(gp, 30, 23);i++;
        gp.iTiles[mapNum][i] = new IT_DryTree(gp, 30, 24);i++;
        gp.iTiles[mapNum][i] = new IT_DryTree(gp, 36, 37);i++;


        //DUNGEON MAP
        mapNum = 2;
        i = 0;

        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 18, 30);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 17, 31);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 17, 32);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 17, 34);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 18, 34);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 18, 33);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 10, 22);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 10, 24);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 38, 18);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 38, 19);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 38, 20);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 38, 21);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 18, 13);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 18, 14);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 22, 28);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 30, 28);i++;
        gp.iTiles[mapNum][i] = new IT_DestructibleWall(gp, 32, 28);i++;

        gp.iTiles[mapNum][i] = new IT_MetalPlate(gp, 20, 22);i++;
        gp.iTiles[mapNum][i] = new IT_MetalPlate(gp, 8, 17);i++;
        gp.iTiles[mapNum][i] = new IT_MetalPlate(gp, 39, 31);i++;
    }
}
