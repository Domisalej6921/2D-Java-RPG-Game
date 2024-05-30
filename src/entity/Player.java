package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import objects.OBJ_Fireball;
import objects.OBJ_Key;
import objects.OBJ_Shield_Wood;
import objects.OBJ_Sword_Normal;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean attackCancel = false;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        //Collision Area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        
//        worldX = gp.tileSize * 23;
//        worldY = gp.tileSize * 21;
        worldX = gp.tileSize * 12;
        worldY = gp.tileSize * 13;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; //Judges how much damage they can do.
        dexterity = 1; //Judges how much damage they can receive.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack = getAttack();
        defense = getDefense();
    }

    public void setDefaultPositions() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void restoreLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    public void setItems() {

        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    //Total attack is decided by strength and weapon attack value
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    //Total defense is decided by dexterity and shield defense value
    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {

        up1 = setup("player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("player/boy_right_2", gp.tileSize, gp.tileSize);

    }

    public void getPlayerAttackImage() {

        if(currentWeapon.type == type_sword) {

            attackUp1 = setup("player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }
        if(currentWeapon.type == type_axe) {

            attackUp1 = setup("player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }

    public void update() {

        if(attacking){
            attacking();
        }
        else if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {
            if(keyH.upPressed == true) {
                direction = "up";
            }
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
            }

            //Checks if players colliding with objects
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //Check Object Collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK INTERACTIVE TILES COLLISION
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTiles);


            //CHECK EVENT
            gp.eHandler.checkEvent();

            //If Collision is false then player can move
            if(collisionOn == false && keyH.enterPressed == false) {

                switch(direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                }
            }

            if(keyH.enterPressed && !attackCancel) {
                attacking = true;
                gp.playSE(7);
                spriteCounter = 0;
            }

            attackCancel = false;
            gp.keyH.enterPressed = false;

            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if(gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)) {

            // SET DEFAULT COORDS, DIRECTION AND USER
            projectile.set(worldX, worldY, direction, true, this);

            //SUBTRACT PROJECTILE COST (MANA, AMMO, ETC)
            projectile.subtractResource(this);

            // ADD TO THE PROJECTILE LIST
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;

            gp.playSE(10);
        }
        // This needs to be placed outside of key if statement!
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if(life > maxLife) {
            life = maxLife;
        }
        if(mana > maxMana) {
            mana = maxMana;
        }
        if(life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12);
        }
    }

    private void attacking() {

        spriteCounter++;

        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            //Save the current worldX, worldY, solidAreaWidth, and solidAreaHeight
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX / Y for the attackArea
            switch(direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }

            //AttackArea becomes SolidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //Check Monster Collision with updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTiles);
            damageInteractiveTile(iTileIndex);

            //RESTORE DATA after checking collision
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }

    public void pickUpObject(int i) {

        if (i != 999) {

            //PICK-UP ONLY ITEMS
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            } else {

                //INVENTORY ITEMS
                String text;

                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][i].name;
                } else {
                    text = "Inventory is full!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }

    public void interactNPC(int i) {

        if(gp.keyH.enterPressed) {
            if (i != 999) {
                attackCancel = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            }
        }
    }

    public void contactMonster(int monsterIndex) {

        if (monsterIndex != 999) {

            if(!invincible && !gp.monster[gp.currentMap][monsterIndex].dying) {
                gp.playSE(6);

                int damage = gp.monster[gp.currentMap][monsterIndex].attack - defense;

                if(damage < 0) {
                    damage = 0;
                }

                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i, int attack) {

        if (i != 999) {
            if(!gp.monster[gp.currentMap][i].invincible) {
                gp.playSE(5);

                int damage = attack - gp.monster[gp.currentMap][i].defense;

                if(damage < 0) {
                    damage = 0;
                }

                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if(gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name);
                    gp.ui.addMessage("EXP " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void damageInteractiveTile(int i) {
        if (i != 999 && gp.iTiles[gp.currentMap][i].destructible && gp.iTiles[gp.currentMap][i].isCorrectItem(this) && !gp.iTiles[gp.currentMap][i].invincible) {

            gp.iTiles[gp.currentMap][i].playSE();
            gp.iTiles[gp.currentMap][i].life--;
            gp.iTiles[gp.currentMap][i].invincible = true;

            //GENERATE PARTICLES
            generateParticle(gp.iTiles[gp.currentMap][i], gp.iTiles[gp.currentMap][i]);

            if(gp.iTiles[gp.currentMap][i].life == 0) {
                gp.iTiles[gp.currentMap][i] = gp.iTiles[gp.currentMap][i].getDestroyedForm();
            }
        }
    }

    public void checkLevelUp() {
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "Level Up! You are now level " + level + "!\n" + "You feel stronger!";
        }
    }

    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
                gp.ui.addMessage("Equipped " + selectedItem.name);
            }
            if(selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
                gp.ui.addMessage("Equipped " + selectedItem.name);
            }
            if(selectedItem.type == type_consumable) {

                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) {
        case "up":
            if(!attacking) {
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
            }
            if(attacking) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {
                    image = attackUp1;
                }
                if (spriteNum == 2) {
                    image = attackUp2;
                }
            }
            break;
        case "down":
            if(!attacking) {
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
            }
            if(attacking) {
                if (spriteNum == 1) {
                    image = attackDown1;
                }
                if (spriteNum == 2) {
                    image = attackDown2;
                }
            }
            break;
        case "left":
            if(!attacking) {
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
            }
            if(attacking) {
                tempScreenX = screenX - gp.tileSize;
                if (spriteNum == 1) {
                    image = attackLeft1;
                }
                if (spriteNum == 2) {
                    image = attackLeft2;
                }
            }
            break;
        case "right":
            if(!attacking) {
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
            }
            if(attacking) {
                if (spriteNum == 1) {
                    image = attackRight1;
                }
                if (spriteNum == 2) {
                    image = attackRight2;
                }
            }
            break;
        }

        if(invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


        //DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN, 10));
//        g2.setColor(Color.WHITE);
//        g2.drawString("Invincible Counter: " + invincibleCounter, 10, 400);
    }
}
