package casm.Factory.EntityFactory;

import casm.Objects.Entities.Enemies.Catfish;
import casm.Objects.Entities.Enemies.TurtleKing;
import casm.Objects.Entities.Enemies.WeaselFisherman;
import casm.Objects.Entities.Entity;
import casm.Objects.Entities.Player;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Objects.HeartBonus;
import casm.Objects.Object;
import casm.Utils.Vector2D;

/**
 * Factory for creating entities
 */
public class EntityFactory implements Factory {
    /**
     * Create an entity
     *
     * @param type          the type of the entity
     * @param spawnPosition the position where it will be spawned
     * @return the entity created
     */
    @Override
    public Object create(FactoryTypes type, Vector2D spawnPosition) {
        if (type.equals(EntityType.PLAYER)) {
            return new Player(spawnPosition);
        } else if (type.equals(EntityType.WEASEL_FISHERMAN)) {
            return new WeaselFisherman(spawnPosition);
        } else if (type.equals(EntityType.CATFISH)) {
            return new Catfish(spawnPosition);
        } else if (type.equals(EntityType.HEART_BONUS)) {
            return new HeartBonus(spawnPosition);
        }else if (type.equals(EntityType.TURTLE_KING)) {
            return new TurtleKing(spawnPosition);
        } else {
            return null;
        }
    }
}
