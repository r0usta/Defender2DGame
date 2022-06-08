package com.game.event;

import com.game.gui.DialogueWindow;
import com.game.main.Game;
import com.game.main.State;

/**
 * Stores events that are called in the EventManager class
 * @see EventManager
 */
public class Event {
    public static void damagePit() {
        Game.state = State.DIALOGUE;
        DialogueWindow.dialogue = "You fall into a pit!";
        Game.player.life -= 1;
        Game.eventManager.canTouchEvent = false;
        Game.playSoundEffect(4);
    }

    public static void healingPlace() {
        if(Game.keyManager.interactionPressed) {
            Game.state = State.DIALOGUE;
            DialogueWindow.dialogue = "You drink the water. Your health is somewhat restored";
            Game.player.life += 1;
            if(Game.player.life > Game.player.maxLife) {
                Game.player.life = Game.player.maxLife;
            }
        }
    }

    public static void woodenPlate() {
        if(Game.keyManager.interactionPressed) {
            Game.state = State.DIALOGUE;
            DialogueWindow.dialogue = "Defend the old man from angry mobs!";
        }
    }
}
