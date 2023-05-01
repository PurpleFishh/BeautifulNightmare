package casm.Entities;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.Utils.Vector2D;

public class SpawnDoor extends GameObject {
    /**
     * Create a new entity
     *
     * @param name name for the entity
     */
    public SpawnDoor(String name) {
        super(name);
        initialize(0, 0);
    }

    public SpawnDoor(String name, Vector2D postion) {
        super(name);
        initialize((int) postion.x, (int) postion.y);
    }

    public void initialize(int x, int y) {
        Assets asset = AssetsCollection.getInstance().addSprite("spawn_door.png");
        this.addComponent(new PositionComponent(x, y, asset.getSprite(0).getWidth(), asset.getSprite(0).getHeight()));
        this.addComponent(new SpriteComponent(asset.getSprite(0)));
        this.init();
    }
}
