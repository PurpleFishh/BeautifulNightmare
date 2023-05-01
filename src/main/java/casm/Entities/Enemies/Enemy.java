package casm.Entities.Enemies;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderType;
import casm.Entities.Entity;
import casm.Utils.Vector2D;

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
