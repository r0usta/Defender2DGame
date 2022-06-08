package com.game.gui;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Responsible for drawing hearts on the screen
 */
public class HUD implements GUI {
    BufferedImage heartFull, heartHalf, heartBlank;

    public HUD() {
        try {
            String path = "/assets/gui/health_bar.png";
            BufferedImage image = UtilityTool.loadImage(path);

            this.heartFull = UtilityTool.scaleImage(UtilityTool.crop(image, 0, 0), Game.tileSize, Game.tileSize);
            this.heartHalf = UtilityTool.scaleImage(UtilityTool.crop(image, 16, 0), Game.tileSize, Game.tileSize);
            this.heartBlank = UtilityTool.scaleImage(UtilityTool.crop(image, 32, 0), Game.tileSize, Game.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int x = Game.tileSize / 2;
        int y = Game.tileSize / 2;

        int imgX = x;

        for(int ml = 0; ml < Game.player.maxLife / 2; ml++) {
            g2.drawImage(heartBlank, imgX, y, null);
            imgX += Game.tileSize;
        }

        imgX = x;

        for(int l = 0; l < Game.player.life; l++) {
            g2.drawImage(heartHalf, imgX, y, null);
            l++;

            if(l < Game.player.life) {
                g2.drawImage(heartFull, imgX, y, null);
            }

            imgX += Game.tileSize;
        }
    }
}
