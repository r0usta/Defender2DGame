package com.game.tile;

import com.game.main.Game;
import com.game.main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class TileManager {
    private static final String TILESET_PATH = "/assets/textures/tile/tileset.png";
    private static final String TILES_PARAMETERS_PATH = "/data/tile/parameters.csv";
    private static final String MAP_MAIN_PATH = "/data/map/map_main.csv";

    /**
     * An array storing each tile with its own configuration
     * @see Tile
     */
    public Tile[] tiles;

    /**
     * Stores tile indexes from the loaded .csv map(s)
     */
    public int[][] tileMap;

    public TileManager() {
        tileMap = new int[Game.maxMapRow][Game.maxMapCol];

        try {
            this.loadTileImage(TILESET_PATH);
            this.loadTileParameters(TILES_PARAMETERS_PATH);
            this.loadMap(MAP_MAIN_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTileImage(String path) throws IOException {
        BufferedImage image = UtilityTool.loadImage(path);

        int imageCol = image.getWidth() / Game.originalTileSize;
        int imageRow = image.getHeight() / Game.originalTileSize;
        tiles = new Tile[imageCol * imageRow];

        int tileIndex = 0;

        int imagePosY = 0;
        for(int row = 0; row < imageRow; row++) {
            int imagePosX = 0;
            for(int col = 0; col < imageCol; col++) {
                tiles[tileIndex] = new Tile();
                tiles[tileIndex].image = UtilityTool.crop(image, imagePosX, imagePosY);
                tiles[tileIndex].image = UtilityTool.scaleImage(tiles[tileIndex].image, Game.tileSize, Game.tileSize);
                imagePosX += Game.originalTileSize;
                tileIndex++;
            }
            imagePosY += Game.originalTileSize;
        }
    }

    public void loadTileParameters(String path) throws IOException {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(path));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.readLine();

        for(int i = 0; i < tiles.length; i++) {
            String[] line = br.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            tiles[index].collision = Boolean.parseBoolean(line[1]);
        }

        is.close();
        br.close();
    }

    public void loadMap(String path) throws IOException {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(path));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        for(int row = 0; row < Game.maxMapRow; row++) {
            String[] tileIndexes = br.readLine().split(",");
            for(int col = 0; col < tileIndexes.length; col++) {
                int tileIndex = Integer.parseInt(tileIndexes[col]);
                this.tileMap[row][col] = tileIndex;
            }
        }

        is.close();
        br.close();
    }

    /**
     * Responsible for drawing tiles.
     */
    public void draw(Graphics2D g2) {
        for(int mapPosRow = 0; mapPosRow < Game.maxMapRow; mapPosRow++) {
            for(int mapPosCol = 0; mapPosCol < Game.maxMapCol; mapPosCol++) {
                // Returns the tile index from the array, so it can be drawn on the screen
                int tileIndex = tileMap[mapPosRow][mapPosCol];

                // Tile positions are translated to coordinate, so we know where to draw tiles on the screen
                int tileMapPosX = mapPosCol * Game.tileSize;
                int tileMapPosY = mapPosRow * Game.tileSize;
                int tileScreenPosX = tileMapPosX - Game.player.mapPosX + Game.player.screenPosX;
                int tileScreenPosY = tileMapPosY - Game.player.mapPosY + Game.player.screenPosY;

                // Additional calculations (so that the camera stops when the player has reached the edge of the map)
                if(Game.player.screenPosX > Game.player.mapPosX) {
                    tileScreenPosX = tileMapPosX;
                }

                if(Game.player.screenPosY > Game.player.mapPosY) {
                    tileScreenPosY = tileMapPosY;
                }

                int rightOffset = Game.screenWidth - Game.player.screenPosX;
                if(rightOffset > Game.mapWidth - Game.player.mapPosX) {
                    tileScreenPosX = Game.screenWidth - (Game.mapWidth - tileMapPosX);
                }

                int bottomOffset = Game.screenHeight - Game.player.screenPosY;
                if(bottomOffset > Game.mapHeight - Game.player.mapPosY) {
                    tileScreenPosY = Game.screenHeight - (Game.mapHeight - tileMapPosY);
                }

                // If the tile position is in the visible area of the screen it is drawn, otherwise it is skipped.
                // Why draw something that the player can't see anyway?
                if(tileMapPosX + Game.tileSize > Game.player.mapPosX - Game.player.screenPosX &&
                    tileMapPosX - Game.tileSize < Game.player.mapPosX + Game.player.screenPosX &&
                    tileMapPosY + Game.tileSize > Game.player.mapPosY - Game.player.screenPosY &&
                    tileMapPosY - Game.tileSize < Game.player.mapPosY + Game.player.screenPosY) {

                    g2.drawImage(tiles[tileIndex].image, tileScreenPosX, tileScreenPosY, null);
                }else if(Game.player.screenPosX > Game.player.mapPosX ||
                        Game.player.screenPosY > Game.player.mapPosY ||
                        rightOffset > Game.mapWidth - Game.player.mapPosX ||
                        bottomOffset > Game.mapHeight - Game.player.mapPosY) {
                    g2.drawImage(tiles[tileIndex].image, tileScreenPosX, tileScreenPosY, null);
                }
            }
        }
    }
}
