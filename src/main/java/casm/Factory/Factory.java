package casm.Factory;

import casm.Objects.Entities.Entity;
import casm.Objects.Object;
import casm.Utils.Vector2D;

/**
 * The interface of the factory
 */
public interface Factory {
    /**
     * Create a new object
     * @param type the type of the object
     * @param spawnPosition the spawn position
     * @return the created object
     */
    Object create(FactoryTypes type, Vector2D spawnPosition);
}
