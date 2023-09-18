package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;

/**
 * SpriteSheet is used to store a sprite sheet and crop it to get the desired sprite.
 */
public class SpriteSheet {

    /**
     * Reference to the BufferedImage object that contains the sprite sheet.
     */
    private final BufferedImage spriteSheet;
    /**
     * Width of a tile in the sprite sheet.
     */
    private final int tileWidth;
    /**
     * Height of a tile in the sprite sheet.
     */
    private final int tileHeight;

    /**
     * @param buffImg   BufferedImage object that contains the sprite sheet.
     * @param tileWidth Width of a tile in the sprite sheet.
     * @param tileHeight Height of a tile in the sprite sheet.
     */
    public SpriteSheet(BufferedImage buffImg, int tileWidth, int tileHeight) {
        /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /**
     * @param x X coordinate of the tile in the sprite sheet.
     * @param y Y coordinate of the tile in the sprite sheet.
     * @return BufferedImage object that contains the desired sprite.
     */
    public BufferedImage crop(int x, int y) {
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }

    /**
     * This method cuts the sprite for (x, y) coordinates in the sprite sheet.
     * @param x X coordinate of the tile in the sprite sheet.
     * @param y Y coordinate of the tile in the sprite sheet.
     * @return BufferedImage object that contains the desired sprite.
     */
    public BufferedImage cropAtPosition(int x, int y) {
        return spriteSheet.getSubimage(x, y, tileWidth, tileHeight);
    }

    /**
     * @param coord Vector2D object that contains the coordinates of the tile in the sprite sheet.
     * @return BufferedImage object that contains the desired sprite.
     */
    public BufferedImage crop(Vector2D coord) {
        return spriteSheet.getSubimage((int) (coord.x * tileWidth), (int) (coord.y * tileHeight), tileWidth, tileHeight);
    }


}
