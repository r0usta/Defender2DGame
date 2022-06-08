package com.game.item;

import com.game.main.UtilityTool;

import java.io.IOException;

public class WoodenShield extends Item{

    public WoodenShield() {
        this.name = "Wooden Shield";
        this.description = "As a basic shield, it's good";
        this.defense = 1;
    }

    @Override
    public void loadImage() {
        try {
            this.image = setupImage(UtilityTool.loadImage("/assets/textures/entity/item/wooden_shield.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
