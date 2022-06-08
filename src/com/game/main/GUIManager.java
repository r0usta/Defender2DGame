package com.game.main;

import com.game.gui.*;

import java.awt.*;
import java.util.Locale;

/**
 * This class basically handles the entire screen user interface.
 * So we can display text messages and item icons first, etc.
 */
public class GUIManager implements GUI {
    /**
     * Imitates input. Rewritten in the KeyManager
     * class when we press keys. Used only when
     * the player dies "Game.state.GAMEOVER"
     */
    public int commandNum;

    private static GUI dialogueWindow;
    private static GUI hud;
    private static GUI characterStatus;
    private static GUI gameOver;

    public GUIManager() {
        dialogueWindow = new DialogueWindow();
        hud = new HUD();
        characterStatus = new CharacterStatus();
        gameOver = new GameOver();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if(Game.state == State.PLAY) {
            hud.draw(g2);
        }else if(Game.state == State.PAUSE) {
            String text = "Pause".toUpperCase(Locale.ROOT);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
            int x = UtilityTool.getXForCenteredText(g2, text);
            int y = Game.screenHeight / 2;

            g2.drawString(text, x, y);
        }else if(Game.state == State.DIALOGUE) {
            dialogueWindow.draw(g2);
        }else if(Game.state == State.CHARACTER) {
            characterStatus.draw(g2);
        }else if(Game.state == State.GAMEOVER) {
            gameOver.draw(g2);
        }
    }
}
