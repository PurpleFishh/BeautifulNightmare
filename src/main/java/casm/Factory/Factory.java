package casm.Factory;

import casm.Entities.Entity;
import casm.Utils.Vector2D;

public interface Factory {
    Entity create(FactoryTypes type, Vector2D spawnPosition);
}
