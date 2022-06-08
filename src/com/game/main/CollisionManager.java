package com.game.main;

import com.game.entity.Creature;
import com.game.entity.Player;

public class CollisionManager {

    /**
     * The method will be used not only for the player, but also for NPCs or mobs.
     * So the argument must be 'creature'.
     */
    public static void checkCollisionWithTile(Creature creature) {
        // It is necessary to know the position of the collision area of the creature, on the map
        int creatureLeftAreaMapPosX = creature.mapPosX + creature.collisionArea.x;
        int creatureRightAreaMapPosX = creature.mapPosX + creature.collisionArea.x + creature.collisionArea.width;
        int creatureTopAreaMapPosY = creature.mapPosY + creature.collisionArea.y;
        int creatureBottomAreaMapPosY = creature.mapPosY + creature.collisionArea.y + creature.collisionArea.height;

        // Converting collision area positions from pixels to columns and rows
        // To be able to get the index of the tiles in front of the creature from the array
        int creatureLeftAreaMapPosCol = creatureLeftAreaMapPosX / Game.tileSize;
        int creatureRightAreaMapPosCol = creatureRightAreaMapPosX / Game.tileSize;
        int creatureTopAreaMapPosRow = creatureTopAreaMapPosY / Game.tileSize;
        int creatureBottomAreaMapPosRow = creatureBottomAreaMapPosY / Game.tileSize;

        int tileIndex1, tileIndex2;

        switch (creature.direction) {
            case UP -> {
                creatureTopAreaMapPosRow = (creatureTopAreaMapPosY - creature.speed) / Game.tileSize;
                tileIndex1 = Game.tileManager.tileMap[creatureTopAreaMapPosRow][creatureLeftAreaMapPosCol];
                tileIndex2 = Game.tileManager.tileMap[creatureTopAreaMapPosRow][creatureRightAreaMapPosCol];
                if(Game.tileManager.tiles[tileIndex1].collision || Game.tileManager.tiles[tileIndex2].collision) {
                    creature.collided = true;
                }
            }
            case DOWN -> {
                creatureBottomAreaMapPosRow = (creatureBottomAreaMapPosY + creature.speed) / Game.tileSize;
                tileIndex1 = Game.tileManager.tileMap[creatureBottomAreaMapPosRow][creatureLeftAreaMapPosCol];
                tileIndex2 = Game.tileManager.tileMap[creatureBottomAreaMapPosRow][creatureRightAreaMapPosCol];
                if(Game.tileManager.tiles[tileIndex1].collision || Game.tileManager.tiles[tileIndex2].collision) {
                    creature.collided = true;
                }
            }
            case LEFT -> {
                creatureLeftAreaMapPosCol = (creatureLeftAreaMapPosX - creature.speed) / Game.tileSize;
                tileIndex1 = Game.tileManager.tileMap[creatureTopAreaMapPosRow][creatureLeftAreaMapPosCol];
                tileIndex2 = Game.tileManager.tileMap[creatureBottomAreaMapPosRow][creatureLeftAreaMapPosCol];
                if(Game.tileManager.tiles[tileIndex1].collision || Game.tileManager.tiles[tileIndex2].collision) {
                    creature.collided = true;
                }
            }
            case RIGHT -> {
                creatureRightAreaMapPosCol = (creatureRightAreaMapPosX + creature.speed) / Game.tileSize;
                tileIndex1 = Game.tileManager.tileMap[creatureTopAreaMapPosRow][creatureRightAreaMapPosCol];
                tileIndex2 = Game.tileManager.tileMap[creatureBottomAreaMapPosRow][creatureRightAreaMapPosCol];
                if(Game.tileManager.tiles[tileIndex1].collision || Game.tileManager.tiles[tileIndex2].collision) {
                    creature.collided = true;
                }
            }
        }
    }

