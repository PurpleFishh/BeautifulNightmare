package casm.Objects.Entities;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.Objects.Object;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.SpriteUtils.Sprite;
import casm.Utils.Vector2D;

/**
 * The spawn door is the door where the player will spawn representing the transition between levels
 */
public class SpawnDoor extends Object {
    /**
     * Create a new entity
     *
     * @param name name for the entity
     */
    public SpawnDoor(String name) {
        super(name, new Vector2D(0, 0));
        initialize();
    }

    /**
     * @param name     name for the entity
     * @param position position for the door
     */
    public SpawnDoor(String name, Vector2D position) {
        super(name, position);
        initialize();
    }

    /**
     * Initialize the spawn door
     */
    public void initialize() {
        Sprite asset = AssetsCollection.getInstance().addSprite("spawn_door.png");
        super.updateDimensions(asset.getWidth(), asset.getHeight());
        this.addComponent(new SpriteComponent(asset));
        // this.init();
    }
}
