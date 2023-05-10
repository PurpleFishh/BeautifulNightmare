package casm.Factory.EntityFactory;

import casm.Objects.Entities.Enemies.WeaselFisherman;
import casm.Objects.Entities.Entity;
import casm.Objects.Entities.Player;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Objects.Object;
import casm.Utils.Vector2D;

public class EntityFactory implements Factory {
    @Override
    public Object create(FactoryTypes type, Vector2D spawnPosition) {
        if (type.equals(EntityType.PLAYER)) {
            return new Player(spawnPosition);
        } else if (type.equals(EntityType.WEASEL_FISHERMAN)) {
            return new WeaselFisherman(spawnPosition);
        } else {
            return null;
        }
    }
}
