package com.game.main;
import com.game.item.Key;
import com.game.mob.GreenSlime;
import com.game.npc.OldMan;

/**
 * A special class that is responsible for placing stuff on the map.
 * Creating a separate class is a great idea, so as not to overload the Game class.
 */
public class EntityManager {

    /**
     * This method creates instances of the Item class and places them on the map
     */
    public static void loadItems() {
        int index = 0;

        // I just created one item on the map, for example
        Game.items[index] = new Key(8,7);
    }

    public static void loadNPCs() {
        int index = 0;

        Game.npcs[index] = new OldMan(7, 12);
    }

    public static void loadMobs() {
        int index = 0;

        Game.mobs[index] = new GreenSlime(4, 23);
        Game.mobs[++index] = new GreenSlime(10, 30);
        Game.mobs[++index] = new GreenSlime(23, 25);
        Game.mobs[++index] = new GreenSlime(19, 15);
    }
}
