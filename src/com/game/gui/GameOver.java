package com.game.gui;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;

/**
 * Responsible for drawing the game over screen
 */
public class GameOver implements GUI{

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, Game.screenWidth, Game.screenHeight);

        int x, y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";

        // Shadow
        g2.setColor(Color.black);
        x = UtilityTool.getXForCenteredText(g2, text);
        y = Game.tileSize * 4;
        g2.drawString(text, x, y);

        // Main Text
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = UtilityTool.getXForCenteredText(g2, text);
        y += Game.tileSize*4;
        g2.drawString(text, x, y);
        if(Game.guiManager.commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        text = "Quit";
        x = UtilityTool.getXForCenteredText(g2, text);
        y += 55;
        g2.drawString(text, x, y);
        if(Game.guiManager.commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }
}
