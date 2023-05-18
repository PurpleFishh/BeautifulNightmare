package casm.Scenes.Level;

import casm.ECS.Components.PositionComponent;
import casm.Factory.EntityFactory.EntityType;
import casm.Objects.Entities.Enemies.Enemy;
import casm.Objects.Entities.Player;
import casm.Objects.HeartBonus;
import casm.Utils.Vector2D;

import java.util.HashSet;
import java.util.Set;

public class LevelMemento implements Memento {
    private int level = 1;
    private double score;
    private SavedEntity player;
    private final Set<SavedEntity> entities = new HashSet<>();

    public static class SavedEntity {
        private final EntityType type;

        private final Vector2D position;
        private final double health;

        public SavedEntity(EntityType type, Vector2D position, double health) {
            this.type = type;
            this.position = position;
            this.health = health;
        }

        public EntityType getType() {
            return type;
        }

        public Vector2D getPosition() {
            return position;
        }

        public double getHealth() {
            return health;
        }
    }
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
    public LevelMemento(Set<SavedEntity> entities, int levelId, double score) {
        entities.forEach(entity -> {
            if(entity.getType() == EntityType.PLAYER)
                this.player = entity;
            else
                this.entities.add(entity);
        });
        this.level = levelId;
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public SavedEntity getPlayer() {
        return player;
    }

    public Set<SavedEntity> getEntities() {
        return entities;
    }

    public double getScore() {
        return score;
    }

}
