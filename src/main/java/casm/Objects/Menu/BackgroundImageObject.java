package casm.Objects.Menu;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Objects.Object;
import casm.SpriteUtils.Sprite;
import casm.Utils.Vector2D;

public class BackgroundImageObject extends Object {

    public BackgroundImageObject(Vector2D spawnPosition, Sprite sprite, int width, int height) {
        super("image", spawnPosition, width, height);
        initObject(spawnPosition, sprite, width, height);
    }
    public BackgroundImageObject(Vector2D spawnPosition, Sprite sprite) {
        super("image", spawnPosition);
        initObject(spawnPosition, sprite, 0, 0);
    }

    public void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
    }

    public void initObject(Vector2D spawnPosition, Sprite sprite, int width, int height) {
        this.addComponent(new SpriteComponent(sprite, width, height));

       // this.init();
    }
}
