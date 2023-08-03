package casm.Objects.Entities;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;

/**
 * The tile object used for the map
 */
public class EmptyTile extends GameObject {

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
    public EmptyTile(String name, int x, int y, int w, int h) {
        super(name);

        tileMaker(x, y, w, h);
    }

    /**
     * Initializes the tile
     *
     * @param x                    The x position of the tile
     * @param y                    The y position of the tile
     * @param w                    The width of the tile
     * @param h                    The height of the tile
     */


    private void tileMaker(int x, int y, int w, int h) {
        tileWidth = w;
        tileHeight = h;
        this.addComponent(new PositionComponent(x * tileWidth, y * tileHeight, tileWidth, tileHeight));
        this.addComponent(new SpriteComponent(AssetsCollection.getInstance().blankSprite));
        //this.init();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
