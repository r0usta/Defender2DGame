package com.game.gui;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Responsible for drawing the dialog window on the screen
 */
public class DialogueWindow implements GUI {
    private final short imgRow;
    private final short imgCol;

    public static String dialogue;

    private BufferedImage topLeft, top, topRight;
    private BufferedImage left, middle, right;
    private BufferedImage bottomLeft, bottom, bottomRight;

    public DialogueWindow() {
        imgRow = 4;
        imgCol = 6;

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
        int x = Game.tileSize * 2 + 12;
        int y = Game.tileSize / 2;

        int imgX = x;
        int imgY = y;

        g2.drawImage(topLeft, imgX, imgY, null);
        imgX += topLeft.getWidth();

        for(int col = 0; col < imgCol - 2; col++) {
            g2.drawImage(top, imgX, imgY, null);
            imgX += top.getWidth();
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

        g2.drawImage(bottomRight, imgX, imgY, null);

        x += Game.tileSize - 15;
        y += Game.tileSize + 5;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));

        for(String line : dialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
}
