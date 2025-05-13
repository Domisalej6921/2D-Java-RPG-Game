package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import objects.*;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean attackCancel = false;
    public boolean lightUpdated = false;

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
    }

    public void setDefaultValues() {
        
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        defaultSpeed = 4;
        speed = defaultSpeed;
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
        currentLight = null;
        projectile = new OBJ_Fireball(gp);
        attack = getAttack();
        defense = getDefense();

        getImage();
        getAttackImage();
        setItems();
        getGuardImage();
        setDefaultPositions();
    }

    public void setDefaultPositions() {

        gp.currentMap = 2;
        worldX = gp.tileSize * 9;
        worldY = gp.tileSize * 9;
        direction = "down";
    }

    public void restoreStatus() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
        speed = defaultSpeed;
    }

    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }

    //Total attack is decided by strength and weapon attack value
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }

    //Total defense is decided by dexterity and shield defense value
    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentWeapon) {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }

    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentShield) {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }

    public void getImage() {

        up1 = setup("player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("player/boy_right_2", gp.tileSize, gp.tileSize);

    }

    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }

    public void getAttackImage() {

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
        if(currentWeapon.type == type_pickaxe) {

            attackUp1 = setup("player/boy_pick_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("player/boy_pick_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("player/boy_pick_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("player/boy_pick_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("player/boy_pick_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("player/boy_pick_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("player/boy_pick_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("player/boy_pick_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }

    public void getGuardImage() {
        guardUp = setup("player/boy_guard_up", gp.tileSize, gp.tileSize);
        guardDown = setup("player/boy_guard_down", gp.tileSize, gp.tileSize);
        guardLeft = setup("player/boy_guard_left", gp.tileSize, gp.tileSize);
        guardRight = setup("player/boy_guard_right", gp.tileSize, gp.tileSize);
    }

    public void update() {

        if(knockBack) {

            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTiles);

            if(collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else if(!collisionOn) {
                switch (knockBackDirection) {
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

            knockBackCounter++;
            if(knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }

        else if(attacking){
            attacking();
        }
        else if(keyH.spacePressed) {
            guarding = true;
            guardCounter++;
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
            guarding = false;
            guardCounter = 0;

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
            guarding = false;
            guardCounter = 0;
        }

        if(gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)) {

            // SET DEFAULT COORDS, DIRECTION AND USER
            projectile.set(worldX, worldY, direction, true, this);

            //SUBTRACT PROJECTILE COST (MANA, AMMO, ETC)
            projectile.subtractResource(this);

            // Check Vacancy
            for(int i = 0; i < gp.projectile[1].length; i++) {
                if(gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;

            gp.playSE(10);
        }
        // This needs to be placed outside of key if statement!
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                transparent = false;
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
        if(!keyH.godModeOn) {
            if (life <= 0) {
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum = -1;
                gp.stopMusic();
                gp.playSE(12);
            }
        }
    }

    public void pickUpObject(int i) {

        if (i != 999) {

            //PICK-UP ONLY ITEMS
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            //OBSTACLE
            else if(gp.obj[gp.currentMap][i].type == type_obstacle) {
                if(keyH.enterPressed) {
                    attackCancel = true;
                    gp.obj[gp.currentMap][i].interact();
                }
            } else {

                //INVENTORY ITEMS
                String text;

                if (canObtainItem(gp.obj[gp.currentMap][i])) {
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

        if (i != 999) {
            if (gp.keyH.enterPressed) {
                attackCancel = true;
                gp.npc[gp.currentMap][i].speak();
            }

            gp.npc[gp.currentMap][i].move(direction);
        }
    }

    public void contactMonster(int monsterIndex) {

        if (monsterIndex != 999) {

            if(!invincible && !gp.monster[gp.currentMap][monsterIndex].dying) {
                gp.playSE(6);

                int damage = gp.monster[gp.currentMap][monsterIndex].attack - defense;

                if(damage < 1) {
                    damage = 1;
                }

                life -= damage;
                invincible = true;
                transparent = true;
            }
        }
    }

    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {

        if (i != 999) {
            if(!gp.monster[gp.currentMap][i].invincible) {
                gp.playSE(5);

                if(knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }

                if(gp.monster[gp.currentMap][i].offBalance) {
                    attack *= 5;
                }

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

    public void damageProjectile(int i ) {

        if(i != 999) {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
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
            dialogues[0][0] = "Level Up! You are now level " + level + "!\n" + "You feel stronger!";
            startDialogue(this, 0);
        }
    }

    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe || selectedItem.type == type_pickaxe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
                gp.ui.addMessage("Equipped " + selectedItem.name);
            }
            if(selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
                gp.ui.addMessage("Equipped " + selectedItem.name);
            }
            if(selectedItem.type == type_light) {

                if(currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            if(selectedItem.type == type_consumable) {

                if (selectedItem.use(this)) {
                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    } else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }

    public int searchItemInInventory(String itemName) {

        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item) {

        boolean canObtain = false;

        Entity newItem = gp.eGenerator.getObject(item.name);

        //CHECK IF STACKABLE
        if(newItem.stackable) {
            int index = searchItemInInventory(newItem.name);

            if(index != 999) {
                inventory.get(index).amount++;
                canObtain = true;

            } else { //New Item
                if(inventory.size() < maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        } else { //NOT STACKABLE
            if(inventory.size() < maxInventorySize) {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return canObtain;
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
            if(guarding) {
                image = guardUp;
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
            if(guarding) {
                image = guardDown;
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
            if(guarding) {
                image = guardLeft;
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
            if(guarding) {
                image = guardRight;
            }
            break;
        }

        if(transparent) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        if(drawing) {
            g2.drawImage(image, tempScreenX, tempScreenY, null);
        }

        //RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
