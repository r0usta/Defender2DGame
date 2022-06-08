package com.game.gui;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Responsible for drawing character status
 */
public class CharacterStatus implements GUI {
    private final short imgRow;
    private final short imgCol;

    private BufferedImage topLeft, top, topRight;
    private BufferedImage left, middle, right;
    private BufferedImage bottomLeft, bottom, bottomRight;

    public CharacterStatus() {
        imgRow = 8;
        imgCol = 3;

        try {
            String path = "/assets/gui/dialogue.png";
            BufferedImage image = UtilityTool.loadImage(path);

            this.topLeft = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 0, 0, 10, 7));
            this.top = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 11, 0, 57, 7));
            this.topRight = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 69, 0, 9, 7));

            this.left = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 0, 8, 10, 23));
            this.middle = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 11, 8, 57, 23));
            this.right = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 69, 8, 9, 23));

            this.bottomLeft = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 0, 32, 10, 7));
            this.bottom = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 11, 32, 57, 7));
            this.bottomRight = UtilityTool.scaleGUIImage(UtilityTool.crop(image, 69, 32, 9, 7));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int x = Game.tileSize * 2;
        int y = Game.tileSize;
        int frameWidth = 0;

        int imgX = x;
        int imgY = y;

        g2.drawImage(topLeft, imgX, imgY, null);
        imgX += topLeft.getWidth();

        for(int col = 0; col < imgCol - 2; col++) {
            g2.drawImage(top, imgX, imgY, null);
            frameWidth = imgX += top.getWidth();
        }

        g2.drawImage(topRight, imgX, imgY, null);
        imgX = x;
        imgY += topLeft.getHeight();

        for(int row = 0; row < imgRow - 2; row++) {
            g2.drawImage(left, imgX, imgY, null);
            imgX += left.getWidth();
            for(int col = 0; col < imgCol - 2; col++) {
                g2.drawImage(middle, imgX, imgY, null);
                imgX += middle.getWidth();
            }
            g2.drawImage(right, imgX, imgY, null);
            imgX = x;
            imgY += left.getHeight();
        }

        g2.drawImage(bottomLeft, imgX, imgY, null);
        imgX += bottomLeft.getWidth();

        for(int col = 0; col < imgCol - 2; col++) {
            g2.drawImage(bottom, imgX, imgY, null);
            imgX += bottom.getWidth();
        }

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(30f));
        x += Game.tileSize - 15;
        y += Game.tileSize + 5;

        int textX = x;
        int textY = y;
        final int lineHeight = 35;

        g2.drawImage(bottomRight, imgX, imgY, null);g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Key", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);

        int tailX = (x + frameWidth) - Game.tileSize * 3 + 15;
        textY = y;
        String value;

        value = String.valueOf(Game.player.level);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = Game.player.life + "/" + Game.player.maxLife;
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(Game.player.strength);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(Game.player.dexterity);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(Game.player.attack);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(Game.player.defense);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(Game.player.key);
        textX = UtilityTool.getXForAlignToRightText(value, tailX, g2);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(Game.player.weapon.image, tailX - Game.tileSize, textY - 24, null);
        textY += Game.tileSize;
        g2.drawImage(Game.player.shield.image, tailX - Game.tileSize, textY- 24, null);
    }
}
