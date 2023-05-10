package casm.Factory;

import casm.Objects.Entities.Entity;
import casm.Objects.Object;
import casm.Utils.Vector2D;

public interface Factory {
    Object create(FactoryTypes type, Vector2D spawnPosition);
}
