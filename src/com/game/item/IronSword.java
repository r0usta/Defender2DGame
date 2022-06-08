package com.game.item;

import com.game.main.UtilityTool;

import java.io.IOException;

public class IronSword extends Item {

    public IronSword() {
        this.name = "Iron Sword";
        this.description = "As a basic sword, it's good";
        this.attack = 1;
    }

    @Override
    public void loadImage() {
        try {
            this.image = setupImage(UtilityTool.loadImage("/assets/textures/entity/item/iron_sword.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
