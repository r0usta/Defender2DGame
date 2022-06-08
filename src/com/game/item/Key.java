package com.game.item;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.io.IOException;

public class Key extends Item {

    public Key(int mapPosX, int mapPosY) {
        this.mapPosX = Game.tileSize * mapPosX;
        this.mapPosY = Game.tileSize * mapPosY;

        this.name = "Key";
        this.description = "Allows to open the door";
    }

    @Override
    public void loadImage() {
        try {
            this.image = setupImage(UtilityTool.loadImage("/assets/textures/entity/item/key.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}