package com.game.entity;

import com.game.item.IronSword;
import com.game.item.Item;
import com.game.item.WoodenShield;
import com.game.main.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Creature {
    /**
     *  Screen Position X and Y - the place where the player's position on the screen is drawn.
     */
    public final int screenPosX, screenPosY;
    public boolean attacking = false;

    public short level;
    /**
     * The more strength he has, the more damage he deals
     */
    public short strength;
    /**
     * The more dexterity he has, the less damage he receives
     */
    public short dexterity;
    /**
     * The total value of the attack is decided by strength and weapon
     */
    public short attack;
    /**
     * The total value of the defense is decided by dexterity and the shield
     */
    public short defense;
    public short key;
    public Item weapon;
    public Item shield;

    /**
     * Images of a player when he attacks
     */
    private final BufferedImage[] attackUp, attackDown, attackLeft, attackRight;
    /**
     * Used to simulate a sword collision with a creature
     */
    private final Rectangle attackArea;

    public Player() {
        this.type = TypeCreature.PLAYER;

        mapPosX = Game.tileSize * 11;
        mapPosY = Game.tileSize * 9;

        // Setting the player in the center of the screen
        screenPosX = (Game.screenWidth / 2) - (Game.tileSize / 2);
        screenPosY = (Game.screenHeight / 2) - (Game.tileSize / 2);

        attackUp = new BufferedImage[2];
        attackDown = new BufferedImage[2];
        attackLeft = new BufferedImage[2];
        attackRight = new BufferedImage[2];

        collisionArea.x = 3 * Game.scale;
        collisionArea.y = 8 * Game.scale;
        collisionArea.width = 10 * Game.scale;
        collisionArea.height = 8 * Game.scale;

        // Auxiliary variables that are used in collision calculations
        collisionAreaDefaultX = collisionArea.x;
        collisionAreaDefaultY = collisionArea.y;

        // Sword collision setup
        attackArea = new Rectangle(0, 0, 36, 36);

        this.loadPlayerAttackImage();
    }

    @Override
    public void setDefaults() {
        maxLife = 6;
        life = maxLife;
        speed = 4;

        level = 1;
        strength = 1;
        dexterity = 1;
        key = 0;

        weapon = new IronSword();
        shield = new WoodenShield();
        attack = getAttack();
        defense = getDefense();
    }

    /**
     * Called when the player is trying to pass the game again.
     */
    public void setDefaultPosition() {
        mapPosX = Game.tileSize * 11;
        mapPosY = Game.tileSize * 9;
        direction = Direction.DOWN;
        invincible = false;
    }

    public void restoreLife() {
        life = maxLife;
    }

    public short getAttack() {
        return (short) (strength * weapon.attack);
    }

    public short getDefense() {
        return (short) (dexterity * shield.defense);
    }

    @Override
    public void loadCreatureMovementImage() {
        String path = "/assets/textures/entity/creature/player/player.png";
        try {
            BufferedImage image = UtilityTool.loadImage(path);

            up[0] = setupImage(0, 0, image);
            up[1] = setupImage(0, 16, image);

            down[0] = setupImage(16, 0, image);
            down[1] = setupImage(16, 16, image);

            left[0] = setupImage(32, 0, image);
            left[1] = setupImage(32, 16, image);

            right[0] = setupImage(48, 0, image);
            right[1] = setupImage(48, 16, image);
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }

    public void loadPlayerAttackImage() {
        String path = "/assets/textures/entity/creature/player/attack/sword.png";
        try {
            BufferedImage image = UtilityTool.loadImage(path);

            attackUp[0] = UtilityTool.crop(image, 0, 0, 16, 32);
            attackUp[0] = UtilityTool.scaleImage(attackUp[0], Game.tileSize, Game.tileSize * 2);
            attackUp[1] = UtilityTool.crop(image, 16, 0, 16, 32);
            attackUp[1] = UtilityTool.scaleImage(attackUp[1], Game.tileSize, Game.tileSize * 2);

            attackDown[0] = UtilityTool.crop(image, 32, 0, 16, 32);
            attackDown[0] = UtilityTool.scaleImage(attackDown[0], Game.tileSize, Game.tileSize * 2);
            attackDown[1] = UtilityTool.crop(image, 48, 0, 16, 32);
            attackDown[1] = UtilityTool.scaleImage(attackDown[1], Game.tileSize, Game.tileSize * 2);

            attackLeft[0] = UtilityTool.crop(image, 64, 0, 32, 16);
            attackLeft[0] = UtilityTool.scaleImage(attackLeft[0], Game.tileSize * 2, Game.tileSize);
            attackLeft[1] = UtilityTool.crop(image, 64, 16, 32, 16);
            attackLeft[1] = UtilityTool.scaleImage(attackLeft[1], Game.tileSize * 2, Game.tileSize);

            attackRight[0] = UtilityTool.crop(image, 96, 0, 32, 16);
            attackRight[0] = UtilityTool.scaleImage(attackRight[0], Game.tileSize * 2, Game.tileSize);
            attackRight[1] = UtilityTool.crop(image, 96, 16, 32, 16);
            attackRight[1] = UtilityTool.scaleImage(attackRight[1], Game.tileSize * 2, Game.tileSize);
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }

    public void attacking() {
        // Depending on the animation, different actions are performed
        spriteCounter++;
        if(spriteCounter <= 5) {
            spriteNum = 0;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 1;

            /*
            First, we save the current positions and the player's collision
            area into these temporary variables. Because when we attack,
            we need to check the collision based on where the weapon is,
            not the player himself. So we temporarily change his parameters during this check.
             */
            int currentMapPosX = mapPosX;
            int currentMapPosY = mapPosY;
            int collisionAreaWidth = collisionArea.width;
            int collisionAreaHeight = collisionArea.height;

            // We "move the player" in the direction of the attack to simulate a sword collision.
            switch (direction) {
                case UP -> mapPosY -= attackArea.height;
                case DOWN -> mapPosY += attackArea.height;
                case LEFT -> mapPosX -= attackArea.width;
                case RIGHT -> mapPosX += attackArea.width;
            }

            // Change the collision size of the player to the collision size of the weapon
            collisionArea.width = attackArea.width;
            collisionArea.height = attackArea.height;

            // Check mob collision with the updated mapPosX, mapPosY and collisionArea
            int mobIndex = CollisionManager.checkCollisionWithCreature(this, Game.mobs);
            this.damageMob(mobIndex);

            // After checking collision, restore the original data
            mapPosX = currentMapPosX;
            mapPosY = currentMapPosY;
            collisionArea.width = collisionAreaWidth;
            collisionArea.height = collisionAreaHeight;
        }
        if(spriteCounter > 25) {
            spriteNum = 0;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpItem(int index) {
        if(index != -1) {
            Game.items[index] = null;
            Game.playSoundEffect(3);
            key++;
        }
    }

    public void interactionWithNPC(int index) {
        if(index != -1) {
            if(Game.keyManager.interactionPressed) {
                Game.npcs[index].speak();
            }
        }
    }

    /**
     * Method responsible for performing functions when the player encounters a mob.
     * @param index mob index
     */
    public void collidedWithMob(int index) {
        if(index != -1) {
            if(!this.invincible && !Game.mobs[index].dying) {
                life -= 2;
                invincible = true;
                Game.playSoundEffect(4);
            }
        }
    }

    public void damageMob(int index) {
        if(index != -1) {
            if(!Game.mobs[index].invincible) {
                Game.mobs[index].life -= 1;
                Game.mobs[index].invincible = true;
                Game.mobs[index].damageReaction();
                Game.playSoundEffect(2);

                if(Game.mobs[index].life <= 0) {
                    Game.mobs[index].dying = true;
                }
            }
        }
    }

    @Override
    public void update() {
        if(attacking) {
            attacking();
        }else if(Game.keyManager.upPressed || Game.keyManager.downPressed ||
                Game.keyManager.leftPressed || Game.keyManager.rightPressed || Game.keyManager.interactionPressed) {

            if(Game.keyManager.upPressed) direction = Direction.UP;
            else if(Game.keyManager.downPressed) direction = Direction.DOWN;
            else if(Game.keyManager.leftPressed) direction = Direction.LEFT;
            else if(Game.keyManager.rightPressed) direction = Direction.RIGHT;

            collided = false;

            CollisionManager.checkCollisionWithTile(this);

            int itemIndex = CollisionManager.checkCollisionWithItem(this);
            this.pickUpItem(itemIndex);

            int npcIndex = CollisionManager.checkCollisionWithCreature(this, Game.npcs);
            this.interactionWithNPC(npcIndex);

            int mobIndex = CollisionManager.checkCollisionWithCreature(this, Game.mobs);
            this.collidedWithMob(mobIndex);

            Game.eventManager.eventCheck();

            // If collided is false, player can move
            if(!collided && !Game.keyManager.interactionPressed) {
                switch (direction) {
                    case UP -> mapPosY -= speed;
                    case DOWN -> mapPosY += speed;
                    case LEFT -> mapPosX -= speed;
                    case RIGHT -> mapPosX += speed;
                }
            }
        }

        if(!attacking) {
            spriteCounter++;
            if(spriteCounter > 15) {
                if(spriteNum == 0) {
                    spriteNum = 1;
                }else if(spriteNum == 1) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
            }
        }

        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(life <= 0) {
            Game.state = State.GAMEOVER;
            Game.guiManager.commandNum = 0;
            Game.playSoundEffect(1);
            Game.stopMusic();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // To adjust the drawing of the image when the player attacks
        int tempScreenPosX = screenPosX;
        int tempScreenPosY = screenPosY;

        //region Sprite Changer
        switch (direction) {
            case UP -> {
                if(!attacking) {
                    if(spriteNum == 0) image = up[0];
                    if(spriteNum == 1) image = up[1];
                }
                if(attacking) {
                    tempScreenPosY = screenPosY - Game.tileSize;
                    if(spriteNum == 0) image = attackUp[0];
                    if(spriteNum == 1) image = attackUp[1];
                }
            }
            case DOWN -> {
                if(!attacking) {
                    if(spriteNum == 0) image = down[0];
                    if(spriteNum == 1) image = down[1];
                }
                if(attacking) {
                    if(spriteNum == 0) image = attackDown[0];
                    if(spriteNum == 1) image = attackDown[1];
                }
            }
            case LEFT -> {
                if(!attacking) {
                    if(spriteNum == 0) image = left[0];
                    if(spriteNum == 1) image = left[1];
                }
                if(attacking) {
                    tempScreenPosX = screenPosX - Game.tileSize;
                    if(spriteNum == 0) image = attackLeft[0];
                    if(spriteNum == 1) image = attackLeft[1];
                }

            }
            case RIGHT -> {
                if(!attacking) {
                    if(spriteNum == 0) image = right[0];
                    if(spriteNum == 1) image = right[1];
                }
                if(attacking) {
                    if(spriteNum == 0) image = attackRight[0];
                    if(spriteNum == 1) image = attackRight[1];
                }
            }
        }
        //endregion

        if(screenPosX > mapPosX) {
            tempScreenPosX = mapPosX - (screenPosX - tempScreenPosX);
        }
        if(screenPosY > mapPosY) {
            tempScreenPosY = mapPosY - (screenPosY - tempScreenPosY);
        }

        int rightOffset = Game.screenWidth - screenPosX;
        if(rightOffset > Game.mapWidth - mapPosX) {
            tempScreenPosX = (Game.screenWidth - (Game.mapWidth - mapPosX)) - (screenPosX - tempScreenPosX);
        }
        int bottomOffset = Game.screenHeight - screenPosY;
        if(bottomOffset > Game.mapHeight - mapPosY) {
            tempScreenPosY = (Game.screenHeight - (Game.mapHeight - mapPosY)) - (screenPosY - tempScreenPosY);
        }

        if(invincible) {
            this.changeAlpha(g2, 0.4f);
        }

        g2.drawImage(image, tempScreenPosX, tempScreenPosY, null);

        this.changeAlpha(g2, 1f);

        if(Game.keyManager.debugPressed) {
            g2.setColor(Color.red);
            if(!attacking) {
                g2.drawRect(tempScreenPosX + collisionArea.x, tempScreenPosY + collisionArea.y, collisionArea.width, collisionArea.height);
            }
        }
    }
}
