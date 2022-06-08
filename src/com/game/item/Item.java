package com.game.item;

import com.game.entity.Entity;
import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item extends Entity {
    public String name;
    public String description;
    public BufferedImage image;
    public short attack;
    public short defense;

    public Item() {
        this.loadImage();
    }

    public BufferedImage setupImage(BufferedImage image) {
        return UtilityTool.scaleImage(image, Game.tileSize, Game.tileSize);
    }

    public abstract void loadImage();

    @Override
    public void update() {
        // Item don't have anything to update
    }

    @Override
    public void draw(Graphics2D g2) {
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
    }
}
