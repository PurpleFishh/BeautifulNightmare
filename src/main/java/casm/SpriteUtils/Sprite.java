package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;

/**
 * Sprite is used to store a sprite and its dimensions.
 */
public class Sprite {


    /**
     * The texture of the sprite.
     */
    private BufferedImage texture;
    /**
     * <b>width</b> and <b>height</b> are the dimensions of the sprite.
     */
    private int width, height;
    /**
     * is the coordinate of the sprite in the sprite sheet.
     */
    private Vector2D coord_in_spritesheet;

    /**
     * Generate a blank sprite(without texture).
     */
    public Sprite() {
        texture = null;
        width = 0;
        height = 0;
    }

    /**
     * @param texture BufferedImage object that contains the sprite.
     */
    public Sprite(BufferedImage texture) {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    /**
     * @param texture BufferedImage object that contains the sprite.
     * @param width   Width of the sprite.
     * @param height  Height of the sprite.
     */
    public Sprite(BufferedImage texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    /**
     * @param texture BufferedImage object that contains the sprite.
     * @param width  Width of the sprite.
     * @param height Height of the sprite.
     * @param coord_in_spritesheet is the coordinate of the sprite in the sprite sheet.
     */
    public Sprite(BufferedImage texture, int width, int height, Vector2D coord_in_spritesheet) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.coord_in_spritesheet = coord_in_spritesheet;
    }

    /**
     * @return BufferedImage object that contains the sprite.
     */
    public BufferedImage getTexture() {
        return texture;
    }

    /**
     * @param texture BufferedImage object that contains the sprite.
     */
    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    /**
     * @return Width of the sprite.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width Width of the sprite.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return Height of the sprite.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height Height of the sprite.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return Vector2D object that contains the coordinates of the tile in the sprite sheet.
     */
    public Vector2D getCoord_in_spritesheet() {
        return coord_in_spritesheet;
    }

    /**
     * @param coord_in_spritesheet Vector2D object that contains the coordinates of the tile in the sprite sheet.
     */
    public void setCoord_in_spritesheet(Vector2D coord_in_spritesheet) {
        this.coord_in_spritesheet = coord_in_spritesheet;
    }


}
