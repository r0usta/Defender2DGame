package com.game.mob;

import com.game.entity.Creature;
import com.game.entity.TypeCreature;
import com.game.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Mob extends Creature {
    protected short aiCounter;

    public boolean alive;
    /**
     * Allows knowing whether the animation of the death of a mob is currently displayed
     */
    public boolean dying;
    private short dyingCounter;

    private boolean hpBarOn;
    private short hpBarCounter;

    public Mob(int mapPosX, int mapPosY) {
        this.mapPosX = Game.tileSize * mapPosX;
        this.mapPosY = Game.tileSize * mapPosY;

        this.type = TypeCreature.MOB;
        alive = true;
        dying = false;
    }

    /**
     * Setting up the behavior (The Simplest AI).
     * An abstract method that is rewritten in other subclasses
     */
    public abstract void AI();

    /**
     * How the entity will behave when taking damage
     * Called in the Player class in the damageMob() method
     */
    public abstract void damageReaction();

    public void dyingAnimation(Graphics2D g2) {
        this.dyingCounter++;

        int i = 5;
        if(this.dyingCounter <= i) changeAlpha(g2, 0f);
        if(this.dyingCounter > i && this.dyingCounter <= i*2) changeAlpha(g2, 1f);
        if(this.dyingCounter > i*2 && this.dyingCounter <= i*3) changeAlpha(g2, 0f);
        if(this.dyingCounter > i*3 && this.dyingCounter <= i*4) changeAlpha(g2, 1f);
        if(this.dyingCounter > i*4 && this.dyingCounter <= i*5) changeAlpha(g2, 0f);
        if(this.dyingCounter > i*5 && this.dyingCounter <= i*6) changeAlpha(g2, 1f);
        if(this.dyingCounter > i*6 && this.dyingCounter <= i*7) changeAlpha(g2, 0f);
        if(this.dyingCounter > i*7 && this.dyingCounter <= i*8) changeAlpha(g2, 1f);
        if(this.dyingCounter > i*8) this.alive = false;
    }

    @Override
    public void update() {
        super.update();
        this.AI();
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

        // Mob HP bar
        if(hpBarOn) {

            /*
                Divide the length of the band by the maximum health of the mob
                (so we know the length of 1 HP). For example, if the max health of
                the mob is 2 - this would be 48/2, so one scale is 24 pixels,
                and if the max health of the mob is 4 - one scale is 12 pixels.
            */

            double oneScale = (double)Game.tileSize/maxLife;

            // Based on this oneScale, we can find out the current bar length
            double hpBarValue = oneScale * life;

            g2.setColor(Color.darkGray);
            g2.fillRect(screenPosX-1, screenPosY-16, Game.tileSize+2, 12);

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenPosX, screenPosY - 15, (int) hpBarValue, 10);

            hpBarCounter++;
            // After 10 seconds the bar will disappear
            if(hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }

        if(this.invincible) {
            // Make the mob transparent until it is invincible
            this.changeAlpha(g2, 0.4f);
            // The player attacked the mob, so the health bar will begin to appear
            hpBarOn = true;
            // Reset the health bar counter, because otherwise it will disappear 10 seconds after the hit
            hpBarCounter = 0;
        }

        // Check if after player last hit, the monster died. Create its death (dying) animation
        if(this.dying) {
            this.dyingAnimation(g2);
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
