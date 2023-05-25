package casm.Objects.Entities.Enemies;

import casm.ECS.Components.*;
import casm.ECS.Components.Collision.ColliderType;
import casm.Factory.EntityFactory.EntityType;
import casm.Game;
import casm.Objects.Entities.Entity;
import casm.Utils.Vector2D;

import java.util.Objects;

/**
 * The enemy of the game
 */
public class Enemy extends Entity {

    /**
     * The type of the enemy
     */
    private EntityType type;

    /**
     * @param type          type of the enemy
     * @param spawnPosition position for the enemy
     * @param enemyWidth    width for the enemy
     * @param enemyHeight   height for the enemy
     */
    public Enemy(EntityType type, Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super("enemy", spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        this.type = type;
        enemyInit();
    }

    /**
     * @param name          name for the enemy
     * @param type          type of the enemy
     * @param spawnPosition position for the enemy
     * @param enemyWidth    width for the enemy
     * @param enemyHeight   height for the enemy
     */
    public Enemy(String name, EntityType type, Vector2D spawnPosition, int enemyWidth, int enemyHeight) {
        super(name, spawnPosition, enemyWidth, enemyHeight, ColliderType.ENTITY, enemyWidth - 2, enemyHeight - 2);
        this.type = type;
        enemyInit();
    }

    /**
     * Initialize the enemy
     */
    private void enemyInit() {
        this.addComponent(new AiBehaviour());
        this.addComponent(new AttackComponent(type));

        // this.init();
    }

    /**
     * @return the type of the enemy
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Kills the enemy
     */
    @Override
    public void kill() {
        super.kill();
        Objects.requireNonNull(Game.getLevelScene()).infoBar.updateScore(
                Objects.requireNonNull(Game.getLevelScene()).addToScore(5));
    }
}
