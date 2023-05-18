package casm.Objects.Entities.Enemies;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderType;
import casm.Factory.EntityFactory.EntityType;
import casm.Game;
import casm.Objects.Entities.Entity;
import casm.Utils.Vector2D;

import java.util.Objects;

public class Enemy extends Entity {

    private final int enemySpawnPosX = 400, enemySpawnPosy = 30;
    private EntityType type;

    public Enemy(EntityType type, Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super("enemy", spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        this.type = type;
        enemyInit();
    }

    public Enemy(String name, EntityType type, Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super(name, spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        this.type = type;
        enemyInit();
    }

    private void enemyInit() {
        this.addComponent(new AiBehaviour());
        this.addComponent(new AttackComponent(type));

        // this.init();
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public void kill() {
        super.kill();
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateScore(
                Objects.requireNonNull(Game.getLevelScene()).addToScore(5));
    }
}
