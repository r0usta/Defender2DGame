package com.game.mob;

import com.game.entity.Direction;
import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GreenSlime extends Mob{

    public GreenSlime(int mapPosX, int mapPosY) {
        super(mapPosX, mapPosY);
        collisionArea.x = Game.scale;
        collisionArea.y = 4 * Game.scale;
        collisionArea.width = 14 * Game.scale;
        collisionArea.height = 12 * Game.scale;

        collisionAreaDefaultX = collisionArea.x;
        collisionAreaDefaultY = collisionArea.y;
    }

    @Override
    public void setDefaults() {
        speed = 1;
        maxLife = 4;
        life = maxLife;
    }

    @Override
    public void loadCreatureMovementImage() {
        String path = "/assets/textures/entity/creature/mob/green_slime.png";
        try {
            BufferedImage image = UtilityTool.loadImage(path);

            up[0] = setupImage(0, 0, image);
            up[1] = setupImage(0, 16, image);

            down[0] = up[0];
            down[1] = up[1];

            left[0] = up[0];
            left[1] = up[1];

            right[0] = up[0];
            right[1] = up[1];
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
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

    @Override
    public void damageReaction() {
        aiCounter = 0;
        direction = Game.player.direction;
    }
}
