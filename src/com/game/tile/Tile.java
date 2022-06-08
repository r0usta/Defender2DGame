package com.game.tile;

import java.awt.image.BufferedImage;

public class Tile {
    protected BufferedImage image;
    /**
     * Can entities collision tile, the default when creating an instance of a class is false.
     */
    public boolean collision = false;
}
