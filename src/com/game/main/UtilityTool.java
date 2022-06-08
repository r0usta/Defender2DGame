package com.game.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * This class is a set of tools for storing convenient functions
 */
public class UtilityTool {

    /**
     * When I draw a tile in the TileManager class,
     * I set its scale (with Game.tileSize), which is very useful, BUT.
     * When it happens 60 times per second, and for every tile that
     * is rendered on the screen, it takes a very long time for the GPU.
     * This method allows me to scale the tiles in advance so that
     * Graphics2D can skip this process.
     *
     * This method will allow me to scale
     * the tile immediately after importing it
     * into the BufferedImage, this improves
     * the rendering performance.
     * @param width The width by which we want to change the scale
     * @param height The height to which we want to change the scale
     * @return scaled image
     */
    public static BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, originalImage.getType());

        // Creates a Graphics2D that can be used to draw into this BufferedImage
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

    public static BufferedImage scaleGUIImage(BufferedImage originalImage) {
        int newWidth = originalImage.getWidth() * Game.scale;
        int newHeight = originalImage.getHeight() * Game.scale;

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return scaledImage;
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(UtilityTool.class.getResourceAsStream(path)));
    }

    /**
     * @return new image of the x and y area specified
     */
    public static BufferedImage crop(BufferedImage image, int x, int y) {
        return image.getSubimage(x, y, Game.originalTileSize, Game.originalTileSize);
    }

    /**
     * Allows to cut an image with adjustable height and width
     */
    public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    /**
     * Because the position of the text is taken from its beginning in the upper left corner.
     * Sometimes you need to know the middle of the text. This is what this method is for.
     */
    public static int getXForCenteredText(Graphics2D g2, String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return Game.screenWidth/2 - length/2;
    }

    public static int getXForAlignToRightText(String text, int tailX, Graphics2D g2) {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return tailX - length;
    }

    public static Font loadFont(String path) {
        InputStream is = UtilityTool.class.getResourceAsStream(path);
        try {
            assert is != null;
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}