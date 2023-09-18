package casm.Objects;

import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Utils.Vector2D;

/**
 * This is the base object of the game<br>
 * All the entities are extended from that class<br>
 * It is an basic object with just spawn position and dimensions
 */
public class Object extends GameObject {

    /**
     * Object dimensions
     */
    private int width, height;
    /**
     * Object spawn position
     */
    private final Vector2D spawnPosition;

    /**
     * Creat a new object
     *
     * @param name          the name of the object
     * @param spawnPosition the spawn position of the object
     * @param entityWidth   object width
     * @param entityHeight  objet height
     */
    public Object(String name, Vector2D spawnPosition, int entityWidth, int entityHeight) {
        super(name);
        this.width = entityWidth;
        this.height = entityHeight;
        this.spawnPosition = spawnPosition;
        entityInit(this.spawnPosition, width, height);
    }

    /**
     * Creat a new object
     *
     * @param name          the name of the object
     * @param spawnPosition the spawn position of the object
     */
    public Object(String name, Vector2D spawnPosition) {
        super(name);
        this.width = 0;
        this.height = 0;
        this.spawnPosition = spawnPosition;
        entityInit(this.spawnPosition, width, height);
    }

    /**
     * Creat a new object
     *
     * @param spawnPosition the spawn position of the object
     * @param entityWidth   object width
     * @param entityHeight  objet height
     */
    private void entityInit(Vector2D spawnPosition, int entityWidth, int entityHeight) {
        this.addComponent(new PositionComponent(spawnPosition.x, spawnPosition.y, entityWidth, entityHeight));

        //this.init();
    }

    /**
     * Update the dimensions of the object
     *
     * @param width  new width
     * @param height new height
     */
    protected void updateDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        this.getComponent(PositionComponent.class).setDimensions(width, height);
    }

    /**
     * @return get the width of the object
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return get the height of the object
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return get the spawn position of the object
     */
    public Vector2D getSpawnPosition() {
        return spawnPosition;
    }
}
