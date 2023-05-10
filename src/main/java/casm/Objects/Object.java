package casm.Objects;

import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Utils.Vector2D;

public class Object extends GameObject {

    private int width, height;
    private Vector2D spawnPosition;

    public Object(String name, Vector2D spawnPosition, int entityWidth, int entityHeight) {
        super(name);
        this.width = entityWidth;
        this.height = entityHeight;
        this.spawnPosition = spawnPosition;
        entityInit(this.spawnPosition, width, height);
    }

    public Object(String name, Vector2D spawnPosition) {
        super(name);
        this.width = 0;
        this.height = 0;
        this.spawnPosition = spawnPosition;
        entityInit(this.spawnPosition, width, height);
    }

    private void entityInit(Vector2D spawnPosition, int entityWidth, int entityHeight) {
        this.addComponent(new PositionComponent(spawnPosition.x, spawnPosition.y, entityWidth, entityHeight));

        //this.init();
    }

    protected void updateDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        this.getComponent(PositionComponent.class).setDimensions(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2D getSpawnPosition() {
        return spawnPosition;
    }
}
