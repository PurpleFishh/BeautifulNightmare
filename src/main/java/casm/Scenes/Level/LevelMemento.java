package casm.Scenes.Level;

import casm.ECS.Components.PositionComponent;
import casm.Factory.EntityFactory.EntityType;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Player;
import casm.Objects.HeartBonus;
import casm.Utils.Vector2D;

import java.util.HashSet;
import java.util.Set;

/**
 * The memento class for the level scene
 */
public class LevelMemento implements Memento {
    /**
     * The id of the saved level
     */
    private int level = 1;
    /**
     * The score of the saved level
     */
    private double score;
    /**
     * The player saved state
     */
    private SavedEntity player;
    /**
     * The entites saved the state
     */
    private final Set<SavedEntity> entities = new HashSet<>();

    /**
     * Used to save the state of the entities
     */
    public static class SavedEntity {
        /**
         * The type of the entity
         */
        private final EntityType type;

        /**
         * The position of the entity
         */
        private final Vector2D position;
        /**
         * The health of the entity
         */
        private final double health;

        /**
         * @param type     The type of the entity
         * @param position The position of the entity
         * @param health   The health of the entity
         */
        public SavedEntity(EntityType type, Vector2D position, double health) {
            this.type = type;
            this.position = position;
            this.health = health;
        }

        /**
         * @return The type of the entity
         */
        public EntityType getType() {
            return type;
        }

        /**
         * @return The position of the entity
         */
        public Vector2D getPosition() {
            return position;
        }

        /**
         * @return The health of the entity
         */
        public double getHealth() {
            return health;
        }
    }

    /**
     * @param player  The player to be saved
     * @param enemies The enemies to be saved
     * @param hearts  The hearts to be saved
     * @param levelId The id of the level
     * @param score   The score of the level
     */
    public LevelMemento(Player player, Set<Enemy> enemies, Set<HeartBonus> hearts, int levelId, double score) {
        this.player = new SavedEntity(EntityType.PLAYER,
                (Vector2D) player.getComponent(PositionComponent.class).position.clone(), player.getLife());
        this.score = score;
        enemies.forEach(it -> this.entities.add(new SavedEntity(it.getType(),
                (Vector2D) it.getComponent(PositionComponent.class).position.clone(), it.getLife())));
        hearts.forEach(it -> this.entities.add(new SavedEntity(EntityType.HEART_BONUS,
                (Vector2D) it.getComponent(PositionComponent.class).position.clone(), 0)));
        this.level = levelId;
    }

    /**
     * @param entities The entities to be saved
     * @param levelId  The id of the level
     * @param score    The score of the level
     */
    public LevelMemento(Set<SavedEntity> entities, int levelId, double score) {
        entities.forEach(entity -> {
            if (entity.getType() == EntityType.PLAYER)
                this.player = entity;
            else
                this.entities.add(entity);
        });
        this.level = levelId;
        this.score = score;
    }

    /**
     * @return The level id
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return The player saved state
     */
    public SavedEntity getPlayer() {
        return player;
    }

    /**
     * @return The entities saved state
     */
    public Set<SavedEntity> getEntities() {
        return entities;
    }

    /**
     * @return The score of the level
     */
    public double getScore() {
        return score;
    }

}
