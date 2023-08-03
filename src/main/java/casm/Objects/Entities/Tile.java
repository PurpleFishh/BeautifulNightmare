package casm.Objects.Entities;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;

import java.util.function.IntBinaryOperator;

/**
 * The tile object used for the map
 */
public class Tile extends GameObject {

    /**
     * The id of the texture in the sprite sheet
     */
    private final int textureId;
    /**
     * <b>tileWidth</b> - The width of the tile<br>
     * <b>tileHeight</b> - The height of the tile
     */
    private int tileWidth = 0, tileHeight = 0;

    /**
     * @param name      The name of the tile
     * @param tileSet   The sprite sheet
     * @param textureId The id of the texture in the sprite sheet
     * @param x         The x position of the tile
     * @param y         The y position of the tile
     */
    public Tile(String name, Assets tileSet, long textureId, int x, int y) {
        super(name);
        byte flipped = (byte) ((textureId & 0b11110000000000000000000000000000) >> 28);
        boolean flipped_vertically = (flipped & 0b0100) == 0b0100;
        boolean flipped_horizontally = (flipped & 0b1000) == 0b1000;
        boolean antiDiagonalFlip = (flipped & 0b0010) == 0b0010;
        this.textureId = (int) (textureId & (~0b11110000000000000000000000000000));

        tileMaker(tileSet, x, y, flipped_vertically, flipped_horizontally, antiDiagonalFlip);
    }

    /**
     * Initializes the tile
     *
     * @param tileSet              The sprite sheet
     * @param x                    The x position of the tile
     * @param y                    The y position of the tile
     * @param flipped_vertically   If the tile is flipped vertically
     * @param flipped_horizontally If the tile is flipped horizontally
     * @param antiDiagonalFlip     If the tile is flipped anti-diagonally
     */
    private void tileMaker(Assets tileSet, int x, int y, boolean flipped_vertically, boolean flipped_horizontally, boolean antiDiagonalFlip) {
        Sprite texture = tileSet.getSprite(textureId);
        tileWidth = texture.getWidth();
        tileHeight = texture.getHeight();
        this.addComponent(new PositionComponent(x * tileWidth, y * tileHeight, tileWidth, tileHeight));
        this.addComponent(new SpriteComponent(texture, flipped_vertically, flipped_horizontally, antiDiagonalFlip));
        //this.init();
    }

    public int getTextureId() {
        return textureId;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