    /**
     * Checks if a player collided with an item and returns the item index if the collision was successful.
     * @return index of the item from the array Item[] items created in the Game class
     */
    public static int checkCollisionWithItem(Player player) {
        int index = -1;
        for (int i = 0; i < Game.items.length; i++) {
            if(Game.items[i] != null) {
                // Get the position of the collision area of the player
                player.collisionArea.x = player.mapPosX + player.collisionArea.x;
                player.collisionArea.y = player.mapPosY + player.collisionArea.y;

                // Get the position of the collision area of the item
                Game.items[i].collisionArea.x = Game.items[i].mapPosX + Game.items[i].collisionArea.x;
                Game.items[i].collisionArea.y = Game.items[i].mapPosY + Game.items[i].collisionArea.y;

                // Simulating the player's movement and checking where he will be after moving
                switch (player.direction) {
                    case UP -> player.collisionArea.y -= player.speed;
                    case DOWN -> player.collisionArea.y += player.speed;
                    case LEFT -> player.collisionArea.x -= player.speed;
                    case RIGHT -> player.collisionArea.x += player.speed;
                }

                // .intersects -> automatically checks if rectangles collide
                if (player.collisionArea.intersects(Game.items[i].collisionArea)) {
                    index = i;
                }

                // Performing a reset after calculations
                player.collisionArea.x = player.collisionAreaDefaultX;
                player.collisionArea.y = player.collisionAreaDefaultY;
                Game.items[i].collisionArea.x = Game.items[i].collisionAreaDefaultX;
                Game.items[i].collisionArea.y = Game.items[i].collisionAreaDefaultY;
            }
        }
        return index;
    }

    /**
     * Collision of a specific creature with creatures.
     * Works like "checkCollisionWithItem".
     * @param creature Who (e.g. player)
     * @param target Whom (e.g. mobs)
     * @return the index of the creature encountered by the creature (e.g. or player).
     */
    public static int checkCollisionWithCreature(Creature creature, Creature[] target) {
        int index = -1;
        for (int i = 0; i < target.length; i++) {
            if(target[i] != null) {

                creature.collisionArea.x = creature.mapPosX + creature.collisionArea.x;
                creature.collisionArea.y = creature.mapPosY + creature.collisionArea.y;

                target[i].collisionArea.x = target[i].mapPosX + target[i].collisionArea.x;
                target[i].collisionArea.y = target[i].mapPosY + target[i].collisionArea.y;

                switch (creature.direction) {
                    case UP -> creature.collisionArea.y -= creature.speed;
                    case DOWN -> creature.collisionArea.y += creature.speed;
                    case LEFT -> creature.collisionArea.x -= creature.speed;
                    case RIGHT -> creature.collisionArea.x += creature.speed;
                }

                if (creature.collisionArea.intersects(target[i].collisionArea)) {
                    if(target[i] != creature) {
                        creature.collided = true;
                        index = i;
                    }
                }

                creature.collisionArea.x = creature.collisionAreaDefaultX;
                creature.collisionArea.y = creature.collisionAreaDefaultY;
                target[i].collisionArea.x = target[i].collisionAreaDefaultX;
                target[i].collisionArea.y = target[i].collisionAreaDefaultY;
            }
        }
        return index;
    }

    /**
     * Collision of a creature with a player.
     * It works like a "checkCollisionWithCreature", the only difference.
     * There is no need to scan the array, since the player is the only one on the map.
     */
    public static boolean checkCollisionWithPlayer(Creature creature) {
        boolean collisionWithPlayer = false;

        creature.collisionArea.x = creature.mapPosX + creature.collisionArea.x;
        creature.collisionArea.y = creature.mapPosY + creature.collisionArea.y;

        Game.player.collisionArea.x = Game.player.mapPosX + Game.player.collisionArea.x;
        Game.player.collisionArea.y = Game.player.mapPosY + Game.player.collisionArea.y;

        switch (creature.direction) {
            case UP -> creature.collisionArea.y -= creature.speed;
            case DOWN -> creature.collisionArea.y += creature.speed;
            case LEFT -> creature.collisionArea.x -= creature.speed;
            case RIGHT -> creature.collisionArea.x += creature.speed;
        }

        if (creature.collisionArea.intersects(Game.player.collisionArea)) {
            creature.collided = true;
            collisionWithPlayer = true;
        }

        creature.collisionArea.x = creature.collisionAreaDefaultX;
        creature.collisionArea.y = creature.collisionAreaDefaultY;
        Game.player.collisionArea.x = Game.player.collisionAreaDefaultX;
        Game.player.collisionArea.y = Game.player.collisionAreaDefaultY;

        return collisionWithPlayer;
    }
}
