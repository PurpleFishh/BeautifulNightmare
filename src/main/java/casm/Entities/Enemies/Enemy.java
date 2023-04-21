package casm.Entities.Enemies;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.GameObject;
import casm.Entities.Entity;
import casm.SpriteUtils.Animation.AnimationState;
import casm.SpriteUtils.Animation.AnimationsExtract;
import casm.Utils.Camera;
import casm.Utils.Vector2D;

import java.util.List;

public class Enemy  extends Entity {

    private final int enemySpawnPosX = 400, enemySpawnPosy = 30;

    public Enemy(Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super("enemy", spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        enemyInit();
    }

    public Enemy(String name, Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super(name, spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        enemyInit();
    }

    private void enemyInit() {
        this.addComponent(new AiBehaviour());

        this.init();
    }

}
