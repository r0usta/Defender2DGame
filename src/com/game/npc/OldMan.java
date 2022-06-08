package com.game.npc;

import com.game.entity.Direction;
import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class OldMan extends NPC {
    public OldMan(int mapPosX, int mapPosY) {
        super(mapPosX, mapPosY);
        collisionArea.x = 2 * Game.scale;
        collisionArea.y = 8 * Game.scale;
        collisionArea.width = 12 * Game.scale;
        collisionArea.height = 8 * Game.scale;

        collisionAreaDefaultX = collisionArea.x;
        collisionAreaDefaultY = collisionArea.y;
    }

    @Override
    public void setDefaults() {
        speed = 1;
    }

    @Override
    public void loadCreatureMovementImage() {
        String path = "/assets/textures/entity/creature/npc/old_man.png";
        try {
            BufferedImage image = UtilityTool.loadImage(path);

            up[0] = setupImage(0, 0, image);
            up[1] = setupImage(0, 16, image);

            down[0] = setupImage(16, 0, image);
            down[1] = setupImage(16, 16, image);

            left[0] = setupImage(32, 0, image);
            left[1] = setupImage(32, 16, image);

            right[0] = setupImage(48, 0, image);
            right[1] = setupImage(48, 16, image);
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }

    @Override
    public void setDialogues() {
        dialogues[0] = "Isn't it time for me to retire?";
        dialogues[1] = "Would you come to my funeral?";
        dialogues[2] = "I’d like to offer you moral support, but I have questionable morals.";
        dialogues[3] = """
                Not trying to brag or anything, but I can wake up without an\s
                alarm clock now simply due to my crippling and\s
                overwhelming anxiety, so...""";
        dialogues[4] = "I’ve come to a point in my life where I need a stronger word \nthan fuck.";
    }

    @Override
    public void AI() {
        aiCounter++;

        if(aiCounter == 120) {
            Random rnd = new Random();
            int i = rnd.nextInt(100)+1;

            if(i <= 25) {
                direction = Direction.UP;
            }
            if(i > 25 && i <= 50) {
                direction = Direction.DOWN;
            }
            if(i > 50 && i <= 75) {
                direction = Direction.LEFT;
            }
            if(i > 75) {
                direction = Direction.RIGHT;
            }

            aiCounter = 0;
        }
    }
}
