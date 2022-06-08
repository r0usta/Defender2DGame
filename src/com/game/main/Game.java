package com.game.main;

import com.game.entity.Entity;
import com.game.entity.Player;
import com.game.event.EventManager;
import com.game.item.Item;
import com.game.mob.Mob;
import com.game.npc.NPC;
import com.game.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Game extends JPanel implements Runnable {

    //region Screen Settings
    public static final byte originalTileSize = 16; // the size of all textures 16x16 pixels
    public static final byte scale = 3;

    public static final byte tileSize = originalTileSize * scale; // to make the tiles not seem too small on the screen

    // How many tiles can be displayed on the screen
    public static final byte maxScreenCol = 20;
    public static final byte maxScreenRow = 12;

    public static final short screenWidth = tileSize * maxScreenCol; // 960 pixels
    public static final short screenHeight = tileSize * maxScreenRow; // 576 pixels
    //endregion

    //region Map Settings
    public static final int maxMapCol = 32;
    public static final int maxMapRow = 40;
    public static final int mapWidth = tileSize * maxMapCol;
    public static final int mapHeight = tileSize * maxMapRow;
    //endregion

    private Thread thread;
    public static KeyManager keyManager;
    public static Player player;
    public static Item[] items;
    public static TileManager tileManager;
    public static Sound music;
    public static Sound soundEffect;
    public static State state;
    public static GUIManager guiManager;
    public static NPC[] npcs;
    public static Mob[] mobs;
    public static EventManager eventManager;

    private final ArrayList<Entity> creatures;

    public Game() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);

        keyManager = new KeyManager();
        this.addKeyListener(keyManager);
        this.setFocusable(true);

        player = new Player();
        items = new Item[1];
        npcs = new NPC[1];
        mobs = new Mob[4];

        tileManager = new TileManager();
        eventManager = new EventManager();

        music = new Sound();
        soundEffect = new Sound();

        guiManager = new GUIManager();
        creatures = new ArrayList<>();
    }

    //region Music & Sound Effect
    public static void playMusic(int index) {
        music.setFile(index);
        music.play();
        music.loop();
    }

    public static void stopMusic() {
        music.stop();
    }

    public static void playSoundEffect(int index) {
        soundEffect.setFile(index);
        soundEffect.play();
    }
    //endregion

    /**
     * Called in the Main class, it creates instances
     * of entities and a thread, and starts the thread.
     */
    public void start() {
        EntityManager.loadNPCs();
        EntityManager.loadItems();
        EntityManager.loadMobs();
        playMusic(0);
        state = State.PLAY;

        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Is called in the KeyManager class
     * when the player selects the retry option
     */
    public static void retry() {
        player.setDefaultPosition();
        player.restoreLife();
        EntityManager.loadNPCs();
        EntityManager.loadMobs();
    }

    /**
     * Responsible for the work of all creatures in the game.
     * All data about them (position, AI, collision check) is updated.
     * This method is called in the run() method every 60 times per second.
     */
    public void update() {
        if(state == State.PLAY) {
            for(NPC npc : npcs) {
                if(npc != null) {
                    npc.update();
                }
            }
            for(int i = 0; i < mobs.length; i++) {
                if(mobs[i] != null) {
                    if(mobs[i].alive && !mobs[i].dying) {
                        mobs[i].update();
                    }
                    if(!mobs[i].alive){
                        mobs[i] = null;
                    }
                }
            }

            player.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        long drawStart = System.nanoTime();

        // Draw tiles (must be done before drawing entities,
        // otherwise tiles will be drawn on top of them)
        tileManager.draw(g2);

        for (Item item : items) {
            if (item != null) {
                item.draw(g2);
            }
        }

        // Adding creatures to a ArrayList
        {
            /*
                By adding creatures to ArrayList, I can sort them.
                To avoid a situation where the player is above an
                NPC or mob that has a modified collision area,
                and for all that, he's drawn on top of that creature.
                Even though logically he is behind the creature.
             */

            creatures.add(player);

            for(NPC npc: npcs) {
                if(npc != null) {
                    creatures.add(npc);
                }
            }

            for(Mob mob: mobs) {
                if(mob != null) {
                    creatures.add(mob);
                }
            }

            // Sorting them according to their position on the Y-axis
            // If creature c1 is lower than c2, we first draw creature c2
            creatures.sort(Comparator.comparingInt(c -> c.mapPosY));

            // Draw them in order of those already sorted
            for (Entity entity : creatures) {
                entity.draw(g2);
            }

            // Cleaning the ArrayList
            creatures.clear();
        }

        // Draw the interface
        guiManager.draw(g2);

        // region Debug
        if(keyManager.debugPressed) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = tileSize / 3;
            int y = tileSize * 10;
            int lineHeight = 20;

            g2.drawString("Map X: " + player.mapPosX, x, y); y+= lineHeight;
            g2.drawString("Map Y: " + player.mapPosY, x, y); y+= lineHeight;
            g2.drawString("Tile Row: " + (player.mapPosY + player.collisionArea.y)/tileSize, x, y); y+= lineHeight;
            g2.drawString("Tile Col: " + (player.mapPosX + player.collisionArea.x)/tileSize, x, y); y+= lineHeight;
            g2.drawString("Draw Time: " + passed, x, y);
        }
        //endregion

        g2.dispose();
    }


    @Override
    public void run() {
        int fps = 60;
        double drawInterval = 1000000000.0/ fps;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (thread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;

            lastTime = now;
            if(delta >= 1) {
                update(); // update information such as entity positions
                repaint(); // draw the screen with the update information
                delta--;
            }
        }
    }
}