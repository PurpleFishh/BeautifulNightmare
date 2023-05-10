package casm.SpriteUtils;

import casm.Utils.Vector2D;

import java.awt.image.BufferedImage;

public class Sprite {


    private BufferedImage texture;
    private int width, height;
    private Vector2D coord_in_spritesheet;

    public Sprite()
    {
        texture = null;
        width = 0;
        height = 0;
    }
    public Sprite(BufferedImage texture) {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }
    public Sprite(BufferedImage texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
    public Sprite(BufferedImage texture, int width, int height, Vector2D coord_in_spritesheet) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.coord_in_spritesheet = coord_in_spritesheet;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2D getCoord_in_spritesheet() {
        return coord_in_spritesheet;
    }

    public void setCoord_in_spritesheet(Vector2D coord_in_spritesheet) {
        this.coord_in_spritesheet = coord_in_spritesheet;
    }


}
