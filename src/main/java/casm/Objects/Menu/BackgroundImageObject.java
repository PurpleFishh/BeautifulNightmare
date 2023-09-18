package casm.Objects.Menu;

import casm.ECS.Components.SpriteComponent;
import casm.Objects.Object;
import casm.SpriteUtils.Sprite;
import casm.Utils.Vector2D;

/**
 * Used to create static images, used mostly for the backgrounds
 */
public class BackgroundImageObject extends Object {

    /**
     * @param spawnPosition the spawn position of the object
     * @param sprite        the sprite of the object
     * @param width         the width of the object(if you give bigger dimensions, it will be up scaled)
     * @param height        the height of the object(if you give bigger dimensions, it will be up scaled)
     */
    public BackgroundImageObject(Vector2D spawnPosition, Sprite sprite, int width, int height) {
        super("image", spawnPosition, width, height);
        initObject(spawnPosition, sprite, width, height);
    }

    /**
     * @param spawnPosition the spawn position of the object
     * @param sprite        the sprite of the object
     */
    public BackgroundImageObject(Vector2D spawnPosition, Sprite sprite) {
        super("image", spawnPosition);
        initObject(spawnPosition, sprite);
    }

    /**
     * @param width  new width (if you give bigger dimensions, it will be up scaled)
     * @param height new height (if you give bigger dimensions, it will be up scaled)
     */
    public void updateDimensions(int width, int height) {
        super.updateDimensions(width, height);
    }

    /**
     * @param spawnPosition the spawn position of the object
     * @param sprite        the sprite of the object
     * @param width         the width of the object(if you give bigger dimensions, it will be up scaled)
     * @param height        the height of the object(if you give bigger dimensions, it will be up scaled)
     */
    public void initObject(Vector2D spawnPosition, Sprite sprite, int width, int height) {
        this.addComponent(new SpriteComponent(sprite, width, height));
        // this.init();
    }

    /**
     * @param spawnPosition the spawn position of the object
     * @param sprite       the sprite of the object
     */
    public void initObject(Vector2D spawnPosition, Sprite sprite) {
        this.addComponent(new SpriteComponent(sprite, sprite.getWidth(), sprite.getHeight()));
        super.updateDimensions(sprite.getWidth(), sprite.getHeight());
        // this.init();
    }
}
