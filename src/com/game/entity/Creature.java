package com.game.entity;

import com.game.main.CollisionManager;
import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Creature extends Entity{
    /**
     * Image data (I use this to store creature image files).
     * The name of the variables indicates the type of image
     * (where the creature turns:  up, down, left, right).
     */
    protected BufferedImage[] up, down, left, right;

    public short speed;
    /**
     * When a creature moves, I need to know where to go in order to draw
     * a sprite for a specific direction
     */
    public Direction direction;

    /**
     * Used to create sprite animations
     */
    public short spriteCounter = 0;
    protected int spriteNum = 0;

    public int maxLife;
    public int life;
    /**
     * Delay so that the player cannot take damage again when touching the mob
     */
    public boolean invincible;
    public short invincibleCounter;

    public TypeCreature type;

    public Creature() {
        up = new BufferedImage[2];
        down = new BufferedImage[2];
        left = new BufferedImage[2];
        right = new BufferedImage[2];

        // The direction of the creature at the start of the game
        direction = Direction.DOWN;
        this.setDefaults();
        this.loadCreatureMovementImage();
    }

    /**
     * The values that are set when the game is started
     */
    public abstract void setDefaults();

    public abstract void loadCreatureMovementImage();

    /**
     * The method contains two methods from the UtilityTool class,
     * which allows you to crop the image and enlarge it at once
     */
    public BufferedImage setupImage(int cropX, int cropY, BufferedImage image) {
        image = UtilityTool.crop(image, cropX, cropY);
        image = UtilityTool.scaleImage(image, Game.tileSize, Game.tileSize);
        return image;
    }

    @Override
    public void update() {
        this.collided = false;
        CollisionManager.checkCollisionWithTile(this);
        boolean collidedWithPlayer = CollisionManager.checkCollisionWithPlayer(this);
        CollisionManager.checkCollisionWithCreature(this, Game.npcs);
        CollisionManager.checkCollisionWithCreature(this, Game.mobs);

        if(this.type == TypeCreature.MOB && collidedWithPlayer) {
            if(!Game.player.invincible) {
                Game.player.life -= 2;
                Game.player.invincible = true;
                Game.playSoundEffect(4);
            }
        }

        if(!collided) {
            switch (direction) {
                case UP -> mapPosY -= speed;
                case DOWN -> mapPosY += speed;
                case LEFT -> mapPosX -= speed;
                case RIGHT -> mapPosX += speed;
            }
        }

        spriteCounter++;
        if(spriteCounter > 15) {
            if(spriteNum == 0) {
                spriteNum = 1;
            }else {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }

        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case UP -> {
                if(spriteNum == 0) image = up[0];
                if(spriteNum == 1) image = up[1];
            }
            case DOWN -> {
                if(spriteNum == 0) image = down[0];
                if(spriteNum == 1) image = down[1];
            }
            case LEFT -> {
                if(spriteNum == 0) image = left[0];
                if(spriteNum == 1) image = left[1];
            }
            case RIGHT -> {
                if(spriteNum == 0) image = right[0];
                if(spriteNum == 1) image = right[1];
            }
        }

        // The principle is exactly the same as in the TileManager class
        int screenPosX = this.mapPosX - Game.player.mapPosX + Game.player.screenPosX;
        int screenPosY = this.mapPosY - Game.player.mapPosY + Game.player.screenPosY;

        if(Game.player.screenPosX > Game.player.mapPosX) {
            screenPosX = mapPosX;
        }

        if(Game.player.screenPosY > Game.player.mapPosY) {
            screenPosY = mapPosY;
        }

        int rightOffset = Game.screenWidth - Game.player.screenPosX;
        if(rightOffset > Game.mapWidth - Game.player.mapPosX) {
            screenPosX = Game.screenWidth - (Game.mapWidth - mapPosX);
        }

        int bottomOffset = Game.screenHeight - Game.player.screenPosY;
        if(bottomOffset > Game.mapHeight - Game.player.mapPosY) {
            screenPosY = Game.screenHeight - (Game.mapHeight - mapPosY);
        }

        if(this.invincible) {
            this.changeAlpha(g2, 0.4f);
        }

        if(this.mapPosX + Game.tileSize > Game.player.mapPosX - Game.player.screenPosX &&
                this.mapPosX - Game.tileSize < Game.player.mapPosX + Game.player.screenPosX &&
                this.mapPosY + Game.tileSize > Game.player.mapPosY - Game.player.screenPosY &&
                this.mapPosY - Game.tileSize < Game.player.mapPosY + Game.player.screenPosY) {

            g2.drawImage(image, screenPosX, screenPosY, Game.tileSize, Game.tileSize, null);
        }else if(Game.player.screenPosX > Game.player.mapPosX ||
                Game.player.screenPosY > Game.player.mapPosY ||
                rightOffset > Game.mapWidth - Game.player.mapPosX ||
                bottomOffset > Game.mapHeight - Game.player.mapPosY) {
            g2.drawImage(image, screenPosX, screenPosY, null);
        }

        this.changeAlpha(g2, 1f);

        if(Game.keyManager.debugPressed) {
            g2.setColor(Color.red);
            g2.drawRect(screenPosX + collisionArea.x, screenPosY + collisionArea.y, collisionArea.width, collisionArea.height);
        }
    }
}
