package com.game.npc;

import com.game.entity.Creature;
import com.game.entity.Direction;
import com.game.entity.TypeCreature;
import com.game.gui.DialogueWindow;
import com.game.main.Game;
import com.game.main.State;

public abstract class NPC extends Creature {
    protected short aiCounter;
    /**
     * Stores various text (dialogs) that will be
     * displayed on the screen when the player interacts with an NPC
     */
    protected String[] dialogues;
    protected int dialogueIndex;

    public NPC(int mapPosX, int mapPosY) {
        this.mapPosX = Game.tileSize * mapPosX;
        this.mapPosY = Game.tileSize * mapPosY;

        this.type = TypeCreature.NPC;
        dialogues = new String[5];
        this.setDialogues();
    }

    @Override
    public abstract void setDefaults();

    public abstract void setDialogues();

    @Override
    public abstract void loadCreatureMovementImage();

    /**
     * Setting up the behavior (The Simplest AI).
     * An abstract method that is rewritten in other subclasses
     */
    public abstract void AI();

    /**
     * This method defines a value for the "dialogue" variable in the GUI class.
     * @see DialogueWindow
     */
    public void speak() {
        if(dialogueIndex == dialogues.length || dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        Game.state = State.DIALOGUE;
        DialogueWindow.dialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        // Add a turn of NPC to us when player talk to him
        switch (Game.player.direction) {
            case UP -> direction = Direction.DOWN;
            case DOWN -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    @Override
    public void update() {
        super.update();
        this.AI();
    }
}