package com.game.entity;

import java.awt.*;

/**
 * Parent class of all entities and creatures (player, NPC, items)
 */
public abstract class Entity {
    /**
     * Entity position on the map
     */
    public int mapPosX, mapPosY;

    /**
     * Used to simulate a collision
     */
    public Rectangle collisionArea;
    public int collisionAreaDefaultX, collisionAreaDefaultY;
    public boolean collided = false;

    public Entity() {
        // Default collision settings for all entities
        collisionArea = new Rectangle(0, 0, 48, 48);
    }

    /**
     * Auxiliary variable, allows it to change the transparency of the sprite
     */
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    /**
     * One of the main methods of this class and its subclasses.
     * It is called, every frame and is responsible for
     * the physics of the entities that are processed by the processor.
     * Depending on the subclass, may be overwritten or not called at all.
     */
    public abstract void update();

    /**
     * One of the main methods of this class and its subclasses.
     * It is called every frame and is responsible
     * for visualization of entities processed by the GPU.
     * This method must always be implemented, but can also be overwritten.
     */
    public abstract void draw(Graphics2D g2);
}