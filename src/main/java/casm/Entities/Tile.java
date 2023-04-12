package casm.Entities;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;

import java.util.function.IntBinaryOperator;

public class Tile extends GameObject {

    private int textureId;
    private int tileWidth = 0, tileHeight = 0;

    public Tile(String name, Assets tileSet, long textureId, int x, int y) {
        super(name);
        byte flipped = (byte) ((textureId & 0b11110000000000000000000000000000) >> 28);
        boolean flipped_vertically = (flipped & 0b1000) == 0b1000;
        boolean flipped_horizontally = (flipped & 0b0100) == 0b0100;
        this.textureId = (int) (textureId & (~0b11110000000000000000000000000000));

        tileMaker(tileSet, x, y, flipped_vertically, flipped_horizontally);
    }

    private void tileMaker(Assets tileSet, int x, int y, boolean flipped_vertically, boolean flipped_horizontally) {
        Sprite texture = tileSet.getSprite(textureId);
        tileWidth = texture.getWidth();
        tileHeight = texture.getHeight();
        this.addComponent(new PositionComponent(x * tileWidth, y * tileHeight, tileWidth, tileHeight));
        this.addComponent(new SpriteComponent(texture, flipped_vertically, flipped_horizontally));
        this.init();
    }

    private void tileMaker(Assets tileSet, int x, int y) {
        Sprite texture = tileSet.getSprite(textureId);
        tileWidth = texture.getWidth();
        tileHeight = texture.getHeight();
        this.addComponent(new PositionComponent(x * tileWidth, y * tileHeight));
        this.addComponent(new SpriteComponent(texture));
        this.init();
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
